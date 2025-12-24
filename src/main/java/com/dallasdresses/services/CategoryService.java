package com.dallasdresses.services;

import com.dallasdresses.dtos.response.CategoryDto;
import com.dallasdresses.dtos.request.CategoryCreateRequest;
import com.dallasdresses.dtos.request.CategoryUpdateRequest;

import java.util.List;

import org.springframework.lang.NonNull;

public interface CategoryService {

    List<CategoryDto> getAllCategories();
    CategoryDto getCategoryById(@NonNull Long id);
    CategoryDto getCategoryByName(String name);
    CategoryDto getCategoryBySlug(String slug);
    CategoryDto createCategory(CategoryCreateRequest request);
    CategoryDto updateCategory(CategoryUpdateRequest request);
    void deleteCategory(@NonNull Long id);
    void deleteCategoryByName(String name);
}
