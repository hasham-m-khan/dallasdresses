package com.dallasdresses.services;

import com.dallasdresses.converters.ItemToItemDtoConverter;
import com.dallasdresses.dtos.request.*;
import com.dallasdresses.dtos.response.ItemDto;
import com.dallasdresses.entities.Category;
import com.dallasdresses.entities.Item;
import com.dallasdresses.entities.ItemImage;
import com.dallasdresses.exceptions.DuplicateEntityException;
import com.dallasdresses.exceptions.EntityNotFoundException;
import com.dallasdresses.exceptions.InvalidEntityException;
import com.dallasdresses.repositories.CategoryRepository;
import com.dallasdresses.repositories.ItemRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
@Transactional(readOnly = true)
public class ItemServiceImpl implements ItemService {

    private final ItemRepository itemRepository;
    private final CategoryRepository categoryRepository;
    private final ItemToItemDtoConverter itemDtoConverter;

    public ItemServiceImpl(ItemRepository itemRepository, CategoryRepository categoryRepository,
                           ItemToItemDtoConverter itemDtoConverter) {
        this.itemRepository = itemRepository;
        this.categoryRepository = categoryRepository;
        this.itemDtoConverter = itemDtoConverter;
    }

    @Override
    public List<ItemDto> getAllItems() {
        return itemRepository.findAllWithCategories().stream()
                .map(itemDtoConverter::convert)
                .collect(Collectors.toList());
    }

    @Override
    public List<ItemDto> getItemsByCategory(String slug) {
        return itemRepository.findItemsByCategorySlug(slug).stream()
                .map(itemDtoConverter::convert)
                .collect(Collectors.toList());
    }

    @Override
    public ItemDto getItemById(Long id) {
        Item item = itemRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("item", id));

        return itemDtoConverter.convert(item);
    }

    @Override
    @Transactional
    public ItemDto createItem(ItemCreateRequest request) {

        // Check if item already exists
        if (itemRepository.existsByNameAndPriceAndDiscountType(
                request.getName(),
                request.getPrice(),
                request.getDiscountType())) {
            throw new DuplicateEntityException("item");
        }

        // Create new item
        Item item = Item.builder()
                .name(request.getName())
                .description(request.getDescription())
                .summary(request.getSummary())
                .price(request.getPrice())
                .discountType(request.getDiscountType())
                .discountAmount(request.getDiscountAmount())
                .build();

        handleCategories(item, request.getCategories());
        handleItemImages(item, request.getItemImages());

        // Save item
        Item savedItem = itemRepository.save(item);
        return itemDtoConverter.convert(savedItem);
    }

    @Override
    @Transactional
    public ItemDto updateItem(Long id, ItemUpdateRequest request) {
        if (request == null) {
            throw new InvalidEntityException("request body is null");
        }

        // Fetch item with id
        Item existingItem = itemRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("item", id));

        // Set fields
        updateExistingItemFields(existingItem, request);

        Item savedItem = itemRepository.save(existingItem);
        return itemDtoConverter.convert(savedItem);
    }

    @Override
    @Transactional
    public void deleteItem(Long id) {
        itemRepository.deleteById(id);
    }

    private void handleCategories(Item item, Set<CategoryCreateRequest> categoriesRequest) {
        if (categoriesRequest == null || categoriesRequest.isEmpty()) {
            return;
        }

        Set<String> slugs = categoriesRequest.stream()
                .map(CategoryCreateRequest::getSlug)
                .collect(Collectors.toSet());

        Map<String, Category> existingCategories = categoryRepository
                .findBySlugIn(slugs)
                .stream()
                .collect(Collectors.toMap(Category::getSlug, c -> c));

        categoriesRequest.forEach(catRequest -> {
            Category category = existingCategories.computeIfAbsent(
                    catRequest.getSlug(),
                    slug -> Category.builder()
                            .name(catRequest.getName())
                            .slug(slug)
                            .build()
            );

            item.addCategory(category);
        });
    }

    private void handleItemImages(Item item, Set<ItemImageCreateRequest> imagesRequest) {
        if (imagesRequest == null || imagesRequest.isEmpty()) {
            return;
        }

        imagesRequest.forEach(imgRequest -> {
            ItemImage image = ItemImage.builder()
                    .url(imgRequest.getUrl())
                    .build();

            item.addImage(image);
        });
    }

    private void updateExistingItemFields(Item existingItem, ItemUpdateRequest request) {

        if (request.getName() != null) {
            existingItem.setName(request.getName());
        }

        if (request.getDescription() != null) {
            existingItem.setDescription(request.getDescription());
        }

        if (request.getSummary() != null) {
            existingItem.setSummary(request.getSummary());
        }

        if (request.getPrice() != null) {
            existingItem.setPrice(request.getPrice());
        }

        if (request.getDiscountType() != null) {
            existingItem.setDiscountType(request.getDiscountType());
        }

        if (request.getDiscountAmount() != null) {
            existingItem.setDiscountAmount(request.getDiscountAmount());
        }

        if (request.getCategories() != null) {
            handleCategoryUpdate(existingItem, request.getCategories());
        }

        if (request.getItemImages() != null) {
            handleImageUpdate(existingItem, request.getItemImages());
        }
    }

    private void handleCategoryUpdate(Item item, Set<CategoryUpdateRequest> categoryRequests) {
        Set<String> slugs = categoryRequests.stream()
                .map(CategoryUpdateRequest::getSlug)
                .collect(Collectors.toSet());

        Map<String, Category> existingCategories = categoryRepository
                .findBySlugIn(slugs)
                .stream()
                .collect(Collectors.toMap(Category::getSlug, c -> c));

        item.getCategories().clear();

        categoryRequests.forEach(catRequest -> {
            Category category = existingCategories.computeIfAbsent(
                    catRequest.getSlug(),
                    slug -> categoryRepository.save(Category.builder()
                            .name(catRequest.getName())
                            .slug(slug)
                            .build())
            );

            item.addCategory(category);
        });
    }

    private void handleImageUpdate(Item item, Set<ItemImageUpdateRequest> imageRequests) {
        item.getItemImages().clear();

        // Add new images for this item
        imageRequests.forEach(imgRequest -> {
            ItemImage image = ItemImage.builder()
                    .url(imgRequest.getUrl())
                    .build();

            item.addImage(image);
        });
    }
}
