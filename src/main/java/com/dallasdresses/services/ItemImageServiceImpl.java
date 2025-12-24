package com.dallasdresses.services;

import com.dallasdresses.converters.ItemImageToItemImageDtoConverter;
import com.dallasdresses.dtos.request.ItemImageCreateRequest;
import com.dallasdresses.dtos.request.ItemImageUpdateRequest;
import com.dallasdresses.dtos.response.ItemImageDto;
import com.dallasdresses.entities.Item;
import com.dallasdresses.entities.ItemImage;
import com.dallasdresses.exceptions.EntityNotFoundException;
import com.dallasdresses.exceptions.InvalidEntityException;
import com.dallasdresses.repositories.ItemImageRepository;
import com.dallasdresses.repositories.ItemRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@Transactional(readOnly = true)
public class ItemImageServiceImpl implements ItemImageService {

    private final ItemImageRepository imageRepository;
    private final ItemRepository itemRepository;
    private final ItemImageToItemImageDtoConverter imageDtoConverter;

    public ItemImageServiceImpl(ItemImageRepository imageRepository,
                                ItemRepository itemRepository,
                                ItemImageToItemImageDtoConverter imageDtoConverter) {
        this.imageRepository = imageRepository;
        this.itemRepository = itemRepository;
        this.imageDtoConverter = imageDtoConverter;
    }

    @Override
    public List<ItemImageDto> getAllImages() {
        return imageRepository.findAll().stream()
                .map(imageDtoConverter::convert)
                .collect(Collectors.toList());
    }

    @Override
    public List<ItemImageDto> getImagesByItemId(Long itemId) {
        return imageRepository.findByItemId(itemId).stream()
                .map(imageDtoConverter::convert)
                .collect(Collectors.toList());
    }

    @Override
    public ItemImageDto getImageById(Long imageId) {
        return imageRepository.findById(imageId)
                .map(imageDtoConverter::convert)
                .orElseThrow(() -> new EntityNotFoundException("image", imageId));
    }

    @Override
    @Transactional
    public ItemImageDto createImage(ItemImageCreateRequest request) {

        Item item = itemRepository.findById(request.getItemId())
                .orElseThrow(() -> new EntityNotFoundException("item", request.getItemId()));

        ItemImage image = ItemImage.builder()
                .url(request.getUrl())
                .build();

        item.addImage(image);

        try {
            itemRepository.save(item);
            return imageDtoConverter.convert(image);
        } catch (Exception e) {
            throw new InvalidEntityException("Error saving image: " + e.getMessage());
        }
    }

    @Override
    public ItemImageDto updateImage(ItemImageUpdateRequest request) {
        return null;
    }

    @Override
    public void deleteImage(Long id) {

    }
}
