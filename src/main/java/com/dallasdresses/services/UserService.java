package com.dallasdresses.services;

import com.dallasdresses.dtos.response.UserDto;
import com.dallasdresses.entities.User;

import java.util.List;

public interface UserService {

    List<UserDto> getAllUsers();
    UserDto getUserById(Long id);
    UserDto getUserByEmail(String email);
    UserDto updateUser(User user);
    UserDto createUser(User user);
    void deleteUser(Long id);
}
