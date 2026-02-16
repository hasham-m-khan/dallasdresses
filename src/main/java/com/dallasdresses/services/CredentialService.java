package com.dallasdresses.services;

import com.dallasdresses.dtos.request.CredentialCreateRequest;
import com.dallasdresses.dtos.request.PasswordUpdateRequest;
import com.dallasdresses.dtos.response.CredentialDto;
import com.dallasdresses.entities.enums.Provider;

import java.util.List;

public interface CredentialService {

    CredentialDto createCredentials(CredentialCreateRequest request);
    List<CredentialDto> getUserCredentials(Long userId);
    CredentialDto getCredentialByProviderKey(String providerKey);
    void updatePassword(PasswordUpdateRequest request);
    void deleteCredential(Long credId, Long userId);
    boolean verifyPassword(String providerKey, String password);
    boolean hasProvider(Long userId, Provider provider);

}
