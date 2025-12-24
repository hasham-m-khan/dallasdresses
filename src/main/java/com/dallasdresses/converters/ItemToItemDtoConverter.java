package com.dallasdresses.converters;

import com.dallasdresses.dtos.response.CategoryDto;
import com.dallasdresses.dtos.response.ItemDto;
import com.dallasdresses.dtos.response.ItemImageDto;
import com.dallasdresses.dtos.response.ItemVariantDto;
import com.dallasdresses.entities.Item;
import org.springframework.core.convert.converter.Converter;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class ItemToItemDtoConverter implements Converter<Item, ItemDto> {

    private final CategoryToCategoryDtoConverter categoryDtoConverter;
    private final ItemImageToItemImageDtoConverter imageDtoConverter;
    private final ItemVariantToItemVariantDtoConverter variantDtoConverter;

    public ItemToItemDtoConverter(CategoryToCategoryDtoConverter categoryDtoConverter,
                                  ItemImageToItemImageDtoConverter imageDtoConverter,
                                  ItemVariantToItemVariantDtoConverter variantDtoConverter) {
        this.categoryDtoConverter = categoryDtoConverter;
        this.imageDtoConverter = imageDtoConverter;
        this.variantDtoConverter = variantDtoConverter;
    }

    @Override
    public ItemDto convert(@NonNull Item source) {
        return ItemDto.builder()
                .id(source.getId())
                .name(source.getName())
                .description(source.getDescription())
                .summary(source.getSummary())
                .price(source.getPrice())
                .discountType(source.getDiscountType())
                .discountAmount(source.getDiscountAmount())
                .categories(source.getCategories().stream()
                        .map(categoryDtoConverter::convert)
                        .collect(Collectors.toSet()))
                .itemImages(source.getItemImages().stream()
                        .map(imageDtoConverter::convert)
                        .collect(Collectors.toSet()))
                .itemVariants(source.getVariants().stream()
                        .map(variantDtoConverter::convert)
                        .collect(Collectors.toSet()))
                .build();
    }
}
