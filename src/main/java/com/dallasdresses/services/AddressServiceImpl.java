package com.dallasdresses.services;

import com.dallasdresses.converters.AddressToAddressDtoConverter;
import com.dallasdresses.dtos.response.AddressDto;
import com.dallasdresses.entities.Address;
import com.dallasdresses.entities.Country;
import com.dallasdresses.entities.User;
import com.dallasdresses.exceptions.DuplicateEntityException;
import com.dallasdresses.exceptions.EntityNotFoundException;
import com.dallasdresses.exceptions.EntityUpdateException;
import com.dallasdresses.dtos.request.AddressCreateRequest;
import com.dallasdresses.dtos.request.AddressUpdateRequest;
import com.dallasdresses.repositories.AddressRepository;
import com.dallasdresses.repositories.CountryRepository;
import com.dallasdresses.repositories.UserRepository;
import lombok.extern.slf4j.Slf4j;

import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
public class AddressServiceImpl implements AddressService {

    private final AddressRepository addressRepository;
    private final UserRepository userRepository;
    private final AddressToAddressDtoConverter addressDtoConverter;
    private final CountryRepository countryRepository;

    public AddressServiceImpl(AddressRepository addressRepository,
                              UserRepository userRepository,
                              AddressToAddressDtoConverter addressDtoConverter,
                              CountryRepository countryRepository) {
        this.addressRepository = addressRepository;
        this.userRepository = userRepository;
        this.addressDtoConverter = addressDtoConverter;
        this.countryRepository = countryRepository;
    }

    public List<AddressDto> getAllAddresses() {
        return addressRepository.findAll().stream()
                .map(addressDtoConverter::convert)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public List<AddressDto> getAddressByUserId(@NonNull Long userId) {

        // Check if user exists
        userRepository.findById(userId).orElseThrow(
                () -> new EntityNotFoundException("No addresses found for the user"));

        // Get addresses with country
        List<Address> addresses = addressRepository.findByUserIdWithCountry(userId);

        if (addresses.isEmpty()) {
            return List.of();
        }

        return addresses.stream()
                .map(addressDtoConverter::convert)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public AddressDto createAddress(AddressCreateRequest request) {

        // Check if user id is present
        Long userId = request.getUserId();
        if (userId == null) {
            throw new IllegalArgumentException("User ID is required");
        }

        // Get user
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("user", userId));

        // Check for duplicate address
        Optional<Address> existingAddress = addressRepository
                .findByUserIdAndAddressLine1AndCityAndStateAndPostalCode(
                        user.getId(),
                        request.getAddressLine1(),
                        request.getCity(),
                        request.getState(),
                        request.getPostalCode()
                );

        if (existingAddress.isPresent()) {
            throw new DuplicateEntityException("address");
        }

        // Get country
        Country country = countryRepository.findByNameIgnoreCase(request.getCountry())
                .orElseThrow(() -> new EntityNotFoundException("country", "name", request.getCountry()));

        // Build address
        Address address = Address.builder()
                .addressType(request.getAddressType())
                .addressLine1(request.getAddressLine1())
                .addressLine2(request.getAddressLine2())
                .city(request.getCity())
                .state(request.getState())
                .postalCode(request.getPostalCode())
                .country(country)
                .build();

        user.addAddress(address);

        // save and convert
        Address savedAddress = addressRepository.save(address);
        return addressDtoConverter.convert(savedAddress);
    }

    @Override
    @Transactional
    public AddressDto updateAddress(AddressUpdateRequest request) {

        // Validate required fields
        Long addressId = request.getId();
        if (addressId == null) {
            throw new IllegalArgumentException("Address id cannot be null");
        }

        Long userId = request.getUserId();
        if (userId == null) {
            throw new IllegalArgumentException("User id cannot be null");
        }

        // Get address
        Address existingAddress = addressRepository.findById(addressId)
                .orElseThrow(() -> new EntityNotFoundException("address", addressId));


        // Validate ownership
        if (!existingAddress.getUser().getId().equals(userId)) {
            throw new EntityUpdateException("Address does not belong to user");
        }

        // Get country ONLY if it's being updated
        Country country = null;
        if (request.getCountry() != null &&
                !request.getCountry().equals(existingAddress.getCountry().getName())) {
            country = countryRepository.findByNameIgnoreCase(request.getCountry())
                    .orElseThrow(() -> new EntityNotFoundException(
                            "country", "name", request.getCountry()));
        }

        // Update address
        updateAddressFields(existingAddress, request, country);

        // Save and convert
        Address savedAddress = addressRepository.save(existingAddress);
        return addressDtoConverter.convert(savedAddress);
    }

    @Override
    @Transactional
    public void deleteAddress(@NonNull Long addressId) {

        // Future considerations:
        //  Is this the user's only address?
        //  Does the user have any pending orders at this address?
        //  Who should be able to delete?

        if (!addressRepository.existsById(addressId)) {
            throw new EntityNotFoundException("address", addressId);
        }

        addressRepository.deleteById(addressId);
    }

    private void updateAddressFields(
            Address address,
            AddressUpdateRequest request,
            Country country) {
        if (request.getAddressType() != null) {
            address.setAddressType(request.getAddressType());
        }

        if (request.getAddressLine1() != null) {
            address.setAddressLine1(request.getAddressLine1());
        }

        if (request.getAddressLine2() != null) {
            address.setAddressLine2(request.getAddressLine2());
        }

        if (request.getCity() != null) {
            address.setCity(request.getCity());
        }

        if (request.getState() != null) {
            address.setState(request.getState());
        }

        if (request.getPostalCode() != null) {
            address.setPostalCode(request.getPostalCode());
        }

        if (country != null) {
            address.setCountry(country);
        }
    }
}
