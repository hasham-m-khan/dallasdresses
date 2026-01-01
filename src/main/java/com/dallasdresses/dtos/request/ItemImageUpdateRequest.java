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
public class ItemImageUpdateRequest {

    @NotNull
    private Long id;

    @NotNull
    private Long itemId;

    private String url;
    private String altText;
    private Integer displayOrder;
    private Boolean isPrimary;
}
