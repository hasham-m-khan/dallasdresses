package com.dallasdresses.controllers;

import com.dallasdresses.dtos.response.CategoryDto;
import com.dallasdresses.dtos.request.CategoryCreateRequest;
import com.dallasdresses.dtos.request.CategoryUpdateRequest;
import com.dallasdresses.dtos.common.ApiResponse;
import com.dallasdresses.services.CategoryServiceImpl;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/api/v1/categories")
public class CategoryController {

    private final CategoryServiceImpl categoryService;

    public CategoryController(CategoryServiceImpl categoryService) {
        this.categoryService = categoryService;
    }

    @GetMapping("")
    public ApiResponse<List<CategoryDto>> getCategories() {

        log.info("🧲 Fetching all categories.");

        List<CategoryDto> categories = categoryService.getAllCategories();

        Map<String, Object> metadata = new HashMap<>();
        metadata.put("filtered", false);
        metadata.put("totalCount", categories.size());

        return ApiResponse.<List<CategoryDto>>builder()
                .success(true)
                .data(categories)
                .metadata(metadata)
                .message("Categories retrieved successfully")
                .build();
    }

    @GetMapping("/{id}")
    public ApiResponse<CategoryDto> getCategoryById(@PathVariable Long id) {
        log.info("🧲 Fetching category with id: {}", id);

        CategoryDto category = categoryService.getCategoryById(id);

        Map<String, Object> metadata = new HashMap<>();
        metadata.put("filtered", true);
        metadata.put("filterType", "id");

        return ApiResponse.<CategoryDto>builder()
                .success(true)
                .data(category)
                .metadata(metadata)
                .message("Category retrieved successfully")
                .build();
    }

    @GetMapping("/slug/{slug}")
    public ApiResponse<CategoryDto> getCategoryBySlug(@PathVariable String slug) {
        log.info("🧲 Fetching category with slug: {}", slug);

        CategoryDto category = categoryService.getCategoryBySlug(slug);

        Map<String, Object> metadata = new HashMap<>();
        metadata.put("filtered", true);
        metadata.put("filterType", "slug");

        return ApiResponse.<CategoryDto>builder()
                .success(true)
                .data(category)
                .metadata(metadata)
                .message("Category retrieved successfully")
                .build();
    }

    @GetMapping("/search")
    public ApiResponse<CategoryDto> getCategoryByName(@RequestParam String name) {
        log.info("🧲 Searching categories with name: {}", name);

        CategoryDto category = categoryService.getCategoryByName(name);

        Map<String, Object> metadata = new HashMap<>();
        metadata.put("filtered", true);
        metadata.put("filterType", "name");

        return ApiResponse.<CategoryDto>builder()
                .success(true)
                .data(category)
                .metadata(metadata)
                .message("Category retrieved successfully")
                .build();
    }

    @PostMapping("")
    public ApiResponse<CategoryDto> createCategory(@RequestBody @Valid CategoryCreateRequest request) {
        log.info("🔔 Creating new category: {}", request);

        CategoryDto createdCategory = categoryService.createCategory(request);

        log.info("🧶 Created category: {}", createdCategory.getName());

        return ApiResponse.<CategoryDto>builder()
                .success(true)
                .data(createdCategory)
                .metadata(new HashMap<>())
                .message("Category created successfully")
                .build();
    }

    @PutMapping("")
    public ApiResponse<CategoryDto> updateCategory(@RequestBody @Valid CategoryUpdateRequest request) {
        log.info("🔔 Updating category: {}", request);

        CategoryDto category = categoryService.updateCategory(request);

        log.info("🧶 Updated category with id '{}': {}", request.getId(), category);

        return ApiResponse.<CategoryDto>builder()
                .success(true)
                .data(category)
                .metadata(new HashMap<>())
                .message("Category updated successfully")
                .build();
    }
}
