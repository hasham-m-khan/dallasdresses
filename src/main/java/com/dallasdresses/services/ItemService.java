package com.dallasdresses.services;

import com.dallasdresses.dtos.request.ItemCreateRequest;
import com.dallasdresses.dtos.request.ItemUpdateRequest;
import com.dallasdresses.dtos.response.ItemDto;

import java.util.List;

public interface ItemService {

    List<ItemDto> getAllItems();
    List<ItemDto> getItemsByCategory(String category);
    ItemDto getItemById(Long id);
    ItemDto createItem(ItemCreateRequest request);
    ItemDto updateItem(Long id, ItemUpdateRequest request);
    void deleteItem(Long id);
}
