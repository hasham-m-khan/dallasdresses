package com.dallasdresses.converters;

import com.dallasdresses.dtos.response.ItemDto;
import com.dallasdresses.entities.Item;
import org.springframework.core.convert.converter.Converter;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
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

        List<ItemDto> children = new ArrayList<>();
        if (source.getChildren() != null && !source.getChildren().isEmpty()) {
            source.getChildren().forEach(child -> {
               ItemDto childDto = convert(child);
               children.add(childDto);
            });
        }

        return ItemDto.builder()
                .id(source.getId())
                .name(source.getName())
                .description(source.getDescription())
                .color(source.getColor())
                .size(source.getSize())
                .stock(source.getStock())
                .price(source.getPrice())
                .discountType(source.getDiscountType())
                .discountValue(source.getDiscountValue())
                .averageRating(source.getAverageRating())
                .totalRatings(source.getTotalRatings())
                .parentId(source.getParent() != null ? source.getParent().getId() : null)
                .isParent(source.isParent())
                .children(children)
                .categories(source.getCategories() != null
                        ? source.getCategories().stream()
                            .map(categoryDtoConverter::convert)
                            .collect(Collectors.toSet())
                        : Collections.emptySet())

                .itemImages(source.getItemImages() != null
                        ? source.getItemImages().stream()
                            .map(imageDtoConverter::convert)
                            .collect(Collectors.toSet())
                        : Collections.emptySet())
                .createdAt(source.getCreatedAt())
                .updatedAt(source.getUpdatedAt())
                .build();
    }
}
