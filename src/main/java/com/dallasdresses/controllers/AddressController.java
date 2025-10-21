package com.dallasdresses.controllers;

import com.dallasdresses.dtos.response.AddressDto;
import com.dallasdresses.dtos.request.AddressCreateRequest;
import com.dallasdresses.dtos.request.AddressUpdateRequest;
import com.dallasdresses.dtos.common.ApiResponse;
import com.dallasdresses.services.AddressService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/api/v1/addresses")
public class AddressController {

    private final AddressService addressService;

    public AddressController(AddressService addressService) {
        this.addressService = addressService;
    }

    @GetMapping("")
    public ApiResponse<List<AddressDto>> getAllAddresses() {
        log.info("🧲 Fetching all addresses");

        Map<String, Object> metadata = new HashMap<>();
        List<AddressDto> addressesDto = addressService.getAllAddresses();
        metadata.put("filtered", false);
        metadata.put("totalCount", addressesDto.size());

        return ApiResponse.<List<AddressDto>>builder()
                .success(true)
                .data(addressesDto)
                .metadata(metadata)
                .message("Addresses retrieved successfully")
                .build();
    }

    @GetMapping( "/{userId}")
    public ApiResponse<List<AddressDto>> getAddressesByUserId(@PathVariable Long userId){
        log.info("🧲 Fetching user with id: {}", userId);

        Map<String, Object> metadata = new HashMap<>();
        List<AddressDto> addressesDto = addressService.getAddressByUserId(userId);
        metadata.put("filtered", true);
        metadata.put("filterType", "userId");
        metadata.put("filterValue", userId);

        return ApiResponse.<List<AddressDto>>builder()
                .success(true)
                .data(addressesDto)
                .metadata(metadata)
                .message("Addresses retrieved successfully")
                .build();
    }

    @PostMapping("")
    public ApiResponse<AddressDto> createAddress(
            @Valid @RequestBody AddressCreateRequest addressRequest){
        log.info("🔔 Creating new address: {}", addressRequest);

        AddressDto savedAddress = addressService.createAddress(addressRequest);

        log.info("🧶 Created address: {}", savedAddress);

        Map<String, Object> metadata = new HashMap<>();
        return ApiResponse.<AddressDto>builder()
                .success(true)
                .data(savedAddress)
                .metadata(metadata)
                .message("Address created successfully")
                .build();
    }

    @PutMapping("/{id}")
    public ApiResponse<AddressDto> updateAddress(
            @PathVariable Long id,
            @Valid @RequestBody AddressUpdateRequest addressRequest) {
        log.info("🔔 Updating address: {}", addressRequest);

        addressRequest.setId(id);
        AddressDto updatedAddress =  addressService.updateAddress(addressRequest);

        log.info("🧶 Updated address: {}", updatedAddress);

        Map<String, Object> metadata = new HashMap<>();
        return ApiResponse.<AddressDto>builder()
                .success(true)
                .data(updatedAddress)
                .metadata(metadata)
                .message("Address updated successfully")
                .build();
    }
}
