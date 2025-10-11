package com.dallasdresses.services;

import com.dallasdresses.entities.User;
import com.dallasdresses.exceptions.users.UserCreationException;
import com.dallasdresses.exceptions.users.UserNotFoundException;
import com.dallasdresses.repositories.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public User findUserById(Long id) {
        return  userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException(id));
    }

    @Override
    public User findUserByEmail(String email) {
        return  userRepository.findUserByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("email", email));
    }

    @Override
    public User updateUser(User user) {
        Optional<User> savedUser = userRepository.findById(user.getId());

        if (savedUser.isEmpty()) {
            log.error("User with id {} not found", user.getId());
        }

        return userRepository.save(user);
    }

    @Override
    public User createUser(User user) {

        if (userRepository.findUserByEmail(user.getEmail()).isPresent()) {
            log.error("User with email '{}' already exists", user.getEmail());

            throw new UserCreationException("User with email '" + user.getEmail() + "' already exists");
        }

        try {
            User savedUser = userRepository.save(user);
        } catch (Exception e) {
            throw new UserCreationException("User creation failed");
        }
        return user;
    }

    @Override
    public void deleteUser(Long id) {
        log.info("Deleting user {}", id);

        userRepository.deleteById(id);
    }
}
