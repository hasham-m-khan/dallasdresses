package com.dallasdresses.services;

import com.dallasdresses.converters.ItemVariantToItemVariantDtoConverter;
import com.dallasdresses.dtos.request.ItemVariantCreateRequest;
import com.dallasdresses.dtos.request.ItemVariantUpdateRequest;
import com.dallasdresses.dtos.response.ItemVariantDto;
import com.dallasdresses.entities.Item;
import com.dallasdresses.entities.ItemVariant;
import com.dallasdresses.entities.enums.DressSize;
import com.dallasdresses.exceptions.DuplicateEntityException;
import com.dallasdresses.exceptions.EntityNotFoundException;
import com.dallasdresses.repositories.ItemRepository;
import com.dallasdresses.repositories.ItemVariantRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ItemVariantServiceImpl implements ItemVariantService {

    private final ItemVariantRepository variantRepository;
    private final ItemRepository itemRepository;
    private final ItemVariantToItemVariantDtoConverter varDtoConverter;

    public ItemVariantServiceImpl(ItemVariantRepository variantRepository,
                                  ItemRepository itemRepository,
                                  ItemVariantToItemVariantDtoConverter varDtoConverter) {
        this.variantRepository = variantRepository;
        this.itemRepository = itemRepository;
        this.varDtoConverter = varDtoConverter;
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ItemVariantDto> getAllVariants(Pageable pageable) {
        return variantRepository.findAllWithItem(pageable)
                .map(varDtoConverter::convert);
    }

    @Override
    @Transactional
    public Page<ItemVariantDto> getVariantsByItem(Long itemId, Pageable pageable) {

        if (!itemRepository.existsById(itemId)) {
            throw new EntityNotFoundException("item",  itemId);
        }

        return variantRepository.getItemVariantsByItemId(itemId, pageable)
                .map(varDtoConverter::convert);
    }

    @Override
    @Transactional(readOnly = true)
    public ItemVariantDto getItemVariantById(long id) {

        ItemVariant variant = variantRepository.findByIdWithItem(id)
                .orElseThrow(() -> new EntityNotFoundException("Item variant", id));

        return varDtoConverter.convert(variant);
    }

    @Override
    @Transactional
    public ItemVariantDto createItemVariant(Long itemId, ItemVariantCreateRequest request) {
        if (variantRepository.existsByItemIdAndColorAndSize(
                itemId,
                request.getColor(),
                request.getSize())) {
            throw new DuplicateEntityException("Item variant");
        }

        Item item = itemRepository.findById(itemId)
                .orElseThrow(() -> new EntityNotFoundException("Item", itemId));

        ItemVariant variant = ItemVariant.builder()
                .color(request.getColor())
                .size(request.getSize())
                .price(request.getPrice())
                .stock(request.getStock())
                .build();
        item.addVariant(variant);

        ItemVariant savedVariant = variantRepository.save(variant);
        return varDtoConverter.convert(savedVariant);
    }

    @Override
    @Transactional
    public ItemVariantDto updateItemVariant(Long itemId, Long variantId, ItemVariantUpdateRequest request) {

        ItemVariant variant = variantRepository.findByIdAndItemId(variantId, itemId)
                .orElseThrow(() -> new EntityNotFoundException(
                        String.format("Variant with id: %d not found for item with id: %d", variantId, itemId)));

        // Check for duplicate if color/size changed
        if (isColorOrSizeChanged(variant, request)) {
            String newColor = request.getColor() != null ? request.getColor() : variant.getColor();
            DressSize newSize = request.getSize() != null ? request.getSize() : variant.getSize();

            if (variantRepository.existsByItemIdAndColorAndSizeAndIdNot(
                    variant.getItem().getId(), newColor, newSize, variant.getId())) {
                throw new DuplicateEntityException(
                        "Another variant with this color and size already exists");
            }
        }

        updateVariantFields(variant, request);
        ItemVariant updatedVariant = variantRepository.save(variant);

        return varDtoConverter.convert(updatedVariant);
    }

    @Override
    public void deleteItemVariant(long id) {
        ItemVariant variant = variantRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Item variant", id));

        variantRepository.delete(variant);
    }

    private boolean isColorOrSizeChanged(ItemVariant variant, ItemVariantUpdateRequest request) {
        return (request.getColor() != null && !request.getColor().equals(variant.getColor())) ||
                (request.getSize() != null && !request.getSize().equals(variant.getSize()));
    }

    private void updateVariantFields(ItemVariant variant, ItemVariantUpdateRequest request) {
        if (request.getColor() != null) {
            variant.setColor(request.getColor());
        }
        if (request.getSize() != null) {
            variant.setSize(request.getSize());
        }
        if (request.getPrice() != null) {
            variant.setPrice(request.getPrice());
        }
        if (request.getStock() != null) {
            variant.setStock(request.getStock());
        }
    }
}
