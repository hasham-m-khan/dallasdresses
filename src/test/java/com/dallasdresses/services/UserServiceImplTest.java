package com.dallasdresses.services;

import com.dallasdresses.converters.UserToUserDtoConverter;
import com.dallasdresses.dtos.UserDto;
import com.dallasdresses.entities.User;
import com.dallasdresses.exceptions.DuplicateEntityException;
import com.dallasdresses.exceptions.EntityNotFoundException;
import com.dallasdresses.exceptions.InvalidEntityException;
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

        List<UserDto> userDtos = new ArrayList<>();
        userDtos.add(userDto1);
        userDtos.add(userDto2);

        // Act
        when(userRepository.findAllWithAddresses()).thenReturn(users);
        when(userDtoConverter.convert(user1)).thenReturn(userDto1);
        when(userDtoConverter.convert(user2)).thenReturn(userDto2);

        // Assert
        assertEquals(userDtos, userService.getAllUsers());
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
    @DisplayName("getUserById - Should throw EntityNotFoundException")
    void testGetUserById_ShouldThrowEntityNotFoundException_WhenUserNotFound() {
        // Arrange & Act
        when(userRepository.findByIdWithAddresses(500L)).thenReturn(Optional.empty());

        // Assert
        assertThrows(EntityNotFoundException.class, () -> userService.getUserById(500L));
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
    @DisplayName("getUserByEmail - Should throw EntityNotFoundException")
    void testGetUserByEmail_ShouldThrowEntityNotFoundException_WhenUserNotFound() {
        // Arrange & Act
        when(userRepository.findByEmailWithAddresses(user1.getEmail())).thenReturn(Optional.empty());

        // Assert
        assertThrows(EntityNotFoundException.class, () -> userService.getUserByEmail(user1.getEmail()));
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
    @DisplayName("updateUser - Should throw EntityNotFoundException")
    void testUpdateUser_ShouldThrowEntityNotFoundException_WhenUserNotFound() {
        // Arrange
        User nonexistingUser = new User();
        nonexistingUser.setId(1L);
        nonexistingUser.setEmail("abc@xyz.com");

        // Act
        when(userRepository.findById(anyLong())).thenReturn(Optional.empty());

        // Assert
        assertThrows(EntityNotFoundException.class, () -> userService.updateUser(nonexistingUser));
        verify(userRepository, times(1)).findById(anyLong());
        verify(userRepository, never()).findUserByEmail(anyString());
        verify(userDtoConverter, never()).convert(any(User.class));
    }

    @Test
    @DisplayName("updateUser - Should throw DuplicateEntityException")
    void testUpdateUser_ShouldThrowDuplicateEntityException_WhenDifferentUserWithSameEmailFound() {
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
        assertThrows(DuplicateEntityException.class, () -> userService.updateUser(updateUser));
        verify(userRepository, times(1)).findById(anyLong());
        verify(userRepository, times(1)).findUserByEmail(anyString());
        verify(userDtoConverter, never()).convert(any(User.class));
    }

    @Test
    @DisplayName("updateUser - Should throw InvalidEntityException")
    void testUpdateUser_ShouldThrowInvalidEntityException_WhenUserCreationException() {
        // Arrange
        User existingUser = new User();
        existingUser.setId(1L);
        existingUser.setEmail("abc@xyz.com");
        existingUser.setFirstName("John");

        // Act
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(existingUser));
        when(userRepository.save(any(User.class))).thenThrow(new RuntimeException("some exception"));

        // Assert
        assertThrows(InvalidEntityException.class, () -> userService.updateUser(existingUser));
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
    @DisplayName("createUser - Should throw DuplicateEntityException")
    void testCreateUser_ShouldThrowDuplicateEntityException_WhenUserExists() {
        // Arrange
        User user = new User();
        user.setId(1L);
        user.setEmail("abc@xyz.com");

        // Act & Assert
        when(userRepository.findUserByEmail(anyString())).thenReturn(Optional.of(user));

        assertThrows(DuplicateEntityException.class, () -> userService.createUser(user));

        verify(userRepository, times(1)).findUserByEmail(anyString());
        verify(userRepository, never()).save(any(User.class));
        verify(userDtoConverter, never()).convert(any(User.class));
    }

    @Test
    @DisplayName("createUser - Should throw InvalidEntityException")
    void testCreateUser_ShouldThrowInvalidEntityException_WhenError() {
        // Arrange
        User user = new User();
        user.setId(1L);
        user.setEmail("abc@xyz.com");

        // Act & Assert
        when(userRepository.findUserByEmail(anyString())).thenReturn(Optional.empty());
        when(userRepository.save(any(User.class))).thenThrow(new RuntimeException("some exception"));

        assertThrows(InvalidEntityException.class, () -> userService.createUser(user));

        verify(userRepository, times(1)).findUserByEmail(anyString());
        verify(userRepository, times(1)).save(any(User.class));
        verify(userDtoConverter, never()).convert(any(User.class));
    }

    @Test
    @DisplayName("deleteUser - Should return void")
    void testDeleteUser_ShouldDeleteUser_WhenNoErrors() {
        // Arrange
        when(userRepository.existsById(anyLong())).thenReturn(true);

        // Act
        userService.deleteUser(1L);

        // Assert
        verify(userRepository, times(1)).existsById(1L);
        verify(userRepository, times(1)).deleteById(1L);
    }

    @Test
    @DisplayName("deleteUser - Should throw EntityNotFoundException")
    void testDeleteUser_ShouldThrowEntityNotFoundException_WhenUserNotFound() {
        // Arrange
        when(userRepository.existsById(anyLong())).thenReturn(false);

        // Act & Assert
        assertThrows(EntityNotFoundException.class, () -> userService.deleteUser(1L));

        verify(userRepository, times(1)).existsById(1L);
        verify(userRepository, never()).deleteById(1L);
    }
}