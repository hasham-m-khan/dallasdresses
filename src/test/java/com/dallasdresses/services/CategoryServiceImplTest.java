package com.dallasdresses.services;

import com.dallasdresses.converters.CategoryDtoToCategoryConverter;
import com.dallasdresses.converters.CategoryToCategoryDtoConverter;
import com.dallasdresses.dtos.CategoryDto;
import com.dallasdresses.entities.Category;
import com.dallasdresses.exceptions.DuplicateEntityException;
import com.dallasdresses.exceptions.EntityNotFoundException;
import com.dallasdresses.exceptions.InvalidEntityException;
import com.dallasdresses.repositories.CategoryRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("Category Service Tests")
class CategoryServiceImplTest {

    @Mock
    CategoryRepository categoryRepository;

    @Mock
    CategoryToCategoryDtoConverter categoryDtoConverter;

    @Mock
    CategoryDtoToCategoryConverter categoryConverter;

    @InjectMocks
    CategoryServiceImpl categoryService;

    Category category1;
    Category category2;

    CategoryDto category1Dto;
    CategoryDto category2Dto;

    @BeforeEach
    void setUp() {
        category1 = Category.builder()
                .id(1L)
                .name("Jackets")
                .slug("jackets")
                .build();

        category2 = Category.builder()
                .id(2L)
                .name("Jeans")
                .slug("jeans")
                .build();

        category1Dto = CategoryDto.builder()
                .id(1L)
                .name("Jackets")
                .slug("jackets")
                .build();

        category2Dto = CategoryDto.builder()
                .id(2L)
                .name("Jeans")
                .slug("jeans")
                .build();
    }

    @Test
    @DisplayName("getAllCategories - Should return all categories")
    void testGetAllCategories_ShouldReturnAllCategories() {
        // Arrange
        List<Category> categories = new ArrayList<>();
        categories.add(category1);
        categories.add(category2);

        when(categoryRepository.findAll()).thenReturn(List.of(category1, category2));

        // Act
        List<CategoryDto> categoryDtos = categoryService.getAllCategories();

        // Assert
        assertEquals(categories.size(), categoryDtos.size());
        verify(categoryRepository,times(1)).findAll();
    }

    @Test
    @DisplayName("getAllCategories - Should return all categories")
    void testGetCategoryById_ShouldReturnCategory_WhenNoErrors() {
        // Arrange
        when(categoryRepository.findById(anyLong())).thenReturn(Optional.of(category1));
        when(categoryDtoConverter.convert(any(Category.class))).thenReturn(category1Dto);

        // Act
        CategoryDto returnedCategoryDto = categoryService.getCategoryById(1L);

        // Assert
        assertEquals(category1Dto, returnedCategoryDto);
        verify(categoryRepository,times(1)).findById(anyLong());
        verify(categoryDtoConverter,times(1)).convert(any(Category.class));
    }

    @Test
    @DisplayName("getAllCategories - Should Throw EntityNotFoundException")
    void testGetCategoryById_ShouldThrowEntityNotFoundException_WhenCategoryNotFound() {
        // Arrange
        when(categoryRepository.findById(anyLong())).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(EntityNotFoundException.class, () -> categoryService.getCategoryById(1L));

        verify(categoryRepository,times(1)).findById(anyLong());
        verify(categoryDtoConverter,never()).convert(any(Category.class));
    }

    @Test
    @DisplayName("getCategoryByName - Should return category")
    void testGetCategoryByName_ShouldReturnCategory_WhenNoErrors() {
        // Arrange
        when(categoryRepository.findByName(anyString())).thenReturn(Optional.of(category1));
        when(categoryDtoConverter.convert(any(Category.class))).thenReturn(category1Dto);

        // Act
        CategoryDto returnedCategoryDto = categoryService.getCategoryByName("Jackets");

        // Assert
        assertEquals(category1Dto, returnedCategoryDto);
        verify(categoryRepository,times(1)).findByName(anyString());
        verify(categoryDtoConverter,times(1)).convert(any(Category.class));
    }

    @Test
    @DisplayName("getCategoryByName - Should Throw EntityNotFoundException")
    void testGetCategoryByName_ShouldThrowEntityNotFoundException_WhenCategoryNotFound() {
        // Arrange
        when(categoryRepository.findByName(anyString())).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(EntityNotFoundException.class, () -> categoryService.getCategoryByName("Jackets"));

        verify(categoryRepository,times(1)).findByName(anyString());
        verify(categoryDtoConverter,never()).convert(any(Category.class));
    }

    @Test
    @DisplayName("getCategoryBySlug - Should return category")
    void testGetCategoryBySlug_ShouldReturnCategory_WhenNoErrors() {
        // Arrange
        when(categoryRepository.findBySlug(anyString())).thenReturn(Optional.of(category1));
        when(categoryDtoConverter.convert(any(Category.class))).thenReturn(category1Dto);

        // Act
        CategoryDto returnedCategoryDto = categoryService.getCategoryBySlug("Jackets");

        // Assert
        assertEquals(category1Dto, returnedCategoryDto);
        verify(categoryRepository,times(1)).findBySlug(anyString());
        verify(categoryDtoConverter,times(1)).convert(any(Category.class));
    }

