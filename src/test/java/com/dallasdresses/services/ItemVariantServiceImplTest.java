package com.dallasdresses.services;

import com.dallasdresses.converters.ItemVariantToItemVariantDtoConverter;
import com.dallasdresses.dtos.request.ItemVariantCreateRequest;
import com.dallasdresses.dtos.request.ItemVariantUpdateRequest;
import com.dallasdresses.dtos.response.ItemVariantDto;
import com.dallasdresses.entities.Item;
import com.dallasdresses.entities.ItemVariant;
import com.dallasdresses.entities.enums.DiscountType;
import com.dallasdresses.entities.enums.DressSize;
import com.dallasdresses.exceptions.DuplicateEntityException;
import com.dallasdresses.exceptions.EntityNotFoundException;
import com.dallasdresses.repositories.ItemRepository;
import com.dallasdresses.repositories.ItemVariantRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("Item Variant Tests")
class ItemVariantServiceImplTest {

    @Mock
    private ItemVariantRepository variantRepository;

    @Mock
    private ItemRepository itemRepository;

    @Mock
    private ItemVariantToItemVariantDtoConverter variantDtoConverter;

    @InjectMocks
    private ItemVariantServiceImpl variantService;

    Item item1;
    Item item2;

    ItemVariant variant1;
    ItemVariant variant2;
    ItemVariant variant3;

    ItemVariantDto variantDto1;
    ItemVariantDto variantDto2;
    ItemVariantDto variantDto3;

    @BeforeEach
    void setUp() {
        item1 = Item.builder()
                .id(10L)
                .name("item1")
                .description("description1")
                .summary("summary1")
                .price(BigDecimal.valueOf(10.00))
                .discountType(DiscountType.NONE)
                .discountAmount(null)
                .build();

        item2 = Item.builder()
                .id(20L)
                .name("item2")
                .description("description2")
                .summary("summary2")
                .price(BigDecimal.valueOf(100.00))
                .discountType(DiscountType.NONE)
                .discountAmount(null)
                .build();

        variant1 = ItemVariant.builder()
                .id(1L)
                .color("blue")
                .size(DressSize.MD)
                .price(new BigDecimal("22.99"))
                .stock(25)
                .item(item1)
                .build();

        variant2 = ItemVariant.builder()
                .id(2L)
                .color("green")
                .size(DressSize.XL)
                .price(new BigDecimal("124.99"))
                .stock(19)
                .item(item1)
                .build();

        variant3 = ItemVariant.builder()
                .id(3L)
                .color("teal")
                .size(DressSize.SM)
                .price(new BigDecimal("67.99"))
                .stock(30)
                .item(item2)
                .build();

        variantDto1 = ItemVariantDto.builder()
                .id(1L)
                .color("blue")
                .size(DressSize.MD)
                .price(new BigDecimal("22.99"))
                .stock(25)
                .itemId(item1.getId())
                .itemName(item1.getName())
                .itemUrl("someUrl")
                .build();

        variantDto2 = ItemVariantDto.builder()
                .id(2L)
                .color("green")
                .size(DressSize.XL)
                .price(new BigDecimal("124.99"))
                .stock(19)
                .itemId(item1.getId())
                .itemName(item1.getName())
                .itemUrl("someUrl")
                .build();

        variantDto3 = ItemVariantDto.builder()
                .id(3L)
                .color("teal")
                .size(DressSize.SM)
                .price(new BigDecimal("67.99"))
                .stock(30)
                .itemId(item2.getId())
                .itemName(item2.getName())
                .itemUrl("someUrl")
                .build();
    }

    @Test
    @DisplayName("getAllVariants - Should return all variants - When no error")
    void testGetAllVariants_ShouldReturnAllVariants_WhenNoError() {
        // Arrange
        List<ItemVariant> variants = Arrays.asList(variant1, variant2, variant3);

        Pageable pageable = PageRequest.of(0, 10);
        Page<ItemVariant> variantPage = new PageImpl<>(variants, pageable, variants.size());

        when(variantRepository.findAllWithItem(pageable)).thenReturn(variantPage);;
        when(variantDtoConverter.convert(variant1)).thenReturn(variantDto1);
        when(variantDtoConverter.convert(variant2)).thenReturn(variantDto2);
        when(variantDtoConverter.convert(variant3)).thenReturn(variantDto3);

        // Act
        Page<ItemVariantDto> result = variantService.getAllVariants(pageable);

        // Assert
        assertEquals(result.getTotalElements(), variants.size());

        verify(variantRepository, times(1)).findAllWithItem(pageable);
        verify(variantDtoConverter, times(3)).convert(any(ItemVariant.class));
    }

