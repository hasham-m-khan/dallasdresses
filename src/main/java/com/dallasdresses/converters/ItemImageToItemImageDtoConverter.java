package com.dallasdresses.converters;

import com.dallasdresses.dtos.response.ItemImageDto;
import com.dallasdresses.entities.ItemImage;
import org.springframework.core.convert.converter.Converter;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

@Component
public class ItemImageToItemImageDtoConverter implements Converter<ItemImage, ItemImageDto> {

    @Override
    public ItemImageDto convert(@NonNull ItemImage source) {

        return ItemImageDto.builder()
                .id(source.getId())
                .itemId(source.getItem().getId())
                .url(source.getUrl())
                .altText(source.getAltText())
                .displayOrder(source.getDisplayOrder())
                .isPrimary(source.getIsPrimary())
                .build();
    }
}
