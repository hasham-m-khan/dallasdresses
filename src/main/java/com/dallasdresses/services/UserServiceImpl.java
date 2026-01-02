package com.dallasdresses.services;

import com.dallasdresses.converters.UserToUserDtoConverter;
import com.dallasdresses.dtos.request.AddressCreateRequest;
import com.dallasdresses.dtos.request.AddressUpdateRequest;
import com.dallasdresses.dtos.request.UserCreateRequest;
import com.dallasdresses.dtos.request.UserUpdateRequest;
import com.dallasdresses.dtos.response.UserDto;
import com.dallasdresses.entities.Address;
import com.dallasdresses.entities.Country;
import com.dallasdresses.entities.User;
import com.dallasdresses.exceptions.DuplicateEntityException;
import com.dallasdresses.exceptions.EntityNotFoundException;
import com.dallasdresses.exceptions.InvalidEntityException;
import com.dallasdresses.repositories.CountryRepository;
import com.dallasdresses.repositories.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
@Transactional(readOnly = true)
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserToUserDtoConverter userDtoConverter;
    private final AddressService addressService;
    private final CountryRepository countryRepository;

    public UserServiceImpl(UserRepository userRepository,
                           UserToUserDtoConverter userDtoConverter,
                           AddressServiceImpl addressService,
                           CountryRepository countryRepository) {
        this.userRepository = userRepository;
        this.userDtoConverter = userDtoConverter;
        this.addressService = addressService;
        this.countryRepository = countryRepository;
    }

    @Override
    public List<UserDto> getAllUsers() {
        return userRepository.findAllWithAddresses().stream()
                .map(userDtoConverter::convert)
                .collect(Collectors.toList());
    }

    @Override
    public UserDto getUserById(Long id) {
        User user = userRepository.findByIdWithAddresses(id)
                .orElseThrow(() -> new EntityNotFoundException("user", id));

        return userDtoConverter.convert(user);
    }

    @Override
    public UserDto getUserByEmail(String email) {
        User user = userRepository.findByEmailWithAddresses(email)
                .orElseThrow(() -> new EntityNotFoundException("user", "email", email));

        return  userDtoConverter.convert(user);
    }

    @Override
    @Transactional
    public UserDto createUser(UserCreateRequest request) {

        String normalizedEmail = request.getEmail().toLowerCase().trim();

        // Check for duplicate email
        if (userRepository.findUserByEmail(normalizedEmail).isPresent()) {
            log.error("User with email '{}' already exists", normalizedEmail);

            throw new DuplicateEntityException("Email already registered");
        }

        // Create user
        User user = User.builder()
                .email(normalizedEmail)
                .role(request.getRole())
                .locale(request.getLocale())
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .telephone(request.getTelephone())
                .avatar(request.getAvatar())
                .emailVerified(false)
                .build();

        // Save user first to get id
        User savedUser = userRepository.save(user);

        // Create addresses
        if (request.getAddresses() != null && !request.getAddresses().isEmpty()) {
            request.getAddresses().forEach(addressRequest -> {

                Country country = Country.builder().name(addressRequest.getCountry()).build();
                Optional<Country> optCountry = countryRepository.findByNameIgnoreCase(addressRequest.getCountry());

                if (optCountry.isPresent()) {
                    country = optCountry.get();
                }

                AddressCreateRequest createRequest = AddressCreateRequest.builder()
                        .userId(savedUser.getId())
                        .addressType(addressRequest.getAddressType())
                        .addressLine1(addressRequest.getAddressLine1())
                        .addressLine2(addressRequest.getAddressLine2())
                        .city(addressRequest.getCity())
                        .country(country.getName())
                        .postalCode(addressRequest.getPostalCode())
                        .build();

                addressService.createAddress(createRequest);
            });
        }

        // Refresh user with updated addresses
        User userWithAddresses = userRepository.findByIdWithAddresses(savedUser.getId())
                .orElseThrow(() -> new EntityNotFoundException("user", savedUser.getId()));;

        return userDtoConverter.convert(userWithAddresses);
    }

    @Override
    @Transactional
    public UserDto updateUser(UserUpdateRequest request) {
        // Get existing user
        User existingUser = userRepository.findById(request.getId())
                .orElseThrow(() -> new EntityNotFoundException("user", request.getId()));

        // Check for duplicated email if it is being changed
        if (request.getEmail() != null && !existingUser.getEmail().equals(request.getEmail())) {
            userRepository.findUserByEmail(request.getEmail())
                    .ifPresent(u -> {
                        throw new DuplicateEntityException("user", "email", u.getEmail());
                    });
        }

        // Update user fields
        updateUserFields(existingUser, request);

        // Handle address updates
        if (request.getAddresses() != null && !request.getAddresses().isEmpty()) {
            handleAddressUpdates(existingUser, request.getAddresses());
        }

        User savedUser = userRepository.save(existingUser);
        User userWithAddresses = userRepository.findByIdWithAddresses(savedUser.getId())
                .orElseThrow(() -> new EntityNotFoundException("user", savedUser.getId()));

        return userDtoConverter.convert(userRepository.save(userWithAddresses));
    }

    @Override
    @Transactional
    public void deleteUser(Long id) {
        if (!userRepository.existsById(id)) {
            throw new EntityNotFoundException("user", id);
        }

        userRepository.deleteById(id);
    }

    public void updateUserFields(User user, UserUpdateRequest request) {
        if (request.getEmail() != null) {
            user.setEmail(request.getEmail());
        }

        if (request.getRole() != null) {
            user.setRole(request.getRole());
        }

        if (request.getLocale() != null) {
            user.setLocale(request.getLocale());
        }

        if (request.getFirstName() != null) {
            user.setFirstName(request.getFirstName());
        }

        if (request.getLastName() != null) {
            user.setLastName(request.getLastName());
        }

        if (request.getTelephone() != null) {
            user.setTelephone(request.getTelephone());
        }

        if (request.getAvatar() != null) {
            user.setAvatar(request.getAvatar());
        }
    }

    private void handleAddressUpdates(User user, List<AddressUpdateRequest> addressRequests) {
        // Get IDs of addresses in the request
        Set<Long> requestAddressIds = addressRequests.stream()
                .map(AddressUpdateRequest::getId)
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());

        // Remove addresses not in the request
        user.getAddresses().removeIf(addr -> !requestAddressIds.contains(addr.getId()));

        // Map existing addresses by ID
        Map<Long, Boolean> existingAddressIds = user.getAddresses().stream()
                .collect(Collectors.toMap(
                        addr -> addr.getId(),
                        addr -> true
                ));

        // Process each address request
        for (AddressUpdateRequest addressRequest : addressRequests) {
            addressRequest.setUserId(user.getId()); // Ensure userId is set

            if (addressRequest.getId() != null && existingAddressIds.containsKey(addressRequest.getId())) {
                // Update existing address
                addressService.updateAddress(addressRequest);
            } else {
                // Create new address
                AddressCreateRequest createRequest = AddressCreateRequest.builder()
                        .userId(user.getId())
                        .addressType(addressRequest.getAddressType())
                        .addressLine1(addressRequest.getAddressLine1())
                        .addressLine2(addressRequest.getAddressLine2())
                        .city(addressRequest.getCity())
                        .state(addressRequest.getState())
                        .country(addressRequest.getCountry())
                        .postalCode(addressRequest.getPostalCode())
                        .build();

                addressService.createAddress(createRequest);
            }
        }
    }
}
