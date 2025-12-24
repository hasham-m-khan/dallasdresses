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
public class ItemCreateRequest {

    @NotBlank
    private String name;

    @NotBlank
    private String description;

    @NotBlank
    private String summary;

    @NotNull
    private BigDecimal price;

    @NotNull
    private DiscountType discountType;

    @NotNull
    private Double discountAmount;

    private Set<CategoryCreateRequest> categories;
    private Set<ItemImageCreateRequest> itemImages;
}
