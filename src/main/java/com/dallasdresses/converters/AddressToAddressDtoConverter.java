package com.dallasdresses.converters;

import com.dallasdresses.dtos.response.AddressDto;
import com.dallasdresses.entities.Address;
import org.springframework.core.convert.converter.Converter;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

@Component
public class AddressToAddressDtoConverter implements Converter<Address, AddressDto> {

    @Override
    public AddressDto convert(@NonNull Address address) {

        return new AddressDto(
                address.getId(),
                address.getAddressType(),
                address.getAddressLine1(),
                address.getAddressLine2(),
                address.getCity(),
                address.getState(),
                address.getCountry().getName(),
                address.getPostalCode(),
                address.getCreatedAt(),
                address.getUpdatedAt()
        );
    }
}
