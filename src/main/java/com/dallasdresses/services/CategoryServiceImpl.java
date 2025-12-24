package com.dallasdresses.services;

import com.dallasdresses.converters.CategoryToCategoryDtoConverter;
import com.dallasdresses.dtos.response.CategoryDto;
import com.dallasdresses.entities.Category;
import com.dallasdresses.exceptions.DuplicateEntityException;
import com.dallasdresses.exceptions.EntityNotFoundException;
import com.dallasdresses.exceptions.InvalidEntityException;
import com.dallasdresses.dtos.request.CategoryCreateRequest;
import com.dallasdresses.dtos.request.CategoryUpdateRequest;
import com.dallasdresses.repositories.CategoryRepository;
import lombok.extern.slf4j.Slf4j;

import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@Transactional(readOnly = true)
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;
    private final CategoryToCategoryDtoConverter categoryDtoConverter;

    public CategoryServiceImpl(CategoryRepository categoryRepository,
                               CategoryToCategoryDtoConverter categoryDtoConverter) {
        this.categoryRepository = categoryRepository;
        this.categoryDtoConverter = categoryDtoConverter;
    }

    @Override
    public List<CategoryDto> getAllCategories() {
        return categoryRepository.findAll().stream()
                .map(categoryDtoConverter::convert)
                .collect(Collectors.toList());
    }

    @Override
    public CategoryDto getCategoryById(@NonNull Long id) {

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
    @Transactional
    public @NonNull CategoryDto createCategory(CategoryCreateRequest request) {

        String slug = request.getSlug();
        if (slug == null || slug.isEmpty()) {
            slug = generateSlug(request.getName());
        }

        if (categoryRepository.existsByNameIgnoreCase(request.getName())) {
            throw new DuplicateEntityException("category", "name", request.getName());
        }

        try {
            Category category = Category.builder()
                    .name(request.getName())
                    .slug(slug)
                    .build();

            Category savedCategory = categoryRepository.save(category);

            return categoryDtoConverter.convert(savedCategory);
        } catch (Exception ex) {
            log.error("Error creating category", ex);
            throw new InvalidEntityException("Error creating category");
        }
    }

    @Override
    @Transactional
    public CategoryDto updateCategory(CategoryUpdateRequest request) {

        // Check if category exists
        Long catId = request.getId();
        if (catId == null) {
            throw new IllegalArgumentException("Category id cannot be null");
        }

        Category existingCategory = categoryRepository.findById(catId)
                .orElseThrow(() -> new EntityNotFoundException("category", catId));

        String slug = request.getSlug();
        if (slug == null || slug.isEmpty()) {
            slug = generateSlug(request.getName());
        }

        // Check if the updated category name already exists in DB
        if (categoryRepository.existsByNameIgnoreCase(request.getName())) {
            throw new DuplicateEntityException("category", "name", request.getName());
        }

        try {
            existingCategory.setName(request.getName());
            existingCategory.setSlug(slug);

            return categoryDtoConverter.convert(
                    categoryRepository.save(existingCategory)
            );
        } catch(Exception ex) {
            throw new InvalidEntityException("Error updating category");
        }
    }

    @Override
    @Transactional
    public void deleteCategory(@NonNull Long id) {
        categoryRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("category", id));

        categoryRepository.deleteById(id);
    }

    @Override
    @Transactional
    public void deleteCategoryByName(String name) {
        Integer deleteCount = categoryRepository.deleteByName(name);

        if (deleteCount == 0) {
            throw new EntityNotFoundException("category", "name", name);
        }
    }

    private String generateSlug(String name) {
        return name.toLowerCase()
                .trim()
                .replaceAll("[^a-z0-9\\s-]", "")
                .replaceAll("\\s+", "-")
                .replaceAll("-+", "-");
    }
}
