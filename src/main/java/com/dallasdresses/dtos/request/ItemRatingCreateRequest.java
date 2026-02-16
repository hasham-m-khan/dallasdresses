package com.dallasdresses.dtos.request;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ItemRatingCreateRequest {

    @NotNull
    private Long itemId;

    @NotNull
    private Long userId;

    @NotNull
    @Min(value = 1)
    @Max(value = 5)
    private Integer rating;

    @Size(max = 100)
    private String title;

    @Size(max = 2000)
    private String reviewText;
}
