package com.dallasdresses.converters;

import com.dallasdresses.dtos.response.CategoryDto;
import com.dallasdresses.entities.Category;
import org.springframework.core.convert.converter.Converter;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

@Component
public class CategoryToCategoryDtoConverter implements Converter<Category, CategoryDto> {

    @Override
    public CategoryDto convert(@NonNull Category category) {

        return CategoryDto.builder()
                .id(category.getId())
                .name(category.getName())
                .slug(category.getSlug())
                .build();
    }
}
