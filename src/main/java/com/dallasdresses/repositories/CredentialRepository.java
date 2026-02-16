package com.dallasdresses.repositories;

import com.dallasdresses.entities.Credential;
import com.dallasdresses.entities.enums.Provider;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CredentialRepository extends JpaRepository<Credential, Long> {

    List<Credential> findByUserId(Long userId);
    Optional<Credential> findByUserIdAndProviderId(Long userId, Provider providerId);
    Optional<Credential> findByProviderKey(String providerKey);
    Optional<Credential> findByProviderKeyAndProviderId(String providerKey, Provider providerId);
    boolean existsByUserIdAndProviderId(Long userId, Provider providerId);
    boolean existsByProviderKey(String providerKey);
    void deleteByUserId(Long userId);
}
