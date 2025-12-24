package com.dallasdresses.dtos.response;

import com.dallasdresses.entities.enums.DressSize;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ItemVariantDto {

    private Long id;
    private DressSize size;
    private String color;
    private BigDecimal price;
    private Integer stock;

    private Long itemId;
    private String itemName;
    private String itemUrl;
}
