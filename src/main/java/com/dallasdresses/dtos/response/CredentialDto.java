package com.dallasdresses.dtos.response;

import com.dallasdresses.entities.enums.Provider;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CredentialDto {

    private Long id;
    private Long userId;
    private Provider providerId;
    private String providerKey;

    // Note: We never expose password hash, salt, or hasher method in responses
}
