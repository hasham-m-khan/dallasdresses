package com.dallasdresses.services;

import com.dallasdresses.converters.UserToUserDtoConverter;
import com.dallasdresses.dtos.request.AddressUpdateRequest;
import com.dallasdresses.dtos.request.UserCreateRequest;
import com.dallasdresses.dtos.request.UserUpdateRequest;
import com.dallasdresses.dtos.response.UserDto;
import com.dallasdresses.entities.Address;
import com.dallasdresses.entities.User;
import com.dallasdresses.exceptions.DuplicateEntityException;
import com.dallasdresses.exceptions.EntityNotFoundException;
import com.dallasdresses.exceptions.InvalidEntityException;
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

    public UserServiceImpl(UserRepository userRepository,
                           UserToUserDtoConverter userDtoConverter) {
        this.userRepository = userRepository;
        this.userDtoConverter = userDtoConverter;
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
    public UserDto updateUser(UserUpdateRequest request) {
        User existingUser = userRepository.findById(request.getId())
                .orElseThrow(() -> new EntityNotFoundException("user", request.getId()));

        if (!existingUser.getEmail().equals(request.getEmail())) {
            userRepository.findUserByEmail(request.getEmail())
                    .ifPresent(u -> {
                        throw new DuplicateEntityException("user", "email", u.getEmail());
                    });
        }

        try {
            if (request.getEmail() != null) {
                existingUser.setEmail(request.getEmail());
            }

            if (request.getRole() != null) {
                existingUser.setRole(request.getRole());
            }
            if (request.getLocale() != null) {
                existingUser.setLocale(request.getLocale());
            }
            if (request.getFirstName() != null) {
                existingUser.setFirstName(request.getFirstName());
            }
            if (request.getLastName() != null) {
                existingUser.setLastName(request.getLastName());
            }
            if (request.getTelephone() != null) {
                existingUser.setTelephone(request.getTelephone());
            }
            if (request.getAvatar() != null) {
                existingUser.setAvatar(request.getAvatar());
            }

            if (request.getAddresses() != null && !request.getAddresses().isEmpty()) {

                Set<Address> existingAddresses = existingUser.getAddresses();

                Map<Long, Address> existingAddressMap = existingAddresses.stream()
                                .collect(Collectors.toMap(Address::getId, addr -> addr));

                Set<Long> requestAddressIds = request.getAddresses().stream()
                    .map(AddressUpdateRequest::getId)
                    .filter(Objects::nonNull)
                    .collect(Collectors.toSet());

                 existingAddresses.removeIf(addr -> !requestAddressIds.contains(addr.getId()));

                for (AddressUpdateRequest addressRequest : request.getAddresses()) {
                    if (addressRequest.getId() != null && existingAddressMap.containsKey(addressRequest.getId())) {
                        // Update existing address
                        Address existingAddress = existingAddressMap.get(addressRequest.getId());
                        existingAddress.setAddressType(addressRequest.getAddressType());
                        existingAddress.setAddressLine1(addressRequest.getAddressLine1());
                        existingAddress.setAddressLine2(addressRequest.getAddressLine2());
                        existingAddress.setCity(addressRequest.getCity());
                        existingAddress.setState(addressRequest.getState());
                        existingAddress.setCountry(addressRequest.getCountry());
                        existingAddress.setPostalCode(addressRequest.getPostalCode());
                    } else {
                        // Add new address
                        Address newAddress = Address.builder()
                                .addressType(addressRequest.getAddressType())
                                .addressLine1(addressRequest.getAddressLine1())
                                .addressLine2(addressRequest.getAddressLine2())
                                .city(addressRequest.getCity())
                                .state(addressRequest.getState())
                                .country(addressRequest.getCountry())
                                .postalCode(addressRequest.getPostalCode())
                                .user(existingUser)
                                .build();
                        existingUser.addAddress(newAddress);
                    }
                }
            }

            return userDtoConverter.convert(userRepository.save(existingUser));
        } catch (Exception ex) {
            throw new InvalidEntityException("Failed to update user with id " + request.getId());
        }
    }

    @Override
    @Transactional
    public UserDto createUser(UserCreateRequest request) {

        String normalizedEmail = request.getEmail().toLowerCase().trim();

        if (userRepository.findUserByEmail(normalizedEmail).isPresent()) {
            log.error("User with email '{}' already exists", normalizedEmail);

            throw new DuplicateEntityException("Email already registered");
        }

        try {
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

            if (request.getAddresses() != null && !request.getAddresses().isEmpty()) {
                request.getAddresses().forEach(addressRequest -> {
                    Address address = Address.builder()
                            .addressType(addressRequest.getAddressType())
                            .addressLine1(addressRequest.getAddressLine1())
                            .addressLine2(addressRequest.getAddressLine2())
                            .city(addressRequest.getCity())
                            .state(addressRequest.getState())
                            .country(addressRequest.getCountry())
                            .postalCode(addressRequest.getPostalCode())
                            .build();

                    user.addAddress(address);
                });
            }

            User savedUser = userRepository.save(user);

            return userDtoConverter.convert(savedUser);
        } catch (Exception e) {
            throw new InvalidEntityException("User creation failed");
        }
    }

    @Override
    @Transactional
    public void deleteUser(Long id) {
        if (!userRepository.existsById(id)) {
            throw new EntityNotFoundException("user", id);
        }

        userRepository.deleteById(id);
    }
}