    @Test
    @DisplayName("getVariantById - Should return variant - When found")
    void testGetVariantById_ShouldReturnVariant() {
        // Arrange
        when(variantRepository.findByIdWithItem(variant1.getId())).thenReturn(Optional.of(variant1));
        when(variantDtoConverter.convert(variant1)).thenReturn(variantDto1);

        // Act
        ItemVariantDto result = variantService.getItemVariantById(variant1.getId());

        // Assert
        assertEquals(variantDto1, result);

        verify(variantRepository, times(1)).findByIdWithItem(variant1.getId());
        verify(variantDtoConverter, times(1)).convert(variant1);
    }

    @Test
    @DisplayName("getVariantById - Should throw EntityNotFoundException - When variant not found")
    void testGetVariantById_ShouldThrowEntityNotFoundException_WhenVariantNotFound() {
        // Arrange
        when(variantRepository.findByIdWithItem(variant1.getId())).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(EntityNotFoundException.class, () -> variantService.getItemVariantById(variant1.getId()));

        verify(variantRepository, times(1)).findByIdWithItem(variant1.getId());
        verify(variantDtoConverter, never()).convert(any(ItemVariant.class));
    }

    @Test
    @DisplayName("createItemVariant - Should create ItemVariant - When no errors")
    void testCreateItemVariant_ShouldCreateItemVariant_WhenNoErrors() {
        // Arrange
        ItemVariantCreateRequest req = new ItemVariantCreateRequest();
        req.setColor(variant1.getColor());
        req.setSize(variant1.getSize());
        req.setPrice(variant1.getPrice());
        req.setStock(variant1.getStock());
        req.setItemId(variant1.getItem().getId());

        ItemVariant variantToSave = variant1;
        variantToSave.setId(null);

        when(variantRepository.existsByItemIdAndColorAndSize(
                req.getItemId(), req.getColor(), req.getSize())).thenReturn(false);
        when(itemRepository.findById(req.getItemId())).thenReturn(Optional.of(item1));
        when(variantRepository.save(any(ItemVariant.class))).thenReturn(variant1);
        when(variantDtoConverter.convert(variant1)).thenReturn(variantDto1);

        // Act
        ItemVariantDto result = variantService.createItemVariant(req);

        // Assert
        assertEquals(variantDto1, result);
        verify(variantRepository, times(1)).existsByItemIdAndColorAndSize(
                req.getItemId(), req.getColor(), req.getSize());
        verify(itemRepository, times(1)).findById(req.getItemId());
        verify(variantRepository, times(1)).save(any(ItemVariant.class));
        verify(variantDtoConverter, times(1)).convert(variant1);
    }

    @Test
    @DisplayName("createItemVariant - Should throw DuplicateEntityException - When variant exists")
    void testCreateItemVariant_ShouldThrowDuplicateEntityException_WhenVariantExists() {
        // Arrange
        ItemVariantCreateRequest req = new ItemVariantCreateRequest();
        req.setColor(variant1.getColor());
        req.setSize(variant1.getSize());
        req.setPrice(variant1.getPrice());
        req.setStock(variant1.getStock());
        req.setItemId(variant1.getItem().getId());

        when(variantRepository.existsByItemIdAndColorAndSize(
                req.getItemId(), req.getColor(), req.getSize())).thenReturn(true);

        // Act & Assert
        assertThrows(DuplicateEntityException.class, () -> variantService.createItemVariant(req));

        verify(variantRepository, times(1)).existsByItemIdAndColorAndSize(
                req.getItemId(), req.getColor(), req.getSize());
        verify(itemRepository, never()).findById(req.getItemId());
        verify(variantRepository, never()).save(any(ItemVariant.class));
        verify(variantDtoConverter, never()).convert(any(ItemVariant.class));
    }

    @Test
    @DisplayName("createItemVariant - Should throw EntityNotFoundException - When item not found")
    void testCreateItemVariant_ShouldThrowEntityNotFoundException_WhenItemNotFound() {
        // Arrange
        ItemVariantCreateRequest req = new ItemVariantCreateRequest();
        req.setColor(variant1.getColor());
        req.setSize(variant1.getSize());
        req.setPrice(variant1.getPrice());
        req.setStock(variant1.getStock());
        req.setItemId(variant1.getItem().getId());

        when(variantRepository.existsByItemIdAndColorAndSize(
                req.getItemId(), req.getColor(), req.getSize())).thenReturn(false);
        when(itemRepository.findById(req.getItemId())).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(EntityNotFoundException.class, () -> variantService.createItemVariant(req));

        verify(variantRepository, times(1)).existsByItemIdAndColorAndSize(
                req.getItemId(), req.getColor(), req.getSize());
        verify(itemRepository, times(1)).findById(req.getItemId());
        verify(variantRepository, never()).save(any(ItemVariant.class));
        verify(variantDtoConverter, never()).convert(any(ItemVariant.class));
    }