    @Test
    @DisplayName("getCategoryBySlug - Should Throw EntityNotFoundException")
    void testGetCategoryBySlug_ShouldThrowEntityNotFoundException_WhenCategoryNotFound() {
        // Arrange
        when(categoryRepository.findBySlug(anyString())).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(EntityNotFoundException.class, () -> categoryService.getCategoryBySlug("Jackets"));

        verify(categoryRepository,times(1)).findBySlug(anyString());
        verify(categoryDtoConverter,never()).convert(any(Category.class));
    }

    @Test
    @DisplayName("createCategory - Should create category")
    void testCreateCategory_ShouldCreateCategory_WhenNoErrors() {
        // Arrange
        CategoryDto categoryToSaveDto = CategoryDto.builder()
                .name("Casual").slug("casual").build();
        Category categoryToSave = Category.builder()
                .name("Casual").slug("casual").build();
        Category savedCategory = Category.builder()
                        .id(3L).name("Casual").slug("casual").build();
        CategoryDto savedCategoryDto = CategoryDto.builder()
                .id(3L).name("Casual").slug("casual").build();

        // Act
        when(categoryConverter.convert(categoryToSaveDto)).thenReturn(categoryToSave);
        when(categoryRepository.findByName(anyString())).thenReturn(Optional.empty());
        when(categoryRepository.save(categoryToSave)).thenReturn(savedCategory);
        when(categoryDtoConverter.convert(savedCategory)).thenReturn(savedCategoryDto);

        CategoryDto cat1 = categoryService.createCategory(categoryToSaveDto);

        // Assert
        assertEquals(categoryToSaveDto.getName(), cat1.getName());
        assertEquals(categoryToSaveDto.getSlug(), cat1.getSlug());
        verify(categoryConverter, times(1)).convert(any());
        verify(categoryRepository, times(1)).findByName(anyString());
        verify(categoryRepository,times(1)).save(categoryToSave);
        verify(categoryDtoConverter,times(1)).convert(savedCategory);
    }

    @Test
    @DisplayName("createCategory - Should throw InvalidEntityException")
    void testCreateCategory_ShouldThrowInvalidEntityException_WhenRequestIsNull() {
        // Arrange
        CategoryDto request = null;

        // Act & Assert
        assertThrows(InvalidEntityException.class, () -> categoryService.createCategory(request));
        verify(categoryConverter, never()).convert(any());
        verify(categoryRepository, never()).findByName(anyString());
        verify(categoryRepository,never()).save(any());
        verify(categoryDtoConverter,never()).convert(any());
    }

    @Test
    @DisplayName("createCategory - Should throw DuplicateEntityException")
    void testCreateCategory_ShouldThrowDuplicateEntityException_WhenCategoryExists() {
        // Arrange
        CategoryDto categoryToSaveDto = CategoryDto.builder()
                .name("Casual").slug("casual").build();
        Category savedCategory = Category.builder()
                .name("Casual").slug("casual").build();

        // Act & Assert
        when(categoryRepository.findByName(anyString())).thenReturn(Optional.of(savedCategory));

        // Assert
        assertThrows (DuplicateEntityException.class,
                () -> categoryService.createCategory(categoryToSaveDto));

        verify(categoryConverter, never()).convert(any());
        verify(categoryRepository, times(1)).findByName(anyString());
        verify(categoryRepository,never()).save(any());
        verify(categoryDtoConverter,never()).convert(any());
    }

    @Test
    @DisplayName("createCategory - Should throw InvalidEntityException")
    void testCreateCategory_ShouldThrowInvalidEntityException_WhenError() {
        // Arrange
        CategoryDto categoryToSaveDto = CategoryDto.builder()
                .name("Casual").slug("casual").build();
        Category categoryToSave = Category.builder()
                .name("Casual").slug("casual").build();
        Category savedCategory = Category.builder()
                .id(3L).name("Casual").slug("casual").build();

        // Act
        when(categoryConverter.convert(categoryToSaveDto)).thenReturn(categoryToSave);
        when(categoryRepository.findByName(anyString())).thenReturn(Optional.empty());
        when(categoryRepository.save(categoryToSave)).thenThrow(new RuntimeException("some error"));

        // Assert
        assertThrows(InvalidEntityException.class, () -> categoryService.createCategory(categoryToSaveDto));

        verify(categoryConverter, times(1)).convert(any());
        verify(categoryRepository, times(1)).findByName(anyString());
        verify(categoryRepository,times(1)).save(categoryToSave);
        verify(categoryDtoConverter, never()).convert(savedCategory);
    }

