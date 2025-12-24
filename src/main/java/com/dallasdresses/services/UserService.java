package com.dallasdresses.services;

import com.dallasdresses.dtos.request.UserCreateRequest;
import com.dallasdresses.dtos.request.UserUpdateRequest;
import com.dallasdresses.dtos.response.UserDto;

import java.util.List;

public interface UserService {

    List<UserDto> getAllUsers();
    UserDto getUserById(Long id);
    UserDto getUserByEmail(String email);
    UserDto updateUser(UserUpdateRequest request);
    UserDto createUser(UserCreateRequest request);
    void deleteUser(Long id);
}
