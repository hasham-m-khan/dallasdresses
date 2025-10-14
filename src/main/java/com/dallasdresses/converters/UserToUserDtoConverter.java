package com.dallasdresses.converters;

import com.dallasdresses.dtos.UserDto;
import com.dallasdresses.entities.User;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import java.util.HashSet;

@Component
public class UserToUserDtoConverter implements Converter<User, UserDto> {

    private final AddressToAddressDtoConverter addressDtoConverter;

    public UserToUserDtoConverter(AddressToAddressDtoConverter addressDtoConverter) {
        this.addressDtoConverter = addressDtoConverter;
    }

    @Override
    public UserDto convert(User user) {

        if (user == null) {
            return null;
        }

        UserDto userDto = UserDto.builder()
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
                .addresses(new HashSet<>())
                .build();

        if (user.getAddresses() != null && !user.getAddresses().isEmpty()) {
            user.getAddresses().forEach(address ->
                    userDto.getAddresses().add(addressDtoConverter.convert(address)));
        }

        userDto.setCredentialCount(0);
        if (user.getCredentials() != null) {
            userDto.setCredentialCount(user.getCredentials().size());
        }

        return userDto;
    }
}
