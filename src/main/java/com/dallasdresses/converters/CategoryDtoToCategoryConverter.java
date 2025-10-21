package com.dallasdresses.converters;

import com.dallasdresses.dtos.CategoryDto;
import com.dallasdresses.entities.Category;
import org.springframework.core.convert.converter.Converter;

public class CategoryDtoToCategoryConverter implements Converter<CategoryDto, Category> {

    @Override
    public Category convert(CategoryDto categoryDto) {

        if (categoryDto == null) {
            return null;
        }

        return Category.builder()
                .id(categoryDto.getId())
                .name(categoryDto.getName())
                .slug(categoryDto.getSlug())
                .build();
    }
}
