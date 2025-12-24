package com.dallasdresses.dtos.request;

import com.dallasdresses.entities.enums.DressSize;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ItemVariantCreateRequest {

    private DressSize size;
    private String color;

    @NotNull
    private BigDecimal price;

    @NotNull
    private Integer stock;
}
