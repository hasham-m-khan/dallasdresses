package com.dallasdresses.services;

import com.dallasdresses.converters.CategoryDtoToCategoryConverter;
import com.dallasdresses.converters.CategoryToCategoryDtoConverter;
import com.dallasdresses.dtos.CategoryDto;
import com.dallasdresses.entities.Category;
import com.dallasdresses.exceptions.DuplicateEntityException;
import com.dallasdresses.exceptions.EntityNotFoundException;
import com.dallasdresses.exceptions.InvalidEntityException;
import com.dallasdresses.repositories.CategoryRepository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;
    private final CategoryToCategoryDtoConverter categoryDtoConverter;
    private final CategoryDtoToCategoryConverter categoryConverter;

    public CategoryServiceImpl(CategoryRepository categoryRepository,
                               CategoryToCategoryDtoConverter categoryDtoConverter,
                               CategoryDtoToCategoryConverter categoryConverter) {
        this.categoryRepository = categoryRepository;
        this.categoryDtoConverter = categoryDtoConverter;
        this.categoryConverter = categoryConverter;
    }

    @Override
    public List<CategoryDto> getAllCategories() {
        return categoryRepository.findAll().stream()
                .map(categoryDtoConverter::convert)
                .collect(Collectors.toList());
    }

    @Override
    public CategoryDto getCategoryById(Long id) {

        return categoryRepository.findById(id)
                .map(categoryDtoConverter::convert)
                .orElseThrow(() -> new EntityNotFoundException("category", id));
    }

    @Override
    public CategoryDto getCategoryByName(String name) {

        return categoryRepository.findByName(name)
                .map(categoryDtoConverter::convert)
                .orElseThrow(() -> new EntityNotFoundException("category", "name", name));
    }

    @Override
    public CategoryDto getCategoryBySlug(String slug) {
        return categoryRepository.findBySlug(slug)
                .map(categoryDtoConverter::convert)
                .orElseThrow(() -> new EntityNotFoundException("category", "slug", slug));
    }

    @Override
    public CategoryDto createCategory(CategoryDto categoryDto) {

        if (categoryDto == null) {
            throw new InvalidEntityException("category request is null");
        }

        categoryRepository.findByName(categoryDto.getName())
                .ifPresent(category -> {
                    throw new DuplicateEntityException("category", "name", category.getName());
                });

        try {
            Category category = categoryConverter.convert(categoryDto);
            return categoryDtoConverter.convert(
                    categoryRepository.save(category));
        } catch (Exception ex) {
            throw new InvalidEntityException("Error creating category");
        }
    }

    @Override
    public CategoryDto updateCategory(CategoryDto categoryDto) {

        if (categoryDto == null) {
            throw new InvalidEntityException("category request is null");
        }

        categoryRepository.findById(categoryDto.getId())
                .orElseThrow(() -> new EntityNotFoundException("category", categoryDto.getId()));

        try {
            Category  category = categoryConverter.convert(categoryDto);
            return categoryDtoConverter.convert(
                    categoryRepository.save(category)
            );
        } catch(Exception ex) {
            throw new InvalidEntityException("Error updating category");
        }
    }

    @Override
    public void deleteCategory(Long id) {
        categoryRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("category", id));

        categoryRepository.deleteById(id);
    };

    @Override
    public void deleteCategoryByName(String name) {
        Category category = categoryRepository.findByName(name)
                .orElseThrow(() -> new EntityNotFoundException("category", "name", name));

        categoryRepository.deleteById(category.getId());
    }
}
