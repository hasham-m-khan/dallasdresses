package com.dallasdresses.dtos.response;

import com.dallasdresses.entities.enums.DiscountType;
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
public class ItemDto {

    private Long id;
    private String name;
    private String description;
    private String summary;
    private BigDecimal price;
    private DiscountType discountType;
    private Double discountValue;
    private Set<CategoryDto> categories;
    private Set<ItemImageDto> itemImages;
}
