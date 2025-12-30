package com.dallasdresses.converters;

import com.dallasdresses.dtos.response.ItemDto;
import com.dallasdresses.entities.Item;
import org.springframework.core.convert.converter.Converter;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
public class ItemToItemDtoConverter implements Converter<Item, ItemDto> {

    private final CategoryToCategoryDtoConverter categoryDtoConverter;
    private final ItemImageToItemImageDtoConverter imageDtoConverter;

    public ItemToItemDtoConverter(CategoryToCategoryDtoConverter categoryDtoConverter,
                                  ItemImageToItemImageDtoConverter imageDtoConverter) {
        this.categoryDtoConverter = categoryDtoConverter;
        this.imageDtoConverter = imageDtoConverter;
    }

    @Override
    public ItemDto convert(@NonNull Item source) {
        return ItemDto.builder()
                .id(source.getId())
                .name(source.getName())
                .description(source.getDescription())
                .price(source.getPrice())
                .discountType(source.getDiscountType())
                .discountValue(source.getDiscountValue())
                .categories(source.getCategories().stream()
                        .map(categoryDtoConverter::convert)
                        .collect(Collectors.toSet()))
                .itemImages(source.getItemImages().stream()
                        .map(imageDtoConverter::convert)
                        .collect(Collectors.toSet()))
                .build();
    }
}
