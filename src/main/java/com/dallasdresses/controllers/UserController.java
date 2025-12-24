package com.dallasdresses.controllers;

import com.dallasdresses.dtos.request.UserCreateRequest;
import com.dallasdresses.dtos.request.UserUpdateRequest;
import com.dallasdresses.dtos.response.UserDto;
import com.dallasdresses.dtos.common.ApiResponse;
import com.dallasdresses.services.UserService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@Slf4j
@RestController
@RequestMapping("${app.api.baseurl}/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("")
    public ApiResponse<List<UserDto>> getAllUsers() {
        log.info("🧲 Fetching all users");

        List<UserDto> userDtos = userService.getAllUsers();

        Map<String, Object> metadata = new HashMap<>();
        metadata.put("filtered", false);
        metadata.put("totalCount", userDtos.size());

        return ApiResponse.<List<UserDto>>builder()
                .success(true)
                .data(userDtos)
                .metadata(metadata)
                .message("Users retrieved successfully")
                .build();
    }

    @GetMapping("/{id}")
    public ApiResponse<UserDto> getUserById(@Valid @PathVariable Long id) {
        log.info("🧲 Fetching user with id: {}", id);

        Map<String, Object> metadata = new HashMap<>();
        UserDto userDto = userService.getUserById(id);
        metadata.put("filtered", true);
        metadata.put("filterType", "id");
        metadata.put("addressCount", userDto.getAddresses().size());

        return ApiResponse.<UserDto>builder()
                .success(true)
                .data(userDto)
                .metadata(metadata)
                .message("Users retrieved successfully")
                .build();
    }

    @GetMapping("/search")
    public ApiResponse<UserDto> getUserByEmail(@Valid @RequestParam String email) {
        log.info("🧲 Fetching user with email: {}", email);

        UserDto userDto = userService.getUserByEmail(email);

        Map<String, Object> metadata = new HashMap<>();
        metadata.put("filtered", true);
        metadata.put("filterType", "email");

        return ApiResponse.<UserDto>builder()
                .success(true)
                .data(userDto)
                .metadata(metadata)
                .message("Users retrieved successfully")
                .build();
    }

    @PostMapping({"", "/"})
    public ApiResponse<UserDto> createUser(@Valid @RequestBody UserCreateRequest request) {
        log.info("🔔 Creating new user: {}", request);

        Map<String, Object> metadata = new HashMap<>();
        UserDto userDto = userService.createUser(request);

        log.info("🧶 Created user: {}", userDto);

        return ApiResponse.<UserDto>builder()
                .success(true)
                .data(userDto)
                .metadata(metadata)
                .message("User created successfully")
                .build();
    }

    @PutMapping("/{id}")
    public ApiResponse<UserDto> updateUser(@Valid @RequestBody UserUpdateRequest request) {
        log.info("🔔 Updating user with id: {}", request.getId());

        UserDto updatedUser = userService.updateUser(request);

        log.info("🧶 User with id '{}' updated: {}", updatedUser.getId(), updatedUser);

        return ApiResponse.<UserDto>builder()
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