    @Test
    @DisplayName("updateItemVariant - Should update ItemVariant - When no errors")
    void testUpdateItemVariant_ShouldUpdateItemVariant_WhenNoErrors() {
        // Arrange
        String colorToUpdate = "magenta";

        ItemVariantUpdateRequest req = new ItemVariantUpdateRequest();
        req.setColor(colorToUpdate);
        req.setSize(variant1.getSize());
        req.setPrice(variant1.getPrice());
        req.setStock(variant1.getStock());

        ItemVariant updatedVariant = variant1;
        updatedVariant.setColor(colorToUpdate);

        ItemVariantDto updatedVariantDto = variantDto1;
        updatedVariantDto.setColor(colorToUpdate);

        when(variantRepository.findById(variant1.getId())).thenReturn(Optional.of(variant1));
        when(variantRepository.save(any(ItemVariant.class))).thenReturn(updatedVariant);
        when(variantDtoConverter.convert(any(ItemVariant.class))).thenReturn(updatedVariantDto);

        // Act
        ItemVariantDto result = variantService.updateItemVariant(variant1.getId(), req);

        // Arrange
        assertEquals(updatedVariantDto, result);

        verify(variantRepository, times(1)).findById(variant1.getId());
        verify(variantRepository, never()).existsByItemIdAndColorAndSizeAndIdNot(any(), any(), any(), any());
        verify(variantRepository, times(1)).save(any(ItemVariant.class));
        verify(variantDtoConverter, times(1)).convert(any(ItemVariant.class));
    }

    @Test
    @DisplayName("updateItemVariant - Should throw EntityNotFoundException - When ItemVariant errors")
    void testUpdateItemVariant_ShouldThrowEntityNotFoundException_WhenItemVariantNotFound() {
        // Arrange
        ItemVariantUpdateRequest req = new ItemVariantUpdateRequest();
        req.setColor(variant1.getColor());
        req.setSize(variant1.getSize());
        req.setPrice(variant1.getPrice());
        req.setStock(variant1.getStock());

        when(variantRepository.findById(variant1.getId())).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(EntityNotFoundException.class, () ->  variantService.updateItemVariant(variant1.getId(), req));

        verify(variantRepository, times(1)).findById(variant1.getId());
        verify(variantRepository, never()).existsByItemIdAndColorAndSizeAndIdNot(any(), any(), any(), any());
        verify(variantRepository, never()).save(any(ItemVariant.class));
        verify(variantDtoConverter, never()).convert(any(ItemVariant.class));
    }

    @Test
    @DisplayName("updateItemVariant - Should throw DuplicateEntityException - When duplicate exists")
    void testUpdateItemVariant_ShouldThrowDuplicateEntityException_WhenDuplicateExists() {
        // Arrange
        ItemVariantUpdateRequest req = new ItemVariantUpdateRequest();
        req.setColor("magenta");
        req.setSize(variant1.getSize());
        req.setPrice(variant1.getPrice());
        req.setStock(variant1.getStock());

        when(variantRepository.findById(variant1.getId())).thenReturn(Optional.of(variant1));
        when(variantRepository.existsByItemIdAndColorAndSizeAndIdNot(
                any(), any(), any(), any()
        )).thenReturn(Boolean.TRUE);

        // Act & Assert
        assertThrows(DuplicateEntityException.class, () ->  variantService.updateItemVariant(variant1.getId(), req));

        verify(variantRepository, times(1)).findById(variant1.getId());
        verify(variantRepository, times(1)).existsByItemIdAndColorAndSizeAndIdNot(any(), any(), any(), any());
        verify(variantRepository, never()).save(any(ItemVariant.class));
        verify(variantDtoConverter, never()).convert(any(ItemVariant.class));
    }

    @Test
    @DisplayName("deleteItemVariant - Should delete ItemVariant")
    void testDeleteItemVariant_ShouldDeleteItemVariant() {
        // Arrange
        when(variantRepository.findById(variant1.getId())).thenReturn(Optional.of(variant1));

        // Act
        variantService.deleteItemVariant(variant1.getId());

        // Assert
        verify(variantRepository, times(1)).findById(variant1.getId());
        verify(variantRepository, times(1)).delete(variant1);
    }

    @Test
    @DisplayName("deleteItemVariant - Should throw EntityNotFoundException - When ItemVariant not found")
    void testDeleteItemVariant_ShouldThrowEntityNotFoundException_WhenItemVariantNotFound() {
        // Arrange
        when(variantRepository.findById(variant1.getId())).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(EntityNotFoundException.class, () -> variantService.deleteItemVariant(variant1.getId()));

        verify(variantRepository, times(1)).findById(variant1.getId());
        verify(variantRepository, never()).delete(variant1);
    }
}