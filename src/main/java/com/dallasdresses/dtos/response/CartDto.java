package com.dallasdresses.dtos.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Set;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CartDto {

    private Long id;
    private Long userId;
    private Set<CartItemDto> items;
    private Integer totalItems;
    private BigDecimal subtotal;
    private BigDecimal tax;
    private BigDecimal total;

    private Timestamp createdAt;
    private Timestamp updatedAt;
}
