package com.dallasdresses.services;

import com.dallasdresses.dtos.request.ItemImageCreateRequest;
import com.dallasdresses.dtos.request.ItemImageUpdateRequest;
import com.dallasdresses.dtos.response.ItemImageDto;

import java.util.List;

public interface ItemImageService {

    List<ItemImageDto> getAllImages();
    List<ItemImageDto> getImagesByItemId(Long itemId);
    ItemImageDto getImageById(Long imageId);
    ItemImageDto createImage(ItemImageCreateRequest request);
    ItemImageDto updateImage(ItemImageUpdateRequest request);
    void deleteImage(Long id);
}
