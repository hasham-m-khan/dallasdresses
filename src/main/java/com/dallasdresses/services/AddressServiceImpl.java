package com.dallasdresses.services;

import com.dallasdresses.converters.AddressToAddressDtoConverter;
import com.dallasdresses.dtos.response.AddressDto;
import com.dallasdresses.entities.Address;
import com.dallasdresses.entities.User;
import com.dallasdresses.exceptions.DuplicateEntityException;
import com.dallasdresses.exceptions.EntityNotFoundException;
import com.dallasdresses.exceptions.EntityUpdateException;
import com.dallasdresses.exceptions.InvalidEntityException;
import com.dallasdresses.dtos.request.AddressCreateRequest;
import com.dallasdresses.dtos.request.AddressUpdateRequest;
import com.dallasdresses.repositories.AddressRepository;
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

    public AddressServiceImpl(AddressRepository addressRepository,
                              UserRepository userRepository,
                              AddressToAddressDtoConverter addressDtoConverter) {
        this.addressRepository = addressRepository;
        this.userRepository = userRepository;
        this.addressDtoConverter = addressDtoConverter;
    }

    public List<AddressDto> getAllAddresses() {
        return addressRepository.findAll().stream()
                .map(addressDtoConverter::convert)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public List<AddressDto> getAddressByUserId(@NonNull Long userId) {

        userRepository.findById(userId).orElseThrow(
                () -> new EntityNotFoundException("No addresses found for the user"));

        Optional<List<Address>> addresses = addressRepository.findByUserId(userId);

        if (addresses.isPresent() && addresses.get().isEmpty()) {
            throw new EntityNotFoundException("No addresses found for the user");
        }

        return addresses.get().stream()
                .map(addressDtoConverter::convert)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public AddressDto createAddress(AddressCreateRequest request) {

        Long userId = request.getUserId();
        if (userId == null) {
            throw new IllegalArgumentException("User ID is required");
        }

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("user", request.getUserId()));

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

        try {
            Address address = new Address();
            address.setAddressType(request.getAddressType());
            address.setAddressLine1(request.getAddressLine1());
            address.setAddressLine2(request.getAddressLine2());
            address.setCity(request.getCity());
            address.setState(request.getState());
            address.setCountry(request.getCountry());
            address.setPostalCode(request.getPostalCode());
            address.setUser(user);

            Address savedAddress = addressRepository.save(address);
            return addressDtoConverter.convert(savedAddress);
        } catch (Exception ex) {
            throw new InvalidEntityException("Error creating address");
        }
    }

    @Override
    @Transactional
    public AddressDto updateAddress(AddressUpdateRequest request) {

        Long addressId = request.getId();
        if (addressId == null) {
            throw new IllegalArgumentException("Address id cannot be null");
        }

        Long userId = request.getUserId();
        if (userId == null) {
            throw new IllegalArgumentException("User id cannot be null");
        }

        Address existingAddress = addressRepository.findById(addressId)
                .orElseThrow(() -> new EntityNotFoundException("address", addressId));

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new  EntityNotFoundException("user", userId));

        if (!existingAddress.getUser().getId().equals(userId)) {
            throw new EntityUpdateException("Address does not belong to user");
        }

        try {
            existingAddress.setAddressType(request.getAddressType());
            existingAddress.setAddressLine1(request.getAddressLine1());
            existingAddress.setAddressLine2(request.getAddressLine2());
            existingAddress.setCity(request.getCity());
            existingAddress.setState(request.getState());
            existingAddress.setCountry(request.getCountry());
            existingAddress.setPostalCode(request.getPostalCode());
            existingAddress.setUser(user);

            Address savedAddress = addressRepository.save(existingAddress);

            return addressDtoConverter.convert(savedAddress);
        } catch (Exception ex) {
            throw new InvalidEntityException("Error updating address");
        }
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
}
