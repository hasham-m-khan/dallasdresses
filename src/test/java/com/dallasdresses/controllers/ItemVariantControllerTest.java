package com.dallasdresses.controllers;

import com.dallasdresses.entities.Item;
import com.dallasdresses.entities.ItemVariant;
import com.dallasdresses.entities.enums.DressSize;
import com.dallasdresses.repositories.ItemRepository;
import com.dallasdresses.repositories.ItemVariantRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.HashSet;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class ItemVariantControllerTest {

    @Mock
    ItemVariantRepository variantRepository;

    @Mock
    ItemRepository itemRepository;

    @InjectMocks
    ItemVariantController itemVariantController;


    Item item1;
    ItemVariant itemVariant1;
    ItemVariant itemVariant2;

    @BeforeEach
    @DisplayName("ItemVariant Controller Tests")
    void setUp() {
        item1 = Item.builder()
                .id(1L)
                .name("Item1")
                .description("Description1")
                .variants(new HashSet<>())
                .build();

        itemVariant1 = ItemVariant.builder()
                .id(1L)
                .price(new BigDecimal("22.99"))
                .size(DressSize.MD)
                .stock(22)
                .build();

        itemVariant2 = ItemVariant.builder()
                .id(2L)
                .price(new BigDecimal("25.99"))
                .size(DressSize.XL)
                .stock(15)
                .build();

        item1.addVariant(itemVariant1);
        item1.addVariant(itemVariant2);
    }

    @Test
    @DisplayName("getItemVariant - Should return ItemVariants - When no error")
    void testGetItemVariants_ShouldReturnItemVariants_WhenNoError() {
        // Arrange
    }
}