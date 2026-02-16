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
public class CartItemDto {

    private Long id;
    private Long itemId;
    private String itemName;
    private String itemColor;
    private DressSize itemSize;
    private String primaryImageUrl;
    private Integer quantity;
    private BigDecimal priceAtAdd;
    private BigDecimal currentPrice;
    private BigDecimal subtotal;

    private Integer availableStock;
    private Boolean inStock;

    private Boolean priceChanged;
}
