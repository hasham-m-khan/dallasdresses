package com.dallasdresses.services;

import com.dallasdresses.converters.AddressToAddressDtoConverter;
import com.dallasdresses.dtos.AddressDto;
import com.dallasdresses.entities.Address;
import com.dallasdresses.entities.User;
import com.dallasdresses.exceptions.addresses.AddressCreationException;
import com.dallasdresses.exceptions.addresses.AddressNotFoundException;
import com.dallasdresses.exceptions.addresses.AddressUpdateException;
import com.dallasdresses.exceptions.addresses.DuplicateAddressException;
import com.dallasdresses.exceptions.users.UserNotFoundException;
import com.dallasdresses.models.request.AddressCreateRequest;
import com.dallasdresses.models.request.AddressUpdateRequest;
import com.dallasdresses.repositories.AddressRepository;
import com.dallasdresses.repositories.UserRepository;
import lombok.extern.slf4j.Slf4j;
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
    public List<AddressDto> getAddressByUserId(Long userId) {

        userRepository.findById(userId).orElseThrow(
                () -> new UserNotFoundException("Could not find address with user id: " + userId));

        Optional<List<Address>> addresses = addressRepository.findByUserId(userId);

        if (addresses.isPresent() && addresses.get().isEmpty()) {
            throw new AddressNotFoundException("No addresses found for user id: " + userId);
        }

        return addresses.get().stream()
                .map(addressDtoConverter::convert)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public AddressDto createAddress(AddressCreateRequest request) {

        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new UserNotFoundException(request.getUserId()));

        Optional<Address> existingAddress = addressRepository
                .findByUserIdAndAddressLine1AndCityAndStateAndPostalCode(
                        user.getId(),
                        request.getAddressLine1(),
                        request.getCity(),
                        request.getState(),
                        request.getPostalCode()
                );

        if (existingAddress.isPresent()) {
            throw new DuplicateAddressException("Address already exists for this user");
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
            throw new AddressCreationException(ex.getMessage());
        }
    }

    @Override
    @Transactional
    public AddressDto updateAddress(AddressUpdateRequest request) {

        Address existingAddress = addressRepository.findById(request.getId())
                .orElseThrow(() -> new AddressNotFoundException(request.getId()));

        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new  UserNotFoundException(request.getUserId()));

        if (!existingAddress.getUser().getId().equals(user.getId())) {
            throw new AddressUpdateException("Address does not belong to user");
        }

        try {
            existingAddress.setAddressType(request.getAddressType());
            existingAddress.setAddressLine1(request.getAddressLine1());
            existingAddress.setAddressLine2(request.getAddressLine2());
            existingAddress.setCity(request.getCity());
            existingAddress.setState(request.getState());
            existingAddress.setCountry(request.getCountry());
            existingAddress.setPostalCode(request.getPostalCode());

            Address savedAddress = addressRepository.save(existingAddress);

            return addressDtoConverter.convert(savedAddress);
        } catch (Exception ex) {
            throw new AddressUpdateException();
        }
    }

    @Override
    @Transactional
    public void deleteAddress(Long addressId) {

        // Future considerations:
        //  Is this the user's only address?
        //  Does the user have any pending orders at this address?
        //  Who should be able to delete?

        if (!addressRepository.existsById(addressId)) {
            throw new AddressNotFoundException(addressId);
        }

        addressRepository.deleteById(addressId);
    }
}
