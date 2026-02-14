package com.dallasdresses.services;

import com.dallasdresses.dtos.request.AddToCartRequest;
import com.dallasdresses.dtos.request.CartItemUpdateRequest;
import com.dallasdresses.dtos.response.CartDto;

public interface CartService {

    CartDto getCartByUserId(Long userId);
    CartDto addToCart(AddToCartRequest request);
    CartDto updateCartItem(CartItemUpdateRequest request);
    CartDto removeFromCart(Long cartItemId, Long userId);
    void clearCart(Long userId);
}
