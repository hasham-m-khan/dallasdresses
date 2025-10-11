package com.dallasdresses.controllers;

import com.dallasdresses.entities.User;
import com.dallasdresses.models.response.ApiResponse;
import com.dallasdresses.services.UserService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@Slf4j
@RestController
@RequestMapping("/api/v1/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("")
    public ApiResponse<List<User>> getAllUsers() {
        log.info("🧲 Fetching all users");

        Map<String, Object> metadata = new HashMap<>();
        List<User> users = userService.getAllUsers();
        metadata.put("filtered", false);
        metadata.put("totalCount", users.size());

        return ApiResponse.<List<User>>builder()
                .success(true)
                .data(users)
                .metadata(metadata)
                .message("Users retrieved successfully")
                .build();
    }

    @GetMapping("/{id}")
    public ApiResponse<List<User>> getUserById(@Valid @PathVariable Long id) {
        log.info("🧲 Fetching user with id: {}", id);

        Map<String, Object> metadata = new HashMap<>();
        List<User> users = List.of(userService.findUserById(id));
        metadata.put("filtered", true);
        metadata.put("filterType", "id");

        return ApiResponse.<List<User>>builder()
                .success(true)
                .data(users)
                .metadata(metadata)
                .message("Users retrieved successfully")
                .build();
    }

    @GetMapping("/search")
    public ApiResponse<List<User>> getUserByEmail(@Valid @RequestParam String email) {
        log.info("🧲 Fetching user with email: {}", email);

        Map<String, Object> metadata = new HashMap<>();
        List<User> users = List.of(userService.findUserByEmail(email));
        metadata.put("filtered", true);
        metadata.put("filterType", "email");

        return ApiResponse.<List<User>>builder()
                .success(true)
                .data(users)
                .metadata(metadata)
                .message("Users retrieved successfully")
                .build();
    }

    @PostMapping({"", "/"})
    public ApiResponse<User> createUser(@Valid @RequestBody User user) {
        log.info("🔔 Creating new user: {}", user);

        Map<String, Object> metadata = new HashMap<>();
        User savedUser = userService.createUser(user);

        log.info("🧶 Created user: {}", savedUser);

        return ApiResponse.<User>builder()
                .success(true)
                .data(user)
                .metadata(metadata)
                .message("User created successfully")
                .build();
    }

    @PutMapping("/{id}")
    public ApiResponse<User> updateUser(@PathVariable Long id, @Valid @RequestBody User user) {
        log.info("🔔 Updating user with id: {}", id);

        User updatedUser = userService.updateUser(user);

        log.info("🧶 User with id '{}' updated: {}", id, updatedUser);

        return ApiResponse.<User>builder()
                .success(true)
                .data(updatedUser)
                .metadata(null)
                .message("Users updated successfully")
                .build();
    }

    @DeleteMapping("/{id}")
    public void deleteUser(@PathVariable Long id) {
        log.info("🔔 Deleting user with id: {}", id);

        this.userService.deleteUser(id);

        log.info("🧶 User with id '{}' deleted", id);
    }
}
