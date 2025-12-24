package com.dallasdresses.converters;

import com.dallasdresses.dtos.response.UserDto;
import com.dallasdresses.entities.User;
import org.springframework.core.convert.converter.Converter;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

import java.util.HashSet;

@Component
public class UserDtoToUserConverter implements Converter<UserDto, User> {

    AddressDtoToAddressConverter addressConverter;

    public UserDtoToUserConverter(AddressDtoToAddressConverter addressConverter) {
        this.addressConverter = addressConverter;
    }

    @Override
    public User convert(@NonNull UserDto userDto) {

        return User.builder()
                .id(userDto.getId())
                .email(userDto.getEmail())
                .role(userDto.getRole())
                .locale(userDto.getLocale())
                .emailVerified(userDto.getEmailVerified())
                .firstName(userDto.getFirstName())
                .lastName(userDto.getLastName())
                .telephone(userDto.getTelephone())
                .avatar(userDto.getAvatar())
                .createdAt(userDto.getCreatedAt())
                .updatedAt(userDto.getUpdatedAt())
                .addresses(new HashSet<>())
                .build();
    }
}
