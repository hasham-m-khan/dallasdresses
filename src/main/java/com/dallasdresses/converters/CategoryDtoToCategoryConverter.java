package com.dallasdresses.converters;

import com.dallasdresses.dtos.response.CategoryDto;
import com.dallasdresses.entities.Category;
import org.springframework.core.convert.converter.Converter;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

@Component
public class CategoryDtoToCategoryConverter implements Converter<CategoryDto, Category> {

    @Override
    public Category convert(@NonNull CategoryDto categoryDto) {

        return Category.builder()
                .id(categoryDto.getId())
                .name(categoryDto.getName())
                .slug(categoryDto.getSlug())
                .build();
    }
}
