package com.dallasdresses.dtos.request;

import com.dallasdresses.entities.enums.Provider;
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
public class CredentialCreateRequest {

    @NotNull(message = "user id is required")
    private Long userId;

    @NotNull(message = "provider is required")
    private Provider provider;

    @NotNull(message = "provider key is required")
    private String providerKey;

    @Size(min = 8, max = 16, message = "password must be between 8 and 16 characters")
    private String password;
}
