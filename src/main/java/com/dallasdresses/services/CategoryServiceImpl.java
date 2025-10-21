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
    @Transactional
    public CategoryDto createCategory(CategoryCreateRequest request) {

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

            return categoryDtoConverter.convert(
                    categoryRepository.save(category));
        } catch (Exception ex) {
            throw new InvalidEntityException("Error creating category");
        }
    }

    @Override
    @Transactional
    public CategoryDto updateCategory(CategoryUpdateRequest request) {

        // Check if category exists
        Category existingCategory = categoryRepository.findById(request.getId())
                .orElseThrow(() -> new EntityNotFoundException("category", request.getId()));

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
    public void deleteCategory(Long id) {
        categoryRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("category", id));

        categoryRepository.deleteById(id);
    }

    @Override
    @Transactional
    public void deleteCategoryByName(String name) {
        Category category = categoryRepository.findByName(name)
                .orElseThrow(() -> new EntityNotFoundException("category", "name", name));

        categoryRepository.deleteById(category.getId());
    }

    private String generateSlug(String name) {
        return name.toLowerCase()
                .trim()
                .replaceAll("[^a-z0-9\\s-]", "")
                .replaceAll("\\s+", "-")
                .replaceAll("-+", "-");
    }
}
