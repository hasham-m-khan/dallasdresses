package com.dallasdresses.services;

import com.dallasdresses.converters.UserToUserDtoConverter;
import com.dallasdresses.dtos.UserDto;
import com.dallasdresses.entities.User;
import com.dallasdresses.exceptions.users.DuplicateUserException;
import com.dallasdresses.exceptions.users.UserCreationException;
import com.dallasdresses.exceptions.users.UserNotFoundException;
import com.dallasdresses.repositories.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("User Service Tests")
class UserServiceImplTest {

    @Mock
    UserRepository userRepository;

    @Mock
    UserToUserDtoConverter userDtoConverter;

    @InjectMocks
    UserServiceImpl userService;

    User user1;
    User user2;
    UserDto userDto1;
    UserDto userDto2;

    @BeforeEach
    void setUp() {
        user1 = new User();
        user1.setId(1L);
        user1.setEmail("john.doe@xyzmail.com");

        user2 = new User();
        user2.setId(2L);
        user2.setEmail("jane.smith@xyzmail.com");

        userDto1 = new UserDto();
        userDto1.setId(1L);
        userDto1.setEmail("john.doe@xyzmail.com");

        userDto2 = new UserDto();
        userDto2.setId(2L);
        userDto2.setEmail("jane.smith@xyzmail.com");
    }

    @Test
    @DisplayName("getAllUsers - Should return all users")
    void testGetAllUsers_ShouldReturnAllUsers() {
        // Arrange
        List<User> users = new ArrayList<>();
        users.add(user1);
        users.add(user2);

        // Act
        when(userRepository.findAllWithAddresses()).thenReturn(users);

        // Assert
        assertEquals(users.size(), userService.getAllUsers().size());
        verify(userRepository, times(1)).findAllWithAddresses();
    }

    @Test
    @DisplayName("getUserById - Should Return User")
    void testGetUserById_ShouldReturnUser_WhenNoErrors() {
        // Arrange & Act
        when(userRepository.findByIdWithAddresses(1L)).thenReturn(Optional.of(user1));
        when(userDtoConverter.convert(user1)).thenReturn(userDto1);

        // Assert
        assertEquals(userDto1, userService.getUserById(1L));
        verify(userRepository, times(1)).findByIdWithAddresses(1L);
        verify(userDtoConverter, times(1)).convert(any(User.class));
    }

    @Test
    @DisplayName("getUserById - Should throw UserNotFound")
    void testGetUserById_ShouldThrowUserNotFoundException_WhenUserNotFound() {
        // Arrange & Act
        when(userRepository.findByIdWithAddresses(500L)).thenReturn(Optional.empty());

        // Assert
        assertThrows(UserNotFoundException.class, () -> userService.getUserById(500L));
        verify(userRepository, times(1)).findByIdWithAddresses(500L);
        verify(userDtoConverter, never()).convert(any(User.class));
    }

    @Test
    @DisplayName("getUserByEmail - Should Return User")
    void testGetUserByEmail_ShouldReturnUser_WhenNoErrors() {
        // Arrange & Act
        when(userRepository.findByEmailWithAddresses(user1.getEmail())).thenReturn(Optional.of(user1));
        when(userDtoConverter.convert(user1)).thenReturn(userDto1);

        // Assert
        assertEquals(userDto1, userService.getUserByEmail(user1.getEmail()));
        verify(userRepository, times(1)).findByEmailWithAddresses(user1.getEmail());
        verify(userDtoConverter, times(1)).convert(any(User.class));
    }

    @Test
    @DisplayName("getUserByEmail - Should throw UserNotFound")
    void testGetUserByEmail_ShouldThrowUserNotFoundException_WhenUserNotFound() {
        // Arrange & Act
        when(userRepository.findByEmailWithAddresses(user1.getEmail())).thenReturn(Optional.empty());

        // Assert
        assertThrows(UserNotFoundException.class, () -> userService.getUserByEmail(user1.getEmail()));
        verify(userRepository, times(1)).findByEmailWithAddresses(anyString());
        verify(userDtoConverter, never()).convert(any(User.class));
    }

    @Test
    @DisplayName("updateUser - Should update user")
    void testUpdateUser_ShouldUpdateUser_WhenNoErrors() {
        // Arrange
        User existingUser = new User();
        existingUser.setId(1L);
        existingUser.setEmail("abc@xyz.com");
        existingUser.setFirstName("John");

        User updatedUser = new User();
        updatedUser.setId(1L);
        updatedUser.setEmail("abc@xyz.com");
        updatedUser.setFirstName("Jeremy");

        UserDto updatedUserDto = new UserDto();
        updatedUserDto.setId(1L);
        updatedUserDto.setEmail("abc@xyz.com");
        updatedUserDto.setFirstName("Jeremy");

        // Act
        when(userRepository.findById(1L)).thenReturn(Optional.of(existingUser));
        when(userRepository.save(updatedUser)).thenReturn(updatedUser);
        when(userDtoConverter.convert(updatedUser)).thenReturn(updatedUserDto);

        // Assert
        assertEquals(updatedUserDto, userService.updateUser(updatedUser));
        verify(userRepository, times(1)).findById(1L);
        verify(userRepository, times(1)).save(updatedUser);
        verify(userDtoConverter, times(1)).convert(any(User.class));
    }

