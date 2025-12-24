package com.dallasdresses.dtos.request;

import com.dallasdresses.entities.enums.DressSize;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ItemVariantUpdateRequest {

    private DressSize size;
    private String color;
    private BigDecimal price;
    private Integer stock;
}
