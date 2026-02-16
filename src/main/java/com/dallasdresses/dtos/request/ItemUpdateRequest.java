package com.dallasdresses.dtos.request;

import com.dallasdresses.entities.enums.DiscountType;
import com.dallasdresses.entities.enums.DressSize;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Set;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ItemUpdateRequest {

    private String name;
    private String description;
    private String color;
    private DressSize size;
    private Integer stock;
    private BigDecimal price;
    private DiscountType discountType;
    private Double discountValue;
    private Long parentId;
    private Boolean isParent;
    private Set<String> categorySlugs;
    private Set<ItemImageUpdateRequest> itemImages;
}
