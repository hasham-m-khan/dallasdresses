package com.dallasdresses.dtos.request;

import com.dallasdresses.entities.enums.DiscountType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
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
    private String summary;
    private BigDecimal price;
    private DiscountType discountType;
    private Double discountValue;
    private Set<CategoryUpdateRequest> categories;
    private Set<ItemImageUpdateRequest> itemImages;
}
