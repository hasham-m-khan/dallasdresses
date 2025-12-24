package com.dallasdresses.dtos.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ItemImageUpdateRequest {

    @NotNull
    private Long id;

    @NotNull
    private Long itemId;

    @NotBlank
    private String url;
}
