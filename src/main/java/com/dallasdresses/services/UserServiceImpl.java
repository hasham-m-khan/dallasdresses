package com.dallasdresses.services;

import com.dallasdresses.converters.UserToUserDtoConverter;
import com.dallasdresses.dtos.response.UserDto;
import com.dallasdresses.entities.User;
import com.dallasdresses.exceptions.DuplicateEntityException;
import com.dallasdresses.exceptions.EntityNotFoundException;
import com.dallasdresses.exceptions.InvalidEntityException;
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
                           UserToUserDtoConverter userDtoConverter) {
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
                .orElseThrow(() -> new EntityNotFoundException("user", id));

        return userDtoConverter.convert(user);
    }

    @Override
    public UserDto getUserByEmail(String email) {
        User user = userRepository.findByEmailWithAddresses(email)
                .orElseThrow(() -> new EntityNotFoundException("user", "email", email));

        return  userDtoConverter.convert(user);
    }

    @Override
    @Transactional
    public UserDto updateUser(User user) {
        User existingUser = userRepository.findById(user.getId())
                .orElseThrow(() -> new EntityNotFoundException("user", user.getId()));

        if (!existingUser.getEmail().equals(user.getEmail())) {
            userRepository.findUserByEmail(user.getEmail())
                    .ifPresent(u -> {
                        throw new DuplicateEntityException("user", "emai", user.getEmail());
                    });
        }

        try {
            return userDtoConverter.convert(userRepository.save(user));
        } catch (Exception ex) {
            throw new InvalidEntityException("Failed to update user with id " + user.getId());
        }
    }

    @Override
    @Transactional
    public UserDto createUser(User user) {

        if (userRepository.findUserByEmail(user.getEmail()).isPresent()) {
            log.error("User with email '{}' already exists", user.getEmail());

            throw new DuplicateEntityException("user", "email", user.getEmail());
        }

        try {
            return userDtoConverter.convert(userRepository.save(user));
        } catch (Exception e) {
            throw new InvalidEntityException("User creation failed");
        }
    }

    @Override
    @Transactional
    public void deleteUser(Long id) {
        if (!userRepository.existsById(id)) {
            throw new EntityNotFoundException("user", id);
        }

        userRepository.deleteById(id);
    }
}