    @Test
    @DisplayName("updateUser - Should update user")
    void testUpdateUser_ShouldThrowUserNotFoundException_WhenUserNotFound() {
        // Arrange
        User nonexistingUser = new User();
        nonexistingUser.setId(1L);
        nonexistingUser.setEmail("abc@xyz.com");

        // Act
        when(userRepository.findById(anyLong())).thenReturn(Optional.empty());

        // Assert
        assertThrows(UserNotFoundException.class, () -> userService.updateUser(nonexistingUser));
        verify(userRepository, times(1)).findById(anyLong());
        verify(userRepository, never()).findUserByEmail(anyString());
        verify(userDtoConverter, never()).convert(any(User.class));
    }

    @Test
    @DisplayName("updateUser - Should throw DuplicateUserException")
    void testUpdateUser_ShouldThrowDuplicateUserException_WhenDifferentUserWithSameEmailFound() {
        // Arrange
        String matchingEmail = "abc@xyz.com";

        User updateUser = new User();
        updateUser.setId(1L);
        updateUser.setEmail(matchingEmail);

        User existingUser1 = new User();
        existingUser1.setId(1L);
        existingUser1.setEmail("def@xyz.com");

        User existingUser2 = new User();
        existingUser2.setId(2L);
        existingUser2.setEmail(matchingEmail);

        // Act
        when(userRepository.findById(1L)).thenReturn(Optional.of(existingUser1));
        when(userRepository.findUserByEmail(matchingEmail)).thenReturn(Optional.of(existingUser2));

        // Assert
        assertThrows(DuplicateUserException.class, () -> userService.updateUser(updateUser));
        verify(userRepository, times(1)).findById(anyLong());
        verify(userRepository, times(1)).findUserByEmail(anyString());
        verify(userDtoConverter, never()).convert(any(User.class));
    }

    @Test
    @DisplayName("updateUser - Should throw UserCreationException")
    void testUpdateUser_ShouldThrowUserCreationException_WhenUserCreationException() {
        // Arrange
        User existingUser = new User();
        existingUser.setId(1L);
        existingUser.setEmail("abc@xyz.com");
        existingUser.setFirstName("John");

        // Act
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(existingUser));
        when(userRepository.save(any(User.class))).thenThrow(new RuntimeException("some exception"));

        // Assert
        assertThrows(UserCreationException.class, () -> userService.updateUser(existingUser));
        verify(userRepository, times(1)).findById(anyLong());
        verify(userRepository, never()).findUserByEmail(anyString());
        verify(userRepository,times(1)).save(any(User.class));
        verify(userDtoConverter, never()).convert(any(User.class));
    }

    @Test
    @DisplayName("createUser - Should create User")
    void testCreateUser_ShouldCreateUser_WhenNoErrors() {
        // Arrange
        User user = new User();
        user.setId(1L);
        user.setEmail("abc@xyz.com");

        UserDto userDto = new UserDto();
        userDto.setId(1L);
        userDto.setEmail("abc@xyz.com");

        // Act
        when(userRepository.findUserByEmail(anyString())).thenReturn(Optional.empty());
        when(userRepository.save(user)).thenReturn(user);
        when(userDtoConverter.convert(user)).thenReturn(userDto);

        // Assert
        assertEquals(userDto, userService.createUser(user));
        verify(userRepository, times(1)).findUserByEmail(anyString());
        verify(userRepository, times(1)).save(any(User.class));
        verify(userDtoConverter, times(1)).convert(any(User.class));
    }

    @Test
    @DisplayName("createUser - Should throw DuplicateUserException")
    void testCreateUser_ShouldThrowUserCreationException_WhenUserExists() {
        // Arrange
        User user = new User();
        user.setId(1L);
        user.setEmail("abc@xyz.com");

        // Act
        when(userRepository.findUserByEmail(anyString())).thenReturn(Optional.of(user));

        // Assert
        assertThrows(DuplicateUserException.class, () -> userService.createUser(user));
        verify(userRepository, times(1)).findUserByEmail(anyString());
        verify(userRepository, never()).save(any(User.class));
        verify(userDtoConverter, never()).convert(any(User.class));
    }

    @Test
    @DisplayName("createUser - Should throw UserCreationException")
    void testCreateUser_ShouldThrowUserCreationException_WhenError() {
        // Arrange
        User user = new User();
        user.setId(1L);
        user.setEmail("abc@xyz.com");

        // Act
        when(userRepository.findUserByEmail(anyString())).thenReturn(Optional.empty());
        when(userRepository.save(any(User.class))).thenThrow(new RuntimeException("some exception"));

        // Assert
        assertThrows(UserCreationException.class, () -> userService.createUser(user));
        verify(userRepository, times(1)).findUserByEmail(anyString());
        verify(userRepository, times(1)).save(any(User.class));
        verify(userDtoConverter, never()).convert(any(User.class));
    }
}