package com.dallasdresses.converters;

import com.dallasdresses.dtos.response.AddressDto;
import com.dallasdresses.entities.Address;
import com.dallasdresses.entities.User;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class AddressDtoToAddressConverter implements Converter<AddressDto, Address> {

    @Override
    public Address convert(AddressDto addressDto) {

        if (addressDto == null) {
            return null;
        }

        return Address.builder()
                .id(addressDto.getId())
                .addressType(addressDto.getAddressType())
                .addressLine1(addressDto.getAddressLine1())
                .addressLine2(addressDto.getAddressLine2())
                .city(addressDto.getCity())
                .state(addressDto.getState())
                .country(addressDto.getCountry())
                .postalCode(addressDto.getPostalCode())
                .createdAt(addressDto.getCreatedAt())
                .updatedAt(addressDto.getUpdatedAt())
                .user(new User())
                .build();
    }

}
