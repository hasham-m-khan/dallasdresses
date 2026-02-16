package com.dallasdresses.controllers;

import com.dallasdresses.dtos.common.ApiResponse;
import com.dallasdresses.dtos.request.CredentialCreateRequest;
import com.dallasdresses.dtos.request.PasswordUpdateRequest;
import com.dallasdresses.dtos.response.CredentialDto;
import com.dallasdresses.entities.enums.Provider;
import com.dallasdresses.services.CredentialService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("${app.api.baseurl}/credentials")
public class CredentialController {

    private final CredentialService credentialService;

    /**
     * Get all credentials for a user
     * GET /api/v1/credentials/user/{userId}
     */
    @GetMapping("/user/{userId}")
    public ApiResponse<List<CredentialDto>> getUserCredentials(@PathVariable("userId") Long userId) {
        log.info("🔑 Fetching credentials for user id: {}", userId);

        List<CredentialDto> credentials = credentialService.getUserCredentials(userId);

        Map<String, Object> metadata = new HashMap<>();
        metadata.put("credentialCount", credentials.size());
        metadata.put("providers", credentials.stream()
                .map(CredentialDto::getProviderId)
                .distinct()
                .toList());

        return ApiResponse.<List<CredentialDto>>builder()
                .success(true)
                .data(credentials)
                .metadata(metadata)
                .message("credentials retrieved successfully")
                .build();
    }

    /**
     * Get credentials by provider key (email or OAuth ID)
     * GET /api/v1/credentials/provider-key/{providerKey}
     */
    @GetMapping("/provider-key/{providerKey}")
    public ApiResponse<CredentialDto> getCredentialsByProviderKey(@PathVariable("providerKey") String providerKey) {
        log.info("🔍 Fetching credentials for provider key: {}", providerKey);

        CredentialDto credentials = credentialService.getCredentialByProviderKey(providerKey);

        return ApiResponse.<CredentialDto>builder()
                .success(true)
                .data(credentials)
                .message("credentials retrieved successfully")
                .build();
    }

    /**
     * Create new credentials for a user
     * POST /api/v1/credentials
     */
    @PostMapping
    public ApiResponse<CredentialDto> createCredentials(@Valid @RequestBody CredentialCreateRequest request) {
        log.info("➕ Creating credentials - User ID: {}, Provider: {}", request.getUserId(), request.getProvider());

        CredentialDto credentials = credentialService.createCredentials(request);

        Map<String, Object> metadata = new HashMap<>();
        metadata.put("provider", credentials.getProviderId());
        metadata.put("credentialId", credentials.getId());

        return ApiResponse.<CredentialDto>builder()
                .success(true)
                .data(credentials)
                .metadata(metadata)
                .message("credentials created successfully")
                .build();
    }

    /**
     * Update user password (EMAIL provider only)
     * PUT /api/v1/credentials/password
     */
    @PutMapping("/password")
    public ApiResponse<Void> updatePassword(@Valid @RequestBody PasswordUpdateRequest request) {
        log.info("🔐 Updating password for user id: {}", request.getUserId());

        credentialService.updatePassword(request);

        return ApiResponse.<Void>builder()
                .success(true)
                .data(null)
                .message("password updated successfully")
                .build();
    }

    /**
     * Delete credentials by ID
     * DELETE /api/v1/credentials/{credentialsId}
     */
    @DeleteMapping("/{credentialsId}")
    public ApiResponse<Void> deleteCredentials(
            @PathVariable("credentialsId") Long credentialsId,
            @RequestParam("userId") Long userId) {
        log.info("🗑️ Deleting credentials id: {} for user id: {}", credentialsId, userId);

        credentialService.deleteCredential(credentialsId, userId);

        return ApiResponse.<Void>builder()
                .success(true)
                .data(null)
                .message("credentials deleted successfully")
                .build();
    }

    /**
     * Check if user has a specific provider
     * GET /api/v1/credentials/user/{userId}/has-provider/{provider}
     */
    @GetMapping("/user/{userId}/has-provider/{provider}")
    public ApiResponse<Boolean> hasProvider(
            @PathVariable("userId") Long userId,
            @PathVariable("provider") String provider) {
        log.info("🔎 Checking if user {} has provider {}", userId, provider);

        Provider providerEnum = Provider.fromValue(provider);
        boolean hasProvider = credentialService.hasProvider(userId, providerEnum);

        Map<String, Object> metadata = new HashMap<>();
        metadata.put("provider", providerEnum);
        metadata.put("hasProvider", hasProvider);

        return ApiResponse.<Boolean>builder()
                .success(true)
                .data(hasProvider)
                .metadata(metadata)
                .message("provider check completed successfully")
                .build();
    }

    /**
     * Verify password (for authentication)
     * POST /api/v1/credentials/verify-password
     */
    @PostMapping("/verify-password")
    public ApiResponse<Boolean> verifyPassword(@RequestBody Map<String, String> request) {
        String providerKey = request.get("providerKey");
        String password = request.get("password");

        log.info("🔓 Verifying password for provider key: {}", providerKey);

        boolean isValid = credentialService.verifyPassword(providerKey, password);

        Map<String, Object> metadata = new HashMap<>();
        metadata.put("valid", isValid);

        return ApiResponse.<Boolean>builder()
                .success(true)
                .data(isValid)
                .metadata(metadata)
                .message("password verification completed")
                .build();
    }
}
