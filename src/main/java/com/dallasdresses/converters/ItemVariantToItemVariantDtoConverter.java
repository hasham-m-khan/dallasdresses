package com.dallasdresses.converters;

import com.dallasdresses.dtos.response.ItemVariantDto;
import com.dallasdresses.entities.ItemVariant;
import org.springframework.core.convert.converter.Converter;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

@Component
public class ItemVariantToItemVariantDtoConverter implements Converter<ItemVariant, ItemVariantDto> {

    @Override
    public ItemVariantDto convert(@NonNull ItemVariant source) {
        return ItemVariantDto.builder()
                .id(source.getId())
                .size(source.getSize())
                .color(source.getColor())
                .price(source.getPrice())
                .stock(source.getStock())
                .itemId(source.getItem().getId())
                .itemName(source.getItem().getName())
                .itemUrl("someUrl") // TODO
                .build();
    }
}
