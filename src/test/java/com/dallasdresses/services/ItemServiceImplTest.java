package com.dallasdresses.services;

import com.dallasdresses.converters.ItemToItemDtoConverter;
import com.dallasdresses.dtos.request.*;
import com.dallasdresses.dtos.response.ItemDto;
import com.dallasdresses.entities.Category;
import com.dallasdresses.entities.Item;
import com.dallasdresses.entities.enums.DiscountType;
import com.dallasdresses.exceptions.DuplicateEntityException;
import com.dallasdresses.exceptions.EntityNotFoundException;
import com.dallasdresses.repositories.CategoryRepository;
import com.dallasdresses.repositories.ItemRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("Item Service Tests")
class ItemServiceImplTest {

    @Mock
    ItemRepository itemRepository;

    @Mock
    CategoryRepository categoryRepository;

    @Mock
    ItemToItemDtoConverter itemDtoConverter;

    @InjectMocks
    ItemServiceImpl itemService;

    Item item1;
    Item item2;
    ItemDto itemDto1;
    ItemDto itemDto2;

    @BeforeEach
    void setUp() {
        item1 = Item.builder()
                .id(1L)
                .name("Item1")
                .description("Item1")
                .summary("Item1")
                .price(new BigDecimal("33.99"))
                .discountType(DiscountType.AMOUNT)
                .discountAmount(10.00)
                .build();

        item2 = Item.builder()
                .id(2L)
                .name("Item2")
                .description("Item2")
                .summary("Item2")
                .price(new BigDecimal("12.99"))
                .discountType(DiscountType.PERCENT)
                .discountAmount(5.0)
                .build();

        itemDto1 = ItemDto.builder()
                .id(1L)
                .name("Item1")
                .description("Item1")
                .summary("Item1")
                .price(new BigDecimal("33.99"))
                .discountType(DiscountType.AMOUNT)
                .discountAmount(10.00)
                .build();

        itemDto2 = ItemDto.builder()
                .id(2L)
                .name("Item2")
                .description("Item2")
                .summary("Item2")
                .price(new BigDecimal("12.99"))
                .discountType(DiscountType.PERCENT)
                .discountAmount(5.0)
                .build();

    }

    @Test
    @DisplayName("getAllItems - Should return all items")
    void testGetAllItems_ShouldReturnAllItems_WhenNoError() {
        // Arrange
        List<Item> items = new ArrayList<>();
        items.add(item1);
        items.add(item2);

        when(itemRepository.findAllWithCategories()).thenReturn(items);
        when(itemDtoConverter.convert(item1)).thenReturn(itemDto1);
        when(itemDtoConverter.convert(item2)).thenReturn(itemDto2);

        // Act
        List<ItemDto> result = itemService.getAllItems();

        // Assert
        assertNotNull(result);
        assertEquals(items.size(), result.size());
        assertEquals(item1.getId(), result.get(0).getId());
        assertEquals(item2.getId(), result.get(1).getId());

        verify(itemRepository, times(1)).findAllWithCategories();
        verify(itemDtoConverter, times(2)).convert(any(Item.class));
    }

    @Test
    @DisplayName("getItemsByCategory - Should return items by category")
    void testGetItemsByCategory_ShouldReturnItemsByCategory_WhenNoError() {
        // Arrange
        List<Item> items = new ArrayList<>();
        items.add(item1);
        items.add(item2);

        when(itemRepository.findItemsByCategorySlug(anyString())).thenReturn(items);
        when(itemDtoConverter.convert(item1)).thenReturn(itemDto1);
        when(itemDtoConverter.convert(item2)).thenReturn(itemDto2);

        // Act
        List<ItemDto> result = itemService.getItemsByCategory("slug");

        // Assert
        assertNotNull(result);
        assertEquals(items.size(), result.size());

        verify(itemRepository, times(1)).findItemsByCategorySlug(anyString());
        verify(itemDtoConverter, times(2)).convert(any(Item.class));
    }

    @Test
    @DisplayName("getItemById - Should return item when item exists")
    void testGetItemById_ShouldReturnItem_WhenItemExists() {
        // Arrange
        when(itemRepository.findById(item1.getId())).thenReturn(Optional.of(item1));
        when(itemDtoConverter.convert(item1)).thenReturn(itemDto1);

        // Act
        ItemDto result = itemService.getItemById(item1.getId());

        // Assert
        assertEquals(itemDto1, result);

        verify(itemRepository, times(1)).findById(item1.getId());
        verify(itemDtoConverter, times(1)).convert(any(Item.class));
    }

    @Test
    @DisplayName("getItemById - Should throw EntityNotFoundException - When item not found")
    void testGetItemById_ShouldThrowEntityNotFoundException_WhenItemNotFound() {
        //Arrange
        when(itemRepository.findById(item1.getId())).thenReturn(Optional.empty());

        //Act & Assert
        assertThrows(EntityNotFoundException.class, () -> itemService.getItemById(1L));

        verify(itemRepository, times(1)).findById(item1.getId());
        verify(itemDtoConverter, never()).convert(any(Item.class));
    }

