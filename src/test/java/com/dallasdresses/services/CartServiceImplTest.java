package com.dallasdresses.services;

import com.dallasdresses.converters.CartToCartDtoConverter;
import com.dallasdresses.dtos.request.AddToCartRequest;
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
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CartServiceImplTest {

    @Mock
    CartRepository cartRepository;

    @Mock
    CartToCartDtoConverter cartConverter;

    @Mock
    UserRepository userRepository;

    @Mock
    ItemRepository itemRepository;

    @Mock
    CartItemRepository cartItemRepository;

    @InjectMocks
    CartServiceImpl cartService;

    Cart cart;
    CartDto cartDto;
    User user;
    AddToCartRequest addRequest;
    Item item;
    CartItem cartItem;

    Long CART_ID = 1L;
    Long USER_ID = 10L;
    Long ITEM_ID = 100L;
    Integer ITEM_STOCK = 10;
    Long CART_ITEM_ID = 2L;

    @BeforeEach
    void setUp() {

        user = User.builder().id(USER_ID).build();

        cart = Cart.builder().id(CART_ID).user(user).build();
        cartDto = CartDto.builder().id(CART_ID).userId(USER_ID).build();
        addRequest = AddToCartRequest.builder().userId(USER_ID).itemId(ITEM_ID).quantity(ITEM_STOCK - 5).build();
        item = Item.builder().id(ITEM_ID).stock(ITEM_STOCK).build();
        cartItem = CartItem.builder().id(CART_ITEM_ID).cart(cart).item(item).quantity(2).build();
    }

    @Test
    @DisplayName("getCartByUserId - Should return Cart - When found")
    void testGetCartByUserId_ShouldReturnCart_WhenFound() {
        // Arrange
        when(cartRepository.findByUserId(USER_ID)).thenReturn(Optional.of(cart));
        when(cartConverter.convert(cart)).thenReturn(cartDto);

        // Act
        CartDto result = cartService.getCartByUserId(USER_ID);

        // Assert
        assertEquals(cartDto, result);
        verify(cartRepository, times(1)).findByUserId(USER_ID);
        verify(cartConverter, times(1)).convert(cart);
    }

    @Test
    @DisplayName("getCartByUserId - Should throw EntityNotFoundException - When not found")
    void testGetCartByUserId_ShouldThrowEntityNotFoundException_WhenNotFound() {
        // Arrange
        when(cartRepository.findByUserId(USER_ID)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(EntityNotFoundException.class, () -> cartService.getCartByUserId(USER_ID));

        verify(cartRepository, times(1)).findByUserId(USER_ID);
        verify(cartConverter, never()).convert(cart);
    }

    @Test
    @DisplayName("addToCart - Should add to cart - When no errors")
    void testAddToCart_ShouldAddToCart_WhenNoErrors() {
        // Arrange
        when(itemRepository.findById(ITEM_ID)).thenReturn(Optional.of(item));
        when(cartRepository.findByUserIdWithItems(USER_ID)).thenReturn(Optional.of(cart));
        when(cartItemRepository.findByCartIdAndItemId(CART_ID, ITEM_ID)).thenReturn(Optional.of(cartItem));
        when(cartRepository.save(cart)).thenReturn(cart);
        when(cartConverter.convert(cart)).thenReturn(cartDto);

        // Act
        CartDto result = cartService.addToCart(addRequest);

        // Assert
        assertEquals(cartDto, result);

        verify(itemRepository, times(1)).findById(ITEM_ID);
        verify(cartRepository, times(1)).findByUserIdWithItems(USER_ID);
        verify(userRepository, never()).findById(USER_ID);
        verify(cartItemRepository, times(1)).findByCartIdAndItemId(CART_ID, ITEM_ID);
        verify(cartRepository, times(1)).save(cart);
        verify(cartConverter, times(1)).convert(cart);
    }

    @Test
    @DisplayName("addToCart - Should throw EntityNotFoundException - When item not found")
    void testAddToCart_ShouldThrowEntityNotFoundException_WhenItemNotFound() {
        // Arrange
        when(itemRepository.findById(ITEM_ID)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(EntityNotFoundException.class, () -> cartService.addToCart(addRequest));

        verify(itemRepository, times(1)).findById(ITEM_ID);
        verify(cartRepository, never()).findByUserIdWithItems(USER_ID);
        verify(userRepository, never()).findById(USER_ID);
        verify(cartItemRepository, never()).findByCartIdAndItemId(CART_ID, ITEM_ID);
        verify(cartRepository, never()).save(cart);
        verify(cartConverter, never()).convert(cart);
    }

    @Test
    @DisplayName("addToCart - Should throw InvalidEntityException - When item out of stock")
    void testAddToCart_ShouldThrowInvalidEntityException_WhenItemOutOfStock() {
        // Arrange
        item.setStock(0);

        when(itemRepository.findById(ITEM_ID)).thenReturn(Optional.of(item));

        // Act & Assert
        assertThrows(InvalidEntityException.class, () -> cartService.addToCart(addRequest));

        verify(itemRepository, times(1)).findById(ITEM_ID);
        verify(cartRepository, never()).findByUserIdWithItems(USER_ID);
        verify(userRepository, never()).findById(USER_ID);
        verify(cartItemRepository, never()).findByCartIdAndItemId(CART_ID, ITEM_ID);
        verify(cartRepository, never()).save(cart);
        verify(cartConverter, never()).convert(cart);
    }

    @Test
    @DisplayName("addToCart - Should throw InvalidEntityException - When requested quantity greater than item stock")
    void testAddToCart_ShouldThrowInvalidEntityException_WhenRequestedQuantityGreaterThanItemStock() {
        // Arrange
        addRequest.setQuantity(20);

        when(itemRepository.findById(ITEM_ID)).thenReturn(Optional.of(item));

        // Act & Assert
        assertThrows(InvalidEntityException.class, () -> cartService.addToCart(addRequest));

        verify(itemRepository, times(1)).findById(ITEM_ID);
        verify(cartRepository, never()).findByUserIdWithItems(USER_ID);
        verify(userRepository, never()).findById(USER_ID);
        verify(cartItemRepository, never()).findByCartIdAndItemId(CART_ID, ITEM_ID);
        verify(cartRepository, never()).save(cart);
        verify(cartConverter, never()).convert(cart);
    }

    @Test
    @DisplayName("addToCart - Should throw InvalidEntityException - When requested quantity greater than max quantity")
    void testAddToCart_ShouldThrowInvalidEntityException_WhenRequestedQuantityGreaterThanMaxQuantity() {
        // Arrange
        item.setStock(30);
        addRequest.setQuantity(20);

        when(itemRepository.findById(ITEM_ID)).thenReturn(Optional.of(item));

        // Act & Assert
        assertThrows(InvalidEntityException.class, () -> cartService.addToCart(addRequest));

        verify(itemRepository, times(1)).findById(ITEM_ID);
        verify(cartRepository, never()).findByUserIdWithItems(USER_ID);
        verify(userRepository, never()).findById(USER_ID);
        verify(cartItemRepository, never()).findByCartIdAndItemId(CART_ID, ITEM_ID);
        verify(cartRepository, never()).save(cart);
        verify(cartConverter, never()).convert(cart);
    }

    @Test
    @DisplayName("addToCart - Should throw EntityNotFoundException - When user not found")
    void testAddToCart_ShouldThrowEntityNotFoundException_WhenUserNotFound() {
        // Arrange
        when(itemRepository.findById(ITEM_ID)).thenReturn(Optional.of(item));
        when(cartRepository.findByUserIdWithItems(USER_ID)).thenReturn(Optional.empty());
        when(userRepository.findById(user.getId())).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(EntityNotFoundException.class, () -> cartService.addToCart(addRequest));

        verify(itemRepository, times(1)).findById(ITEM_ID);
        verify(cartRepository, times(1)).findByUserIdWithItems(USER_ID);
        verify(userRepository, times(1)).findById(USER_ID);
        verify(cartItemRepository, never()).findByCartIdAndItemId(CART_ID, ITEM_ID);
        verify(cartRepository, never()).save(cart);
        verify(cartConverter, never()).convert(cart);
    }

    @Test
    void updateCartItem() {
    }

    @Test
    void removeFromCart() {
    }

    @Test
    void clearCart() {
    }
}