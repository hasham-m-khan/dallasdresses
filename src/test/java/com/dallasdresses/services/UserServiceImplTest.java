package com.dallasdresses.services;

import com.dallasdresses.converters.UserToUserDtoConverter;
import com.dallasdresses.dtos.request.AddressEmbedRequest;
import com.dallasdresses.dtos.request.UserCreateRequest;
import com.dallasdresses.dtos.request.UserUpdateRequest;
import com.dallasdresses.dtos.response.UserDto;
import com.dallasdresses.entities.User;
import com.dallasdresses.entities.enums.AddressType;
import com.dallasdresses.entities.enums.UserRole;
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
import java.util.Arrays;
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
        UserUpdateRequest request = UserUpdateRequest.builder()
                .id(1L)
                .email("abc@xyz.com")
                .role(UserRole.STAFF)
                .locale("fr")
                .build();

        User existingUser = User.builder()
                .id(1L)
                .email("abc@xyz.com")
                .role(UserRole.STAFF)
                .locale("en")
                .build();

        User updatedUser = User.builder()
                .id(1L)
                .email("abc@xyz.com")
                .role(UserRole.STAFF)
                .locale("fr")
                .build();

        UserDto updatedUserDto = UserDto.builder()
                .id(1L)
                .email("abc@xyz.com")
                .role(UserRole.STAFF)
                .locale("fr")
                .build();

        when(userRepository.findById(request.getId())).thenReturn(Optional.of(existingUser));
        when(userRepository.save(existingUser)).thenReturn(updatedUser);
        when(userDtoConverter.convert(updatedUser)).thenReturn(updatedUserDto);

        // Act
        UserDto result = userService.updateUser(request);

        // Assert
        assertEquals(request.getLocale(), result.getLocale());
        assertEquals(request.getEmail(), result.getEmail());
        assertEquals(request.getId(), result.getId());

        verify(userRepository, times(1)).findById(1L);
        verify(userRepository, times(1)).save(updatedUser);
        verify(userDtoConverter, times(1)).convert(any(User.class));
    }

    @Test
    @DisplayName("updateUser - Should throw EntityNotFoundException")
    void testUpdateUser_ShouldThrowEntityNotFoundException_WhenUserNotFound() {
        // Arrange
        UserUpdateRequest request = UserUpdateRequest.builder()
                .id(1L)
                .email("abc@xyz.com")
                .role(UserRole.STAFF)
                .locale("fr")
                .build();

        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(EntityNotFoundException.class, () -> userService.updateUser(request));

        verify(userRepository, times(1)).findById(1L);
        verify(userRepository, never()).save(any(User.class));
        verify(userDtoConverter, never()).convert(any(User.class));
    }

    @Test
    @DisplayName("updateUser - Should throw DuplicateEntityException")
    void testUpdateUser_ShouldThrowDuplicateEntityException_WhenDifferentUserWithSameEmailFound() {
        // Arrange
        UserUpdateRequest request = UserUpdateRequest.builder()
                .id(1L)
                .email("def@xyz.com")
                .role(UserRole.STAFF)
                .locale("fr")
                .build();

        User existingUser = User.builder()
                .id(1L)
                .email("abc@xyz.com")
                .role(UserRole.STAFF)
                .locale("en")
                .build();

        User diffUserWithSameEmail = User.builder()
                .id(3L)
                .email("def@xyz.com")
                .role(UserRole.STAFF)
                .locale("en")
                .build();

        when(userRepository.findById(request.getId())).thenReturn(Optional.of(existingUser));
        when(userRepository.findUserByEmail(request.getEmail()))
                .thenReturn(Optional.of(diffUserWithSameEmail));

        // Act & Assert
        assertThrows(DuplicateEntityException.class, () -> userService.updateUser(request));
        verify(userRepository, times(1)).findById(request.getId());
        verify(userRepository, times(1)).findUserByEmail(request.getEmail());
        verify(userRepository, never()).save(any(User.class));
        verify(userDtoConverter, never()).convert(any(User.class));
    }

    @Test
    @DisplayName("updateUser - Should throw InvalidEntityException")
    void testUpdateUser_ShouldThrowInvalidEntityException_WhenUserCreationException() {
        // Arrange
        UserUpdateRequest request = UserUpdateRequest.builder()
                .id(1L)
                .email("abc@xyz.com")
                .role(UserRole.STAFF)
                .locale("fr")
                .build();

        User existingUser = User.builder()
                .id(1L)
                .email("abc@xyz.com")
                .role(UserRole.STAFF)
                .locale("en")
                .build();

        User updatedUser = User.builder()
                .id(1L)
                .email("abc@xyz.com")
                .role(UserRole.STAFF)
                .locale("en")
                .build();

        when(userRepository.findById(1L)).thenReturn(Optional.of(existingUser));
        when(userRepository.save(updatedUser)).thenThrow(new RuntimeException("some error"));

        // Act & Assert
        assertThrows(InvalidEntityException.class, () -> userService.updateUser(request));

        verify(userRepository, times(1)).findById(1L);
        verify(userRepository, times(1)).save(updatedUser);
        verify(userDtoConverter, never()).convert(any(User.class));
    }

    @Test
    @DisplayName("createUser - Should create User Without Addresses")
    void testCreateUser_ShouldCreateUserWithoutAddresses_WhenNoErrors() {
        // Arrange
        UserCreateRequest request = UserCreateRequest.builder()
                .role(UserRole.USER)
                .email("suzanne.hutchkins@xyzmail.com")
                .locale("en")
                .build();

        User savedUser = User.builder()
                .id(5L)
                .role(UserRole.USER)
                .email("suzanne.hutchkins@xyzmail.com")
                .locale("en")
                .build();

        UserDto userDto = UserDto.builder()
                .id(5L)
                .role(UserRole.USER)
                .email("suzanne.hutchkins@xyzmail.com")
                .locale("en")
                .build();


        // Act
        when(userRepository.findUserByEmail(anyString())).thenReturn(Optional.empty());
        when(userRepository.save(any(User.class))).thenReturn(savedUser);
        when(userDtoConverter.convert(savedUser)).thenReturn(userDto);

        UserDto result = userService.createUser(request);

        // Assert
        assertEquals(userDto, result);
        verify(userRepository, times(1)).findUserByEmail(anyString());
        verify(userRepository, times(1)).save(any(User.class));
        verify(userDtoConverter, times(1)).convert(any(User.class));
    }

    @Test
    @DisplayName("createUser - Should create User With Addresses")
    void testCreateUser_ShouldCreateUserWithAddresses_WhenNoErrors() {
        // Arrange
        AddressEmbedRequest addRequest1 = AddressEmbedRequest.builder()
                .addressType(AddressType.RESIDENTIAL)
                .addressLine1("112 Maple ST")
                .city("Garling")
                .state("North Dakota")
                .country("usa")
                .postalCode("78945")
                .build();

        AddressEmbedRequest addRequest2 = AddressEmbedRequest.builder()
                .addressType(AddressType.ALTERNATE)
                .addressLine1("3009 Jungle Wood DR")
                .city("Jacksonville")
                .state("Florida")
                .country("usa")
                .postalCode("33456")
                .build();

        List<AddressEmbedRequest> addRequests = Arrays.asList(addRequest1, addRequest2);

        UserCreateRequest request = UserCreateRequest.builder()
                .role(UserRole.USER)
                .email("suzanne.hutchkins@xyzmail.com")
                .locale("en")
                .addresses(addRequests)
                .build();

        User savedUser = User.builder()
                .id(5L)
                .role(UserRole.USER)
                .email("suzanne.hutchkins@xyzmail.com")
                .locale("en")
                .build();

        UserDto userDto = UserDto.builder()
                .id(5L)
                .role(UserRole.USER)
                .email("suzanne.hutchkins@xyzmail.com")
                .locale("en")
                .build();

        when(userRepository.findUserByEmail(anyString())).thenReturn(Optional.empty());
        when(userRepository.save(any(User.class))).thenReturn(savedUser);
        when(userDtoConverter.convert(savedUser)).thenReturn(userDto);

        // Act
        UserDto result = userService.createUser(request);

        // Assert
        assertEquals(userDto, result);
        verify(userRepository, times(1)).findUserByEmail(anyString());
        verify(userRepository, times(1)).save(any(User.class));
        verify(userDtoConverter, times(1)).convert(any(User.class));
    }

    @Test
    @DisplayName("createUser - Should throw DuplicateEntityException")
    void testCreateUser_ShouldThrowDuplicateEntityException_WhenUserExists() {
        // Arrange
        UserCreateRequest request = UserCreateRequest.builder()
                .role(UserRole.USER)
                .email("suzanne.hutchkins@xyzmail.com")
                .locale("en")
                .build();

        User user = new User();
        user.setId(1L);
        user.setEmail("suzanne.hutchkins@xyzmail.com");
        user.setLocale("en");
        user.setRole(UserRole.USER);

        // Act & Assert
        when(userRepository.findUserByEmail(anyString())).thenReturn(Optional.of(user));

        assertThrows(DuplicateEntityException.class, () -> userService.createUser(request));

        verify(userRepository, times(1)).findUserByEmail(anyString());
        verify(userRepository, never()).save(any(User.class));
        verify(userDtoConverter, never()).convert(any(User.class));
    }

    @Test
    @DisplayName("createUser - Should throw InvalidEntityException")
    void testCreateUser_ShouldThrowInvalidEntityException_WhenError() {
        // Arrange
        UserCreateRequest request = UserCreateRequest.builder()
                .role(UserRole.USER)
                .email("suzanne.hutchkins@xyzmail.com")
                .locale("en")
                .build();

        User user = new User();
        user.setId(1L);
        user.setEmail("suzanne.hutchkins@xyzmail.com");
        user.setLocale("en");
        user.setRole(UserRole.USER);

        // Act & Assert
        when(userRepository.findUserByEmail(anyString())).thenReturn(Optional.empty());
        when(userRepository.save(any(User.class))).thenThrow(new RuntimeException("some exception"));

        assertThrows(InvalidEntityException.class, () -> userService.createUser(request));

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