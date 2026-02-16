package com.dallasdresses.dtos.request;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CartItemRemoveRequest {

    @NotNull(message = "cart item id is required")
    private Long cartItemId;

    @NotNull(message = "user id is required")
    private Long userId;
}
