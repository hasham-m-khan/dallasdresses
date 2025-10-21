package com.dallasdresses.converters;

import com.dallasdresses.dtos.CategoryDto;
import com.dallasdresses.entities.Category;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class CategoryToCategoryDtoConverter implements Converter<Category, CategoryDto> {

    @Override
    public CategoryDto convert(Category category) {

        if (category == null) {
            return null;
        }

        return CategoryDto.builder()
                .id(category.getId())
                .name(category.getName())
                .slug(category.getSlug())
                .build();
    }
}
