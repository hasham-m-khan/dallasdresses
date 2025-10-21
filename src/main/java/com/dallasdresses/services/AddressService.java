package com.dallasdresses.services;

import com.dallasdresses.dtos.response.AddressDto;
import com.dallasdresses.dtos.request.AddressCreateRequest;
import com.dallasdresses.dtos.request.AddressUpdateRequest;

import java.util.List;

public interface AddressService {

    List<AddressDto> getAllAddresses();
    List<AddressDto> getAddressByUserId(Long userId);
    AddressDto createAddress(AddressCreateRequest request);
    AddressDto updateAddress(AddressUpdateRequest request);
    void deleteAddress(Long addressId);
}
