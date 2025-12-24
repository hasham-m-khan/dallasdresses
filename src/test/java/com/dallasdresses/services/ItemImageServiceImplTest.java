package com.dallasdresses.services;

import com.dallasdresses.converters.ItemImageToItemImageDtoConverter;
import com.dallasdresses.dtos.response.ItemImageDto;
import com.dallasdresses.entities.Item;
import com.dallasdresses.entities.ItemImage;
import com.dallasdresses.exceptions.EntityNotFoundException;
import com.dallasdresses.repositories.ItemImageRepository;
import com.dallasdresses.repositories.ItemRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("ItemImage tests")
class ItemImageServiceImplTest {

    @Mock
    ItemImageRepository imageRepository;

    @Mock
    ItemRepository itemRepository;

    @Mock
    ItemImageToItemImageDtoConverter itemImageDtoConverter;

    @InjectMocks
    ItemImageServiceImpl imageService;

    Item item;
    ItemImage image1;
    ItemImage image2;
    ItemImageDto image1Dto;
    ItemImageDto image2Dto;

    @BeforeEach
    void setUp() {

        item = Item.builder()
                .id(5L)
                .name("BIC Ball point pens - 8 count")
                .price(new BigDecimal("8.99"))
                .build();

        image1 = ItemImage.builder()
                .id(1L)
                .url("http://www.imagesite.com/123czased23skl8")
                .build();

        image2 = ItemImage.builder()
                .id(2L)
                .url("http://www.imagesite.com/sdalk132KMaksladkAS")
                .build();

        image1Dto = ItemImageDto.builder()
                .id(1L)
                .url("http://www.imagesite.com/123czased23skl8")
                .build();

        image2Dto = ItemImageDto.builder()
                .id(2L)
                .url("http://www.imagesite.com/sdalk132KMaksladkAS")
                .build();
    }

    @Test
    @DisplayName("getAllImages - Should return all images")
    void testGetAllImages_ShouldReturnAllImages_WhenNoError() {
        // Arrange
        when(imageRepository.findAll()).thenReturn(List.of(image1, image2));
        when(itemImageDtoConverter.convert(image1)).thenReturn(image1Dto);
        when(itemImageDtoConverter.convert(image2)).thenReturn(image2Dto);

        // Act
        List<ItemImageDto> result = imageService.getAllImages();

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());

        verify(imageRepository, times(1)).findAll();
        verify(itemImageDtoConverter, times(2)).convert(any(ItemImage.class));
    }

    @Test
    @DisplayName("getImagesByItemId - Should return all images")
    void testGetImagesByItemId_ShouldReturnImages_WhenNoError() {
        // Arrange
        when(imageRepository.findByItemId(anyLong())).thenReturn(List.of(image1, image2));
        when(itemImageDtoConverter.convert(image1)).thenReturn(image1Dto);
        when(itemImageDtoConverter.convert(image2)).thenReturn(image2Dto);

        // Act
        List<ItemImageDto> result = imageService.getImagesByItemId(1L);

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());

        verify(imageRepository, times(1)).findByItemId(anyLong());
        verify(itemImageDtoConverter, times(2)).convert(any(ItemImage.class));
    }

    @Test
    @DisplayName("getImageById - Should return image")
    void testGetImageById_ShouldReturnImage_WhenNoError() {
        // Arrange
        when(imageRepository.findById(anyLong())).thenReturn(Optional.of(image1));
        when(itemImageDtoConverter.convert(image1)).thenReturn(image1Dto);

        // Act
        ItemImageDto result = imageService.getImageById(1L);

        // Assert
        assertNotNull(result);
        assertEquals(image1Dto, result);

        verify(imageRepository, times(1)).findById(1L);
        verify(itemImageDtoConverter, times(1)).convert(any(ItemImage.class));
    }

    @Test
    @DisplayName("getImageById - Should throw EntityNotFoundException")
    void testGetImageById_ShouldThrowEntityNotFoundException_WhenImageNotFound() {
        // Arrange
        when(imageRepository.findById(anyLong())).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(EntityNotFoundException.class, () -> imageService.getImageById(1L));

        verify(imageRepository, times(1)).findById(1L);
        verify(itemImageDtoConverter, never()).convert(any(ItemImage.class));
    }

    @Test
    @DisplayName("createImage - Should create image")
    void testCreateImage_ShouldCreateImage_WhenNoError() {
        // Arrange
        when(itemRepository.findById(item.getId())).thenReturn(Optional.of(item));
    }

}