    @Test
    @DisplayName("createItem - Should create item - When No error")
    void testCreateItem_ShouldCreateItem_WhenNoError() {
        // Arrange
        CategoryCreateRequest requestCat1 = CategoryCreateRequest.builder()
                .name("Category1").slug("slug1").build();
        CategoryCreateRequest requestCat2 = CategoryCreateRequest.builder()
                .name("Category2").slug("slug2").build();

        Set<CategoryCreateRequest> categoriesRequest = new HashSet<>();
        categoriesRequest.add(requestCat1);
        categoriesRequest.add(requestCat2);

        Category category1 = Category.builder()
                .id(1L)
                .name("Category1")
                .slug("slug1")
                .items(new HashSet<>())
                .build();
        Category category2 = Category.builder()
                .id(2L)
                .name("Category2")
                .slug("slug2")
                .items(new HashSet<>())
                .build();
        List<Category> existingCategories = List.of(category1, category2);

        ItemImageCreateRequest requestImg1 = ItemImageCreateRequest.builder()
                .url("url1").build();
        ItemImageCreateRequest requestImg2 = ItemImageCreateRequest.builder()
                .url("url2").build();

        Set<ItemImageCreateRequest> imagesRequest = new HashSet<>();
        imagesRequest.add(requestImg1);
        imagesRequest.add(requestImg2);

        ItemCreateRequest requestItem1  = ItemCreateRequest.builder()
                .name(item1.getName())
                .description(item1.getDescription())
                .summary(item1.getSummary())
                .price(item1.getPrice())
                .discountType(item1.getDiscountType())
                .discountAmount(item1.getDiscountAmount())
                .categories(categoriesRequest)
                .itemImages(imagesRequest)
                .build();

        when(itemRepository.existsByNameAndPriceAndDiscountType(
                requestItem1.getName(),
                requestItem1.getPrice(),
                requestItem1.getDiscountType()))
            .thenReturn(false);
        when(categoryRepository.findBySlugIn(any())).thenReturn(existingCategories);
        when(itemRepository.save(any(Item.class))).thenReturn(item1);
        when(itemDtoConverter.convert(item1)).thenReturn(itemDto1);


        // Act
        ItemDto result = itemService.createItem(requestItem1);

        // Assert
        assertEquals(itemDto1, result);

        verify(itemRepository, times(1)).existsByNameAndPriceAndDiscountType(
                anyString(), any(BigDecimal.class), any(DiscountType.class));
        verify(categoryRepository, times(1)).findBySlugIn(any());
        verify(itemRepository, times(1)).save(any(Item.class));
        verify(itemDtoConverter, times(1)).convert(any(Item.class));
    }

    @Test
    @DisplayName("createItem - Should throw DuplicateEntityException - When item exists")
    void testCreateItem_ShouldThrowDuplicateEntityException_WhenItemExists() {
        // Arrange

        ItemCreateRequest requestItem1  = ItemCreateRequest.builder()
                .name(item1.getName())
                .price(item1.getPrice())
                .discountType(item1.getDiscountType())
                .build();

        when(itemRepository.existsByNameAndPriceAndDiscountType(
                requestItem1.getName(),
                requestItem1.getPrice(),
                requestItem1.getDiscountType()))
                .thenReturn(true);

        // Act & Assert
        assertThrows(DuplicateEntityException.class, () -> itemService.createItem(requestItem1));
        verify(itemRepository, times(1)).existsByNameAndPriceAndDiscountType(
                anyString(), any(BigDecimal.class), any(DiscountType.class));
        verify(itemRepository, never()).findById(anyLong());
        verify(categoryRepository, never()).findBySlugIn(any());
        verify(itemRepository, never()).save(any(Item.class));
        verify(itemDtoConverter, never()).convert(any(Item.class));
    }

    @Test
    @DisplayName("updateItem - Should update item - When no error")
    void testUpdateItem_ShouldUpdateItem_WhenNoError() {
        // Arrange
        CategoryUpdateRequest requestCat1 = CategoryUpdateRequest.builder()
                .name("Category1").slug("slug1").build();
        CategoryUpdateRequest requestCat2 = CategoryUpdateRequest.builder()
                .name("Category2").slug("slug2").build();

        Set<CategoryUpdateRequest> categoriesRequest = new HashSet<>();
        categoriesRequest.add(requestCat1);
        categoriesRequest.add(requestCat2);

        Category category1Req = Category.builder()
                .id(1L)
                .name("Category1")
                .slug("slug1")
                .build();
        Category category2Req = Category.builder()
                .id(2L)
                .name("Category2")
                .slug("slug2")
                .build();
        Set<Category> categories = new HashSet<>(Arrays.asList(category1Req, category2Req));
        item1.setCategories(categories);

        ItemImageUpdateRequest requestImg1 = ItemImageUpdateRequest.builder()
                .url("url1").build();
        ItemImageUpdateRequest requestImg2 = ItemImageUpdateRequest.builder()
                .url("url2").build();

        Set<ItemImageUpdateRequest> imagesRequest = new HashSet<>();
        imagesRequest.add(requestImg1);
        imagesRequest.add(requestImg2);

        ItemUpdateRequest request = ItemUpdateRequest.builder()
                .name(item1.getName())
                .description(item1.getDescription())
                .summary(item1.getSummary())
                .price(item1.getPrice())
                .discountType(item1.getDiscountType())
                .discountAmount(item1.getDiscountAmount())
                .itemImages(imagesRequest)
                .categories(categoriesRequest)
                .build();

        when(categoryRepository.findBySlugIn(any())).thenReturn(new ArrayList<Category>(categories));

        when(itemRepository.findById(item1.getId())).thenReturn(Optional.of(item1));
        when(itemRepository.save(any(Item.class))).thenReturn(item1);
        when(itemDtoConverter.convert(item1)).thenReturn(itemDto1);

        // Act
        ItemDto result = itemService.updateItem(item1.getId(), request);

        // Assert
        assertEquals(itemDto1, result);
        verify(itemRepository, times(1)).findById(item1.getId());
        verify(itemRepository, times(1)).save(any(Item.class));
        verify(itemDtoConverter, times(1)).convert(any(Item.class));
        verify(categoryRepository, times(1)).findBySlugIn(any());
    }
}