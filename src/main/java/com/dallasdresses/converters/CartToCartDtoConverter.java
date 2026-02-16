package com.dallasdresses.converters;

import com.dallasdresses.dtos.response.CartDto;
import com.dallasdresses.dtos.response.CartItemDto;
import com.dallasdresses.entities.Cart;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class CartToCartDtoConverter implements Converter<Cart, CartDto> {

    private final CartItemToCartItemDtoConverter cartItemDtoConverter;

    public CartToCartDtoConverter (CartItemToCartItemDtoConverter cartItemDtoConverter) {
        this.cartItemDtoConverter = cartItemDtoConverter;
    }

    @Override
    public CartDto convert(Cart source) {
        BigDecimal subtotal = source.getSubtotal();
        BigDecimal tax = calculateTax(subtotal);
        BigDecimal total = subtotal.add(tax);
        Set<CartItemDto> itemDtos = source.getItems().stream()
                .map(cartItemDtoConverter::convert)
                .collect(Collectors.toSet());

        return CartDto.builder()
                .id(source.getId())
                .userId(source.getUser().getId())
                .items(itemDtos)
                .totalItems(source.getTotalItems())
                .subtotal(subtotal)
                .tax(tax)
                .total(tax)
                .createdAt(source.getCreatedAt())
                .updatedAt(source.getUpdatedAt())
                .build();
    }

    private BigDecimal calculateTax(BigDecimal subtotal) {
        return subtotal.multiply(new BigDecimal(0.08))
                .setScale(2, RoundingMode.HALF_UP);
    }

}
