package com.dallasdresses.services;

import com.dallasdresses.dtos.request.ItemVariantCreateRequest;
import com.dallasdresses.dtos.request.ItemVariantUpdateRequest;
import com.dallasdresses.dtos.response.ItemVariantDto;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ItemVariantService {
    Page<ItemVariantDto> getAllVariants(Pageable pageable);
    Page<ItemVariantDto> getVariantsByItem(Long itemId, Pageable pageable);
    ItemVariantDto getItemVariantById(long id);
    ItemVariantDto createItemVariant(ItemVariantCreateRequest request);
    ItemVariantDto updateItemVariant(Long id, ItemVariantUpdateRequest request);
    void deleteItemVariant(long id);
}
