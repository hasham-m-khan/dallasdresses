package com.dallasdresses.converters;

import com.dallasdresses.dtos.response.AddressDto;
import com.dallasdresses.dtos.response.UserDto;
import com.dallasdresses.entities.User;
import org.springframework.core.convert.converter.Converter;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class UserToUserDtoConverter implements Converter<User, UserDto> {

    private final AddressToAddressDtoConverter addressDtoConverter;

    public UserToUserDtoConverter(AddressToAddressDtoConverter addressDtoConverter) {
        this.addressDtoConverter = addressDtoConverter;
    }

    @Override
    public UserDto convert(@NonNull User user) {

        Set<AddressDto> addresses = new HashSet<>();
        if (!user.getAddresses().isEmpty() && user.getAddresses() != null) {
            addresses = user.getAddresses().stream()
                    .map(addressDtoConverter::convert)
                    .collect(Collectors.toSet());
        }

        return UserDto.builder()
                .id(user.getId())
                .email(user.getEmail())
                .role(user.getRole())
                .locale(user.getLocale())
                .emailVerified(user.getEmailVerified())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .telephone(user.getTelephone())
                .avatar(user.getAvatar())
                .createdAt(user.getCreatedAt())
                .updatedAt(user.getUpdatedAt())
                .addresses(addresses)
                .build();
    }
}
