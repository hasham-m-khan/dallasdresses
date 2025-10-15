package com.dallasdresses.converters;

import com.dallasdresses.dtos.UserDto;
import com.dallasdresses.entities.User;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import java.util.HashSet;

@Component
public class UserDtoToUserConverter implements Converter<UserDto, User> {

    AddressDtoToAddressConverter addressConverter;

    public UserDtoToUserConverter(AddressDtoToAddressConverter addressConverter) {
        this.addressConverter = addressConverter;
    }

    @Override
    public User convert(UserDto userDto) {
        if (userDto == null) {
            return null;
        }

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
