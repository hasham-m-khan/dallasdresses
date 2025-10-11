package com.dallasdresses.services;

import com.dallasdresses.entities.User;

import java.util.List;

public interface UserService {

    List<User> getAllUsers();
    User findUserById(Long id);
    User findUserByEmail(String email);
    User updateUser(User user);
    User createUser(User user);
    void deleteUser(Long id);
}
