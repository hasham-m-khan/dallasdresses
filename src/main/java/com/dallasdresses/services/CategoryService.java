package com.dallasdresses.services;

import com.dallasdresses.dtos.CategoryDto;

import java.util.List;

public interface CategoryService {

    List<CategoryDto> getAllCategories();
    CategoryDto getCategoryById(Long id);
    CategoryDto getCategoryByName(String name);
    CategoryDto getCategoryBySlug(String slug);
    CategoryDto createCategory(CategoryDto categoryDto);
    CategoryDto updateCategory(CategoryDto categoryDto);
    void deleteCategory(Long id);
    void deleteCategoryByName(String name);
}
