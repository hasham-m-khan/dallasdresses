package com.dallasdresses.services;

import com.dallasdresses.converters.CredentialToCredentialDtoConverter;
import com.dallasdresses.dtos.request.CredentialCreateRequest;
import com.dallasdresses.dtos.request.PasswordUpdateRequest;
import com.dallasdresses.dtos.response.CredentialDto;
import com.dallasdresses.entities.Credential;
import com.dallasdresses.entities.User;
import com.dallasdresses.entities.enums.Hasher;
import com.dallasdresses.entities.enums.Provider;
import com.dallasdresses.exceptions.EntityNotFoundException;
import com.dallasdresses.exceptions.InvalidEntityException;
import com.dallasdresses.repositories.CredentialRepository;
import com.dallasdresses.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.SecureRandom;
import java.util.Base64;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CredentialServiceImpl implements CredentialService {

    private final CredentialRepository credentialRepository;
    private final UserRepository userRepository;
    private final CredentialToCredentialDtoConverter credConverter;
    private final PasswordEncoder passwordEncoder;

    private static final SecureRandom SECURE_RANDOM = new SecureRandom();

    @Override
    @Transactional
    public CredentialDto createCredentials(CredentialCreateRequest request) {
        // Validate user exists
        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new EntityNotFoundException("user", "id", String.valueOf(request.getUserId())));

        // Check if credentials already exist for this user and provider
        if (credentialRepository.existsByUserIdAndProviderId(request.getUserId(), request.getProvider())) {
            throw new InvalidEntityException(
                    String.format("credentials already exist for user %d with provider %s",
                            request.getUserId(), request.getProvider()));
        }

        // Check if provider key is already taken
        if (credentialRepository.existsByProviderKey(request.getProviderKey())) {
            throw new InvalidEntityException(
                    String.format("provider key '%s' is already in use", request.getProviderKey()));
        }

        // Build credentials entity
        Credential credentials = Credential.builder()
                .user(user)
                .providerId(request.getProvider())
                .providerKey(request.getProviderKey())
                .build();

        // For EMAIL provider, hash the password
        if (request.getProvider() == Provider.EMAIL) {
            if (request.getPassword() == null || request.getPassword().isBlank()) {
                throw new InvalidEntityException("password is required for email provider");
            }

            String salt = generateSalt();
            String passwordHash = passwordEncoder.encode(request.getPassword() + salt);

            credentials.setHasher(Hasher.BCRYPT);
            credentials.setPasswordHash(passwordHash);
            credentials.setPasswordSalt(salt);
        }

        // Save credentials
        Credential savedCredentials = credentialRepository.save(credentials);
        log.info("Created credentials for user {} with provider {}", request.getUserId(), request.getProvider());

        return credConverter.convert(savedCredentials);
    }

    @Override
    public List<CredentialDto> getUserCredentials(Long userId) {
        List<Credential> credentials = credentialRepository.findByUserId(userId);
        return credentials.stream()
                .map(credConverter::convert)
                .collect(Collectors.toList());
    }

    @Override
    public CredentialDto getCredentialByProviderKey(String providerKey) {
        Credential credentials = credentialRepository.findByProviderKey(providerKey)
                .orElseThrow(() -> new EntityNotFoundException("credentials", "provider key", providerKey));

        return credConverter.convert(credentials);
    }

    @Override
    @Transactional
    public void updatePassword(PasswordUpdateRequest request) {
        // Find credentials for user with EMAIL provider
        Credential credentials = credentialRepository.findByUserIdAndProviderId(
                        request.getUserId(), Provider.EMAIL)
                .orElseThrow(() -> new EntityNotFoundException(
                        "email credentials", "user id", String.valueOf(request.getUserId())));

        // Verify current password
        String saltedCurrentPassword = request.getCurrentPassword() + credentials.getPasswordSalt();
        if (!passwordEncoder.matches(saltedCurrentPassword, credentials.getPasswordHash())) {
            throw new InvalidEntityException("current password is incorrect");
        }

        // Generate new salt and hash new password
        String newSalt = generateSalt();
        String newPasswordHash = passwordEncoder.encode(request.getNewPassword() + newSalt);

        // Update credentials
        credentials.setPasswordHash(newPasswordHash);
        credentials.setPasswordSalt(newSalt);

        credentialRepository.save(credentials);
        log.info("Updated password for user {}", request.getUserId());
    }

    @Override
    @Transactional
    public void deleteCredential(Long credId, Long userId) {
        Credential credentials = credentialRepository.findById(credId)
                .orElseThrow(() -> new EntityNotFoundException("credentials", credId));

        // Verify ownership
        if (!credentials.getUser().getId().equals(userId)) {
            throw new InvalidEntityException("credentials do not belong to user");
        }

        // Check if this is the last credential - prevent user lockout
        List<Credential> userCredentials = credentialRepository.findByUserId(userId);
        if (userCredentials.size() == 1) {
            throw new InvalidEntityException("cannot delete last credential - user would be locked out");
        }

        credentialRepository.delete(credentials);
        log.info("Deleted credentials {} for user {}", credId, userId);
    }

    @Override
    public boolean verifyPassword(String providerKey, String password) {
        Credential credentials = credentialRepository.findByProviderKeyAndProviderId(
                        providerKey, Provider.EMAIL)
                .orElse(null);

        if (credentials == null) {
            return false;
        }

        String saltedPassword = password + credentials.getPasswordSalt();
        return passwordEncoder.matches(saltedPassword, credentials.getPasswordHash());
    }

    @Override
    public boolean hasProvider(Long userId, Provider provider) {
        return credentialRepository.existsByUserIdAndProviderId(userId, provider);
    }

    /**
     * Generate a random salt for password hashing
     */
    private String generateSalt() {
        byte[] salt = new byte[16];
        SECURE_RANDOM.nextBytes(salt);
        return Base64.getEncoder().encodeToString(salt);
    }
}
