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
        // Validate request
        if (request == null) {
            throw new InvalidEntityException("request cannot be null");
        }

        // Validate that at least one category exists
        if (request.getCategorySlugs() == null || request.getCategorySlugs().isEmpty()) {
            throw new InvalidEntityException("At least one category is required");
        }

        // Check for duplicates
        if (itemRepository.existsByNameAndColorAndSize(
                request.getName(),
                request.getColor(),
                request.getSize())) {
            throw new DuplicateEntityException("item");
        }

        // Handle parent item if this is a child
        Item parent = null;
        if (request.getParentId() != null) {
            parent = itemRepository.findById(request.getParentId())
                    .orElseThrow(() -> new EntityNotFoundException(
                            "parent item", request.getParentId()));
        }

        // Build the item
        Item item = Item.builder()
                .name(request.getName())
                .description(request.getDescription())
                .color(request.getColor())
                .size(request.getSize())
                .stock(request.getStock())
                .price(request.getPrice())
                .discountType(request.getDiscountType())
                .discountValue(request.getDiscountValue())
                .parent(parent)
                .build();

        // Handle categories
        handleCategories(item, request.getCategorySlugs());

        // Handle images
        handleItemImages(item, request.getItemImages());

        // Save item
        Item savedItem = itemRepository.save(item);

        return itemDtoConverter.convert(savedItem);
    }

    @Override
    @Transactional
    public ItemDto updateItem(Long id, ItemUpdateRequest request) {
        // Validate request
        if (request == null) {
            throw new InvalidEntityException("request body is null");
        }

        // Fetch item with id
        Item existingItem = itemRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("item", id));

        // Update basic fields
        updateExistingItemFields(existingItem, request);

        // Handle category updates if applicable
        if (request.getCategorySlugs() != null) {
            handleCategoryUpdate(existingItem, request.getCategorySlugs());
        }

        // Handle image updates if applicable
        if (request.getItemImages() != null) {
            handleImageUpdate(existingItem, request.getItemImages());
        }

        // Save item
        Item savedItem = itemRepository.save(existingItem);

        return itemDtoConverter.convert(savedItem);
    }

    @Override
    @Transactional
    public void deleteItem(Long id) {
        if (!itemRepository.existsById(id)) {
            throw new EntityNotFoundException("item", id);
        }

        itemRepository.deleteById(id);
    }

    /**
     * Validates that all categories exist and adds them to the item
     */
    private void handleCategories(Item item, Set<String> categorySlugs) {
        if (categorySlugs == null || categorySlugs.isEmpty()) {
            throw new InvalidEntityException("At least one category is required");
        }

        // Fetch categories by slugs
        List<Category> categories = categoryRepository.findBySlugIn(categorySlugs);

        // Verify all categories exist
        if (categories.size() != categorySlugs.size()) {
            Set<String> foundSlugs = categories.stream()
                    .map(Category::getSlug)
                    .collect(Collectors.toSet());

            Set<String> missingSlugs = new HashSet<>(categorySlugs);
            missingSlugs.removeAll(foundSlugs);

            throw new EntityNotFoundException("categories with slugs:" + missingSlugs);
        }

        // Add all categories to item
        categories.forEach(item::addCategory);
    }

    /**
     * Handles item images during creation.
     */
    private void handleItemImages(Item item, Set<ItemImageCreateRequest> imagesRequest) {
        if (imagesRequest == null || imagesRequest.isEmpty()) {
            return;
        }

        imagesRequest.forEach(imgRequest -> {
            ItemImage image = ItemImage.builder()
                    .url(imgRequest.getUrl())
                    .altText(imgRequest.getAltText())
                    .displayOrder(imgRequest.getDisplayOrder())
                    .isPrimary(imgRequest.getIsPrimary())
                    .build();

            item.addImage(image);
        });
    }

    /**
     * Updates basic item fields (not relationships).
     */
    private void updateExistingItemFields(Item existingItem, ItemUpdateRequest request) {
        if (request.getName() != null) {
            existingItem.setName(request.getName());
        }

        if (request.getDescription() != null) {
            existingItem.setDescription(request.getDescription());
        }

        if (request.getColor() != null) {
            existingItem.setColor(request.getColor());
        }

        if (request.getSize() != null) {
            existingItem.setSize(request.getSize());
        }

        if (request.getStock() != null) {
            existingItem.setStock(request.getStock());
        }

        if (request.getPrice() != null) {
            existingItem.setPrice(request.getPrice());
        }

        if (request.getDiscountType() != null) {
            existingItem.setDiscountType(request.getDiscountType());
        }

        if (request.getDiscountValue() != null) {
            existingItem.setDiscountValue(request.getDiscountValue());
        }

        // Handle parent change (be careful with this!)
        if (request.getParentId() != null) {
            if (request.getParentId().equals(existingItem.getId())) {
                throw new InvalidEntityException("Item cannot be its own parent");
            }

            if (!request.getParentId().equals(existingItem.getParent().getId())) {
                Item newParent = itemRepository.findById(request.getParentId())
                        .orElseThrow(() -> new EntityNotFoundException("parent item", request.getParentId()));
                existingItem.setParent(newParent);
            }
        }
    }

    /**
     * Updates item categories - replaces all existing categories.
     */
    private void handleCategoryUpdate(Item item, Set<String> categorySlugs) {
        if (categorySlugs == null || categorySlugs.isEmpty()) {
            throw new InvalidEntityException("At least one category is required");
        }

        // Fetch categories by slug
        List<Category> categories = categoryRepository.findBySlugIn(categorySlugs);

        // Verify that all the categories exist
        if (categories.size() != categorySlugs.size()) {
            Set<String> foundSlugs = categories.stream()
                    .map(Category::getSlug)
                    .collect(Collectors.toSet());

            Set<String> missingSlugs = new HashSet<>(categorySlugs);
            missingSlugs.removeAll(foundSlugs);

            throw new EntityNotFoundException("categories with slugs:" + missingSlugs);
        }

        // Create map for lookup
        Map<String, Category> categoryMap = categories.stream()
                .collect(Collectors.toMap(Category::getSlug, c -> c));

        // Clear existing categories
        Set<Category> existingCategories = new HashSet<>(item.getCategories());
        existingCategories.forEach(item::removeCategory);

        // Add new categories
        categorySlugs.forEach(slug -> {
            Category category = categoryMap.get(slug);

            if (category != null) {
                item.addCategory(category);
            }
        });
    }

    /**
     * Updates item images - replaces all existing images.
     * WARNING: Setting ID on new entities can cause issues!
     */
    private void handleImageUpdate(Item item, Set<ItemImageUpdateRequest> imageRequests) {
        // Clear existing images
        item.getItemImages().clear();

        // Add new images for this item
        if (imageRequests != null && !imageRequests.isEmpty()) {
            imageRequests.forEach(imgRequest -> {
                ItemImage image = ItemImage.builder()
                        // .id(imgRequest.getId())
                        .url(imgRequest.getUrl())
                        .altText(imgRequest.getAltText())
                        .displayOrder(imgRequest.getDisplayOrder())
                        .isPrimary(imgRequest.getIsPrimary())
                        .build();

                item.addImage(image);
            });
        }
    }
}
