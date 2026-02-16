package com.dallasdresses.services;

import com.dallasdresses.converters.CartToCartDtoConverter;
import com.dallasdresses.dtos.request.AddToCartRequest;
import com.dallasdresses.dtos.request.CartItemRemoveRequest;
import com.dallasdresses.dtos.request.CartItemUpdateRequest;
import com.dallasdresses.dtos.response.CartDto;
import com.dallasdresses.entities.Cart;
import com.dallasdresses.entities.CartItem;
import com.dallasdresses.entities.Item;
import com.dallasdresses.entities.User;
import com.dallasdresses.exceptions.EntityNotFoundException;
import com.dallasdresses.exceptions.InvalidEntityException;
import com.dallasdresses.repositories.CartItemRepository;
import com.dallasdresses.repositories.CartRepository;
import com.dallasdresses.repositories.ItemRepository;
import com.dallasdresses.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CartServiceImpl implements CartService {

    private final CartRepository cartRepository;
    private final UserRepository userRepository;
    private final ItemRepository itemRepository;
    private final CartItemRepository cartItemRepository;
    private final CartToCartDtoConverter cartConverter;

    @Value("${app.cart.max-items:50}")
    private int MAX_CART_ITEMS;

    @Value("${app.cart.max-quantity-per-item:10}")
    private int MAX_QUANTITY_PER_ITEM;

    @Override
    public CartDto getCartByUserId(Long userId) {
        Cart cart =  cartRepository.findByUserId(userId)
                .orElseThrow(() -> new EntityNotFoundException("cart", "user id", "" + userId));

        return cartConverter.convert(cart);
    }

    @Override
    @Transactional
    public CartDto addToCart(AddToCartRequest request) {

        // Validate item exists and is available
        Item item = getAvailableItem(request.getItemId());

        // Validate stock availability
        validateStockAvailability(item, request.getQuantity());

        // Get or create cart
        Cart cart = getOrCreateCart(request.getUserId());

        // Validate cart limits
        validateCartLimits(cart);

        // Add or update cart item
        CartItem cartItem = addOrUpdateCartItem(cart, item, request.getQuantity());

        // Save and return
        Cart savedCart = cartRepository.save(cart);
        return cartConverter.convert(savedCart);
    }

    @Override
    @Transactional
    public CartDto updateCartItem(CartItemUpdateRequest request) {
        // Validate and get cart item
        CartItem cartItem = cartItemRepository.findById(request.getCartItemId())
                .orElseThrow(() -> new EntityNotFoundException("cart item", request.getCartItemId()));

        // Verify ownership
        if (!cartItem.getCart().getUser().getId().equals(request.getUserId())) {
            throw new InvalidEntityException(("Cart item does not belong to user"));
        }

        // Validate new quantity
        validateQuantity(cartItem.getItem(), request.getQuantity());

        // Update quantity
        int oldQuantity = cartItem.getQuantity();
        cartItem.setQuantity(request.getQuantity());

        // Save and return
        Cart savedCart = cartRepository.save(cartItem.getCart());

        return cartConverter.convert(savedCart);
    }

    @Override
    @Transactional
    public CartDto removeFromCart(CartItemRemoveRequest request) {
        // Validate and get cart item
        CartItem cartItem = cartItemRepository.findById(request.getCartItemId())
                .orElseThrow(() -> new EntityNotFoundException("cart item", request.getCartItemId()));

        Cart cart = cartItem.getCart();
        User user = cart.getUser();

        // Verify ownership
        if (!user.getId().equals(request.getUserId())) {
            throw new InvalidEntityException(("Cart item does not belong to user"));
        }

        // Remove cart item from cart
        cart.removeItem(cartItem);

        // Delete cart item
        cartItemRepository.delete(cartItem);

        // Save and return cart
        Cart savedCart = cartRepository.save(cart);
        return cartConverter.convert(savedCart);
    }

    @Override
    public void clearCart(Long userId) {
        // Get cart with items
        Cart cart = cartRepository.findByUserIdWithItems(userId)
                .orElseThrow(() -> new EntityNotFoundException("cart for user", userId));

        // Check if cart is already empty
        if (cart.getItems().isEmpty()) {
            throw new InvalidEntityException("cart is already empty");
        }

        // Clear all items
        cart.clearItems();

        cartRepository.save(cart);
    }

    private Item getAvailableItem(Long itemId) {
        Item item = itemRepository.findById(itemId)
                .orElseThrow(() -> new EntityNotFoundException("item", "id", "" + itemId));

        // Validate item is not discontinued, deleted, etc.
        if (item.getStock() == null || item.getStock() == 0) {
            throw new InvalidEntityException(String.format("item '%s' is out of stock", item.getName()));
        }

        return item;
    }

    private void validateStockAvailability(Item item, Integer requestedQuantity) {
        // Check if item has enough stock
        if (requestedQuantity > item.getStock()) {
            throw new InvalidEntityException(String.format(
                    "Insufficient stock for item '%s'. Requested: %d, Available: %d",
                    item.getName(), requestedQuantity, item.getStock()));
        }

        // Check item quantity per business rule
        if (requestedQuantity > MAX_QUANTITY_PER_ITEM) {
            throw new InvalidEntityException(String.format(
                    "Cannot add more than %d units of the same item",
                    MAX_QUANTITY_PER_ITEM
            ));
        }
    }

    private Cart getOrCreateCart(Long userId) {
        // Return existing cart if it exists
        Optional<Cart> existingCart = cartRepository.findByUserIdWithItems(userId);
        if (existingCart.isPresent()) {
            return existingCart.get();
        }

        // Create new cart if cart doesn't exist
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("user", "id", "" + userId));
        Cart newCart = Cart.builder()
                .user(user)
                .build();

        return cartRepository.save(newCart);
    }

    private void validateCartLimits(Cart cart) {
        if (cart.getItems().size() >= MAX_CART_ITEMS) {
            throw new InvalidEntityException(
                    String.format("Cart cannot contain more than %d items", MAX_CART_ITEMS)
            );
        }
    }

    private CartItem addOrUpdateCartItem(Cart cart, Item item, Integer quantity) {
        Optional<CartItem> existingItem = cartItemRepository.findByCartIdAndItemId(cart.getId(), item.getId());

        if (existingItem.isPresent()) {
            // Update existing item if exists
            CartItem cartItem = existingItem.get();
            int newQuantity = cartItem.getQuantity() + quantity;

            // Validate new total quantity
            validateStockAvailability(item, newQuantity);

            cartItem.setQuantity(newQuantity);

            return cartItem;
        } else {
            CartItem newCartItem = CartItem.builder()
                    .item(item)
                    .quantity(quantity)
                    .priceAtAdd(item.getPrice())
                    .build();

            cart.addItem(newCartItem);

            return newCartItem;
        }
    }

    private void validateQuantity(Item item, Integer quantity) {
        if (quantity <= 0) {
            throw new InvalidEntityException("quantity must be greater than 0");
        }

        if (quantity >item.getStock()) {
            throw new InvalidEntityException(
                    String.format(
                            "Insufficient stock for item '%s'. Requested: %d, Available: %d",
                            item.getName(), quantity, item.getStock()));
        }

        if (quantity > MAX_QUANTITY_PER_ITEM) {
            throw new InvalidEntityException(
                    String.format(
                            "Cannot add more than %d units of the same item"
                    , MAX_QUANTITY_PER_ITEM));
        }
    }
}