    @Test
    @DisplayName("updateCategory - Should update category")
    void testUpdateCategory_ShouldUpdateCategory_WhenNoErrors() {
        // Arrange
        CategoryDto updatedCategoryDto =  CategoryDto.builder()
                .id(1L).name("Coats").slug("coats").build();
        Category updatedCategory =  Category.builder()
                .id(1L).name("Coats").slug("coats").build();

        when(categoryRepository.findById(any())).thenReturn(Optional.of(category1));
        when(categoryConverter.convert(any(CategoryDto.class))).thenReturn(updatedCategory);
        when(categoryRepository.save(any(Category.class))).thenReturn(updatedCategory);
        when(categoryDtoConverter.convert(updatedCategory)).thenReturn(updatedCategoryDto);

        // Act
        CategoryDto returnedCategory = categoryService.updateCategory(updatedCategoryDto);

        // Assert
        assertEquals(updatedCategoryDto.getName(), returnedCategory.getName());
        assertEquals(updatedCategoryDto.getSlug(), returnedCategory.getSlug());
        assertEquals(updatedCategoryDto.getId(), returnedCategory.getId());

        verify(categoryRepository, times(1)).findById(any());
        verify(categoryDtoConverter, times(1)).convert(any(Category.class));
        verify(categoryRepository, times(1)).save(any(Category.class));
        verify(categoryDtoConverter, times(1)).convert(any(Category.class));
    }

    @Test
    @DisplayName("updateCategory - Should throw EntityNotFoundException")
    void testUpdateCategory_ShouldThrowEntityNotFoundException_WhenCategoryNotFound() {
        // Arrange
        CategoryDto updatedCategoryDto =  CategoryDto.builder()
                .id(1L).name("Coats").slug("coats").build();

        when(categoryRepository.findById(any())).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(EntityNotFoundException.class, () -> categoryService.updateCategory(updatedCategoryDto));

        verify(categoryRepository, times(1)).findById(any());
        verify(categoryConverter, never()).convert(any(CategoryDto.class));
        verify(categoryRepository, never()).save(any(Category.class));
        verify(categoryDtoConverter, never()).convert(any(Category.class));
    }

    @Test
    @DisplayName("updateCategory - Should throw InvalidEntityException")
    void testUpdateCategory_ShouldThrowInvalidEntityException_WhenError() {
        // Arrange
        CategoryDto updatedCategoryDto =  CategoryDto.builder()
                .id(1L).name("Coats").slug("coats").build();
        Category updatedCategory =  Category.builder()
                .id(1L).name("Coats").slug("coats").build();

        when(categoryRepository.findById(any())).thenReturn(Optional.of(updatedCategory));
        when(categoryConverter.convert(any(CategoryDto.class))).thenReturn(updatedCategory);
        when(categoryRepository.save(any(Category.class))).thenThrow(new RuntimeException("some error"));

        // Act & Assert
        assertThrows(InvalidEntityException.class, () -> categoryService.updateCategory(updatedCategoryDto));

        verify(categoryRepository, times(1)).findById(any());
        verify(categoryConverter, times(1)).convert(any(CategoryDto.class));
        verify(categoryRepository, times(1)).save(any(Category.class));
        verify(categoryDtoConverter, never()).convert(any(Category.class));
    }

    @Test
    @DisplayName("deleteCategory - Should delete category")
    void testDeleteCategory_ShouldDeleteCategory_WhenNoErrors() {
        // Arrange
        when(categoryRepository.findById(category1.getId()))
                .thenReturn(Optional.of(category1));

        // Act
        categoryService.deleteCategory(category1.getId());

        // Assert
        verify(categoryRepository, times(1)).findById(any());
        verify(categoryRepository, times(1)).deleteById(any());
    }

    @Test
    @DisplayName("deleteCategory - Should throw EntityNotFoundException")
    void testDeleteCategory_ShouldThrowEntityNotFoundException_WhenCategoryNotFound() {
        // Arrange
        when(categoryRepository.findById(category1.getId()))
                .thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(EntityNotFoundException.class, () ->
                categoryService.deleteCategory(category1.getId()));
        verify(categoryRepository, times(1)).findById(any());
        verify(categoryRepository, never()).deleteById(any());
    }

    @Test
    @DisplayName("deleteCategoryByName - Should delete category")
    void testDeleteCategoryByName_ShouldDeleteCategory_WhenNoErrors() {
        // Arrange
        when(categoryRepository.findByName(category1.getName()))
                .thenReturn(Optional.of(category1));

        // Act
        categoryService.deleteCategoryByName(category1.getName());

        // Assert
        verify(categoryRepository, times(1)).findByName(any());
        verify(categoryRepository, times(1)).deleteById(any());
    }

    @Test
    @DisplayName("deleteCategoryByName - Should throw EntityNotFoundException")
    void testDeleteCategoryByName_ShouldThrowEntityNotFoundException_WhenCategoryNotFound() {
        // Arrange
        when(categoryRepository.findByName(category1.getName()))
                .thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(EntityNotFoundException.class, () ->
                categoryService.deleteCategoryByName(category1.getName()));
        verify(categoryRepository, times(1)).findByName(any());
        verify(categoryRepository, never()).deleteById(any());
    }
}