package com.dallasdresses.controllers;

import com.dallasdresses.dtos.common.ApiResponse;
import com.dallasdresses.dtos.request.AddToCartRequest;
import com.dallasdresses.dtos.request.CartItemRemoveRequest;
import com.dallasdresses.dtos.request.CartItemUpdateRequest;
import com.dallasdresses.dtos.response.CartDto;
import com.dallasdresses.services.CartService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("${app.api.baseurl}/carts")
public class CartController {

    private final CartService cartService;

    @GetMapping("/user/{userId}")
    public ApiResponse<CartDto> getCart(@PathVariable("userId") Long userId) {
        log.info("🧲 Fetching cart for user id: {}",  userId);

        CartDto cart = cartService.getCartByUserId(userId);

        Map<String, Object> metadata = new HashMap<>();
        metadata.put("itemCount", cart.getItems().size());

        return ApiResponse.<CartDto>builder()
                .success(true)
                .data(cart)
                .metadata(metadata)
                .message("cart retrieved successfully")
                .build();
    }

    @PostMapping("/items")
    public ApiResponse<CartDto> addToCart(@Valid @RequestBody AddToCartRequest request) {
        log.info("🔔 Adding item to cart - user id: {}, item id: {}, quantity: {}",
                request.getUserId(), request.getItemId(), request.getQuantity());

        CartDto cart = cartService.addToCart(request);

        Map<String, Object> metadata = new HashMap<>();
        metadata.put("itemCount", cart.getItems().size());
        metadata.put("itemAdded", request.getItemId());

        return ApiResponse.<CartDto>builder()
                .success(true)
                .data(cart)
                .metadata(metadata)
                .message("item added to cart successfully")
                .build();
    }

    @PutMapping("/items/{cartItemId}")
    public ApiResponse<CartDto> updateCartItem(
            @PathVariable("cartItemId") Long cartItemId,
            @Valid @RequestBody CartItemUpdateRequest request) {
        log.info("🔔 Updating cart item ID: {} - New Quantity: {}", cartItemId, request.getQuantity());

        request.setCartItemId(cartItemId);
        CartDto cart = cartService.updateCartItem(request);

        Map<String, Object> metadata = new HashMap<>();
        metadata.put("itemCount", cart.getItems().size());
        metadata.put("updatedCartItemId", cartItemId);

        return ApiResponse.<CartDto>builder()
                .success(true)
                .data(cart)
                .metadata(metadata)
                .message("cart item updated successfully")
                .build();
    }

    @DeleteMapping("/items/{cartItemId}")
    public ApiResponse<CartDto> removeFromCart(
            @PathVariable("cartItemId") Long cartItemId,
            @RequestParam("userId") Long userId) {
        log.info("🗑️ Removing cart item ID: {} for user ID: {}", cartItemId, userId);

        CartItemRemoveRequest request = CartItemRemoveRequest.builder()
                .cartItemId(cartItemId)
                .userId(userId)
                .build();

        CartDto cart = cartService.removeFromCart(request);

        Map<String, Object> metadata = new HashMap<>();
        metadata.put("itemCount", cart.getItems().size());
        metadata.put("removedCartItemId", cartItemId);

        return ApiResponse.<CartDto>builder()
                .success(true)
                .data(cart)
                .metadata(metadata)
                .message("item removed from cart successfully")
                .build();
    }

    @DeleteMapping("/user/{userId}")
    public ApiResponse<Void> clearCart(@PathVariable("userId") Long userId) {
        log.info("🧹 Clearing cart for user ID: {}", userId);

        cartService.clearCart(userId);

        return ApiResponse.<Void>builder()
                .success(true)
                .data(null)
                .message("cart cleared successfully")
                .build();
    }
}
