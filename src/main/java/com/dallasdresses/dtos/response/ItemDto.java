package com.dallasdresses.dtos.response;

import com.dallasdresses.entities.enums.DiscountType;
import com.dallasdresses.entities.enums.DressSize;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.List;
import java.util.Set;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ItemDto {

    private Long id;
    private String name;
    private String description;
    private String color;
    private DressSize size;
    private Integer stock;
    private BigDecimal price;
    private DiscountType discountType;
    private Double discountValue;
    private Long parentId;
    private List<ItemDto> children;
    private Set<CategoryDto> categories;
    private Set<ItemImageDto> itemImages;
    private Timestamp createdAt;
    private Timestamp updatedAt;
}
