package com.dallasdresses.converters;

import com.dallasdresses.dtos.response.CartItemDto;
import com.dallasdresses.entities.CartItem;
import com.dallasdresses.entities.Item;
import com.dallasdresses.entities.ItemImage;
import org.springframework.core.convert.converter.Converter;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class CartItemToCartItemDtoConverter implements Converter<CartItem, CartItemDto> {

    @Override
    public CartItemDto convert(@NonNull CartItem source) {

        Item item =  source.getItem();
        BigDecimal currentPrice = item.getPrice();
        Boolean priceChanged = !source.getPriceAtAdd().equals(currentPrice);
        Boolean inStock = item.getStock() != null &&
                item.getStock() >= source.getQuantity();

        return CartItemDto.builder()
                .id(source.getId())
                .itemId(item.getId())
                .itemName(item.getName())
                .itemColor(item.getColor())
                .itemSize(item.getSize())
                .primaryImageUrl(getPrimaryImageUrl(item))
                .quantity(source.getQuantity())
                .priceAtAdd(source.getPriceAtAdd())
                .currentPrice(currentPrice)
                .subtotal(source.getSubtotal())
                .availableStock(item.getStock())
                .inStock(inStock)
                .priceChanged(priceChanged)
                .build();
    }

    private String getPrimaryImageUrl(Item item) {
        return item.getItemImages().stream()
                .filter(ItemImage::getIsPrimary)
                .findFirst()
                .map(ItemImage::getUrl)
                .orElse(item.getItemImages().isEmpty() ? null :
                        item.getItemImages().iterator().next().getUrl());
    }
}
