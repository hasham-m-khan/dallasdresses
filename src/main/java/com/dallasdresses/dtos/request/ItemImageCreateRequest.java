package com.dallasdresses.dtos.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ItemImageCreateRequest {

    @NotNull
    private Long itemId;

    @NotBlank
    @Pattern(regexp = "^(http|https)://.*$", message = "Image URL must be a valid HTTP/HTTPS URL")
    private String url;

    @Size(max = 500, message = "Alt text must not exceed 500 characters")
    private String altText;

    private Integer displayOrder;

    private Boolean isPrimary;
}
