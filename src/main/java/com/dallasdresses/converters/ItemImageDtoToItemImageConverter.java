package com.dallasdresses.converters;

import com.dallasdresses.dtos.response.ItemImageDto;
import com.dallasdresses.entities.ItemImage;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class ItemImageDtoToItemImageConverter implements Converter<ItemImageDto, ItemImage> {

    // You have to set item after the object has been converted
    @Override
    public ItemImage convert(ItemImageDto source) {
        return ItemImage.builder()
                .id(source.getId())
                .url(source.getUrl())
                .build();
    }
}
