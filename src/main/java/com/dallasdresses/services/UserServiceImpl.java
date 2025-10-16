package com.dallasdresses.services;

import com.dallasdresses.converters.AddressToAddressDtoConverter;
import com.dallasdresses.converters.UserToUserDtoConverter;
import com.dallasdresses.dtos.UserDto;
import com.dallasdresses.entities.User;
import com.dallasdresses.exceptions.users.DuplicateUserException;
import com.dallasdresses.exceptions.users.UserCreationException;
import com.dallasdresses.exceptions.users.UserNotFoundException;
import com.dallasdresses.repositories.AddressRepository;
import com.dallasdresses.repositories.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
@Transactional(readOnly = true)
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserToUserDtoConverter userDtoConverter;

    public UserServiceImpl(UserRepository userRepository,
                           AddressRepository addressRepository,
                           UserToUserDtoConverter userDtoConverter,
                           AddressToAddressDtoConverter addressDtoConverter) {
        this.userRepository = userRepository;
        this.userDtoConverter = userDtoConverter;
    }

    @Override
    public List<UserDto> getAllUsers() {
        return userRepository.findAllWithAddresses().stream()
                .map(userDtoConverter::convert)
                .collect(Collectors.toList());
    }

    @Override
    public UserDto getUserById(Long id) {
        User user = userRepository.findByIdWithAddresses(id)
                .orElseThrow(() -> new UserNotFoundException((id)));

        return userDtoConverter.convert(user);
    }

    @Override
    public UserDto getUserByEmail(String email) {
        User user = userRepository.findByEmailWithAddresses(email)
                .orElseThrow(() -> new UserNotFoundException("User not found with email: " + email));

        return  userDtoConverter.convert(user);
    }

    @Override
    public User updateUser(User user) {
        User existingUser = userRepository.findById(user.getId())
                .orElseThrow(() -> new UserNotFoundException(user.getId()));

        if (!existingUser.getEmail().equals(user.getEmail())) {
            userRepository.findUserByEmail(user.getEmail())
                    .ifPresent(u -> {
                        throw new DuplicateUserException(user.getEmail());
                    });
        }

        try {
            return userRepository.save(user);
        } catch (Exception ex) {
            throw new UserCreationException("Failed to update user with id " + user.getId(), ex);
        }
    }

    @Override
    public UserDto createUser(User user) {

        if (userRepository.findUserByEmail(user.getEmail()).isPresent()) {
            log.error("User with email '{}' already exists", user.getEmail());

            throw new UserCreationException("User with email '" + user.getEmail() + "' already exists");
        }

        try {
            return userDtoConverter.convert(userRepository.save(user));
        } catch (Exception e) {
            throw new UserCreationException("User creation failed");
        }
    }

    @Override
    public void deleteUser(Long id) {
        if (!userRepository.existsById(id)) {
            throw new UserNotFoundException(id);
        }

        userRepository.deleteById(id);
    }
}
