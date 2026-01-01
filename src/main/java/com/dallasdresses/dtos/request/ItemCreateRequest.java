package com.dallasdresses.dtos.request;

import com.dallasdresses.dtos.response.ItemDto;
import com.dallasdresses.dtos.response.ItemImageDto;
import com.dallasdresses.entities.enums.DiscountType;
import com.dallasdresses.entities.enums.DressSize;
import jakarta.validation.constraints.*;
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
    @Size(max = 255, message = "Item name must not exceed 255 characters")
    private String name;

    @NotBlank
    @Size(max = 5000, message = "Description must not exceed 5000 characters")
    private String description;

    @NotBlank
    private String color;

    @NotNull
    private DressSize size;

    @NotNull
    @Min(value = 0, message = "Stock cannot be negative")
    private Integer stock;

    @NotNull
    @Min(value = 0, message = "Price cannot be negative")
    private BigDecimal price;

    private DiscountType discountType;

    @Min(value = 0, message = "Discount value cannot be negative")
    private Double discountValue;

    private Long parentId;

    @NotEmpty(message = "At least one category is required")
    private Set<String> categorySlugs;

    @NotEmpty
    private Set<ItemImageCreateRequest> itemImages;

    @AssertTrue(message = "Invalid discount: percentage must be 0-100, fixed amount must not exceed price")
    public boolean isValidDiscount() {
        if (discountType == null) {
            return discountValue == null || discountValue == 0.0;
        }

        if (discountValue == null || discountValue < 0) {
            return false;
        }

        if (discountType == DiscountType.PERCENTAGE) {
            return discountValue >= 0 && discountValue <= 100;
        }

        if (discountType == DiscountType.FIXED && price != null) {
            return discountValue <= price.doubleValue();
        }

        return true;
    }
}
