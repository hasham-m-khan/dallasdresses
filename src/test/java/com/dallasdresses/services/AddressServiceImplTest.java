package com.dallasdresses.services;

import com.dallasdresses.converters.AddressToAddressDtoConverter;
import com.dallasdresses.dtos.response.AddressDto;
import com.dallasdresses.entities.Address;
import com.dallasdresses.entities.User;
import com.dallasdresses.entities.enums.enums.AddressType;
import com.dallasdresses.exceptions.DuplicateEntityException;
import com.dallasdresses.exceptions.EntityNotFoundException;
import com.dallasdresses.exceptions.EntityUpdateException;
import com.dallasdresses.exceptions.InvalidEntityException;
import com.dallasdresses.dtos.request.AddressCreateRequest;
import com.dallasdresses.dtos.request.AddressUpdateRequest;
import com.dallasdresses.repositories.AddressRepository;
import com.dallasdresses.repositories.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("Address Service Tests")
class AddressServiceImplTest {

    @Mock
    AddressRepository addressRepository;

    @Mock
    UserRepository userRepository;

    @Mock
    AddressToAddressDtoConverter addressDtoConverter;

    @InjectMocks
    AddressServiceImpl addressService;

    Address address1;
    Address address2;
    AddressDto addressDto1;
    AddressDto addressDto2;
    User user;
    AddressCreateRequest cr;
    AddressUpdateRequest ur;

    @BeforeEach
    void setUp() {
        address1 = new Address();
        address1.setId(1L);
        address1.setAddressType(AddressType.RESIDENTIAL);
        address1.setAddressLine1("123 Main ST");
        address1.setCity("Chicago");
        address1.setState("IL");
        address1.setCountry("USA");
        address1.setPostalCode("30301");

        address2 = new Address();
        address2.setId(2L);
        address2.setAddressType(AddressType.ALTERNATE);
        address2.setAddressLine1("456 Elm ST");
        address2.setCity("Queens");
        address2.setState("NY");
        address2.setCountry("USA");
        address2.setPostalCode("11428");

        addressDto1 = new AddressDto(1L, AddressType.RESIDENTIAL, "123 Main ST", null,
                "Chicago", "IL", "USA", "30301", null, null);
        addressDto2 = new AddressDto(2L, AddressType.ALTERNATE, "456 Elm ST", null,
                "Queens", "NY", "USA", "11428", null, null);

        user = new User();
        user.setId(3L);

        cr =  new AddressCreateRequest();
        cr.setUserId(user.getId());
        cr.setAddressLine1("123 Main ST");
        cr.setCity("Queens");
        cr.setState("NY");
        cr.setPostalCode("30301");

        ur = new AddressUpdateRequest();
        ur.setId(1L);
        ur.setUserId(user.getId());
        ur.setAddressType(AddressType.ALTERNATE);
        ur.setAddressLine1("Some New Street");
        ur.setCity("Queens");
        ur.setState("NY");
        ur.setCountry("USA");
        ur.setPostalCode("11428");
    }

    @Test
    @DisplayName("getAllAddresses - Should return all addresses")
    void testGetAllAddresses_ShouldReturnAllAddresses_WhenNoErrors() {
        // Arrange
        List<Address> addresses = new ArrayList<>();
        addresses.add(address1);
        addresses.add(address2);

        List<AddressDto> addressDtos = new ArrayList<>();
        addressDtos.add(addressDto1);
        addressDtos.add(addressDto2);

        // Act
        when(addressRepository.findAll()).thenReturn(addresses);
        when(addressDtoConverter.convert(address1)).thenReturn(addressDto1);
        when(addressDtoConverter.convert(address2)).thenReturn(addressDto2);

        // Assert
        assertEquals(addressDtos, addressService.getAllAddresses());
        verify(addressRepository, times(1)).findAll();
        verify(addressDtoConverter, times(2)).convert(any(Address.class));
    }

    @Test
    @DisplayName("getAddressByUserId - Should return user")
    void testGetAddressByUserId_ShouldReturnUser_WhenNoErrors() {
        // Arrange & Act
        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));
        when(addressRepository.findByUserId(anyLong()))
                .thenReturn(Optional.of(List.of(address1, address2)));
        when(addressDtoConverter.convert(address1)).thenReturn(addressDto1);
        when(addressDtoConverter.convert(address2)).thenReturn(addressDto2);

        // Assert
        assertEquals(2, addressService.getAddressByUserId(user.getId()).size());
        verify(userRepository, times(1)).findById(user.getId());
        verify(addressRepository, times(1)).findByUserId(anyLong());
        verify(addressDtoConverter, times(2)).convert(any(Address.class));
    }

    @Test
    @DisplayName("getAddressByUserId - Should throw EntityNotFoundException")
    void testGetAddressByUserId_ShouldThrowUserNotFoundException_WhenUserNotFound() {
        // Arrange & Act
        when(userRepository.findById(anyLong())).thenReturn(Optional.empty());

        // Assert
        assertThrows(EntityNotFoundException.class, () -> addressService.getAddressByUserId(anyLong()));
        verify(userRepository, times(1)).findById(anyLong());
        verify(addressRepository, never()).findByUserId(anyLong());
        verify(addressDtoConverter, never()).convert(any(Address.class));
    }

    @Test
    @DisplayName("getAddressByUserId - Should throw EntityNotFoundException")
    void testGetAddressByUserId_ShouldThrowEntityNotFoundException_WhenAddressNotFound() {
        // Arrange
        User user = new User();
        user.setId(3L);

        Optional<List<Address>> addresses = Optional.of(Collections.emptyList());

        // Act
        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));
        when(addressRepository.findByUserId(anyLong())).thenReturn(addresses);

        // Assert
        assertThrows(EntityNotFoundException.class, () -> addressService.getAddressByUserId(user.getId()));
        verify(userRepository, times(1)).findById(anyLong());
        verify(addressRepository, times(1)).findByUserId(anyLong());
        verify(addressDtoConverter, never()).convert(any(Address.class));
    }

    @Test
    @DisplayName("createAddress - Should create address")
    void testCreateAddress_ShouldCreateAddress_WhenNoErrors() {
        // Arrange & Act
        when(userRepository.findById(cr.getUserId())).thenReturn(Optional.of(user));
        when(addressRepository
                .findByUserIdAndAddressLine1AndCityAndStateAndPostalCode(
                        anyLong(), anyString(), anyString(), anyString(), anyString()))
                .thenReturn(Optional.empty());
        when(addressRepository.save(any(Address.class))).thenReturn(address1);
        when(addressDtoConverter.convert(address1)).thenReturn(addressDto1);

        // Assert
        assertEquals(addressDto1, addressService.createAddress(cr));
        verify(userRepository, times(1)).findById(anyLong());
        verify(addressRepository, times(1))
                .findByUserIdAndAddressLine1AndCityAndStateAndPostalCode(
                        anyLong(), anyString(), anyString(), anyString(), anyString());
        verify(addressRepository, times(1)).save(any(Address.class));
        verify(addressDtoConverter, times(1)).convert(address1);
    }

    @Test
    @DisplayName("createAddress - Should throw EntityNotFoundException")
    void testCreateAddress_ShouldThrowEntityNotFoundException_WhenUserNotFound() {
        // Arrange & Act
        when(userRepository.findById(anyLong())).thenReturn(Optional.empty());

        // Assert
        assertThrows(EntityNotFoundException.class, () -> addressService.createAddress(cr));
        verify(userRepository, times(1)).findById(anyLong());
        verify(addressRepository, never())
                .findByUserIdAndAddressLine1AndCityAndStateAndPostalCode(
                        anyLong(), anyString(), anyString(), anyString(), anyString());
        verify(addressRepository, never()).save(any(Address.class));
        verify(addressDtoConverter, never()).convert(address1);
    }

    @Test
    @DisplayName("createAddress - Should throw DuplicateEntityException")
    void testCreateAddress_ShouldThrowDuplicateEntityException_WhenDuplicateAddress() {
        // Arrange & Act
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));
        when(addressRepository
                .findByUserIdAndAddressLine1AndCityAndStateAndPostalCode(
                        anyLong(), anyString(), anyString(), anyString(), anyString()))
                .thenReturn(Optional.of(address1));

        // Assert
        assertThrows(DuplicateEntityException.class, () -> addressService.createAddress(cr));
        verify(userRepository, times(1)).findById(anyLong());
        verify(addressRepository, times(1))
                .findByUserIdAndAddressLine1AndCityAndStateAndPostalCode(
                        anyLong(), anyString(), anyString(), anyString(), anyString());
        verify(addressRepository, never()).save(any(Address.class));
        verify(addressDtoConverter, never()).convert(address1);
    }

    @Test
    @DisplayName("createAddress - Should throw InvalidEntityException")
    void testCreateAddress_ShouldThrowInvalidEntityException_WhenErrorDuringCreation() {
        // Arrange & Act
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));
        when(addressRepository
                .findByUserIdAndAddressLine1AndCityAndStateAndPostalCode(
                        anyLong(), anyString(), anyString(), anyString(), anyString()))
                .thenReturn(Optional.empty());
        when(addressRepository.save(any(Address.class))).thenThrow(new RuntimeException());

        // Assert
        assertThrows(InvalidEntityException.class, () -> addressService.createAddress(cr));
        verify(userRepository, times(1)).findById(anyLong());
        verify(addressRepository, times(1))
                .findByUserIdAndAddressLine1AndCityAndStateAndPostalCode(
                        anyLong(), anyString(), anyString(), anyString(), anyString());
        verify(addressRepository, times(1)).save(any(Address.class));
        verify(addressDtoConverter, never()).convert(address1);
    }

    @Test
    @DisplayName("updateAddress - Should update address")
    void testUpdateAddress_ShouldUpdateAddress_WhenNoErrors() {
        // Arrange
        address1.setUser(user);
        Address updatedAddress = new Address();
        updatedAddress.setId(1L);
        updatedAddress.setUser(user);
        updatedAddress.setAddressType(AddressType.ALTERNATE);
        updatedAddress.setAddressLine1("Some New Street");
        updatedAddress.setCity("Queens");
        updatedAddress.setState("NY");
        updatedAddress.setCountry("USA");
        updatedAddress.setPostalCode("11428");

        AddressDto updatedAddressDto = new AddressDto();
        updatedAddress.setId(1L);
        updatedAddress.setAddressType(AddressType.ALTERNATE);
        updatedAddress.setAddressLine1("Some New Street");
        updatedAddress.setCity("Queens");
        updatedAddress.setState("NY");
        updatedAddress.setCountry("USA");
        updatedAddress.setPostalCode("11428");

        // Act
        when(addressRepository.findById(anyLong())).thenReturn(Optional.of(address1));
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));
        when(addressRepository.save(any(Address.class))).thenReturn(updatedAddress);
        when(addressDtoConverter.convert(any(Address.class))).thenReturn(updatedAddressDto);

        // Assert
        assertEquals(updatedAddressDto, addressService.updateAddress(ur));
        verify(addressRepository, times(1)).findById(anyLong());
        verify(userRepository, times(1)).findById(anyLong());
        verify(addressRepository, times(1)).save(any(Address.class));
        verify(addressDtoConverter, times(1)).convert(any(Address.class));

    }

    @Test
    @DisplayName("updateAddress - Should throw EntityNotFoundException")
    void testUpdateAddress_ShouldThrowEntityNotFoundException_WhenAddressNotFound() {
        // Arrange
        when(addressRepository.findById(anyLong())).thenReturn(Optional.empty());

        // Act & Assert
        EntityNotFoundException exception = assertThrows(
                EntityNotFoundException.class,
                () -> addressService.updateAddress(ur));

        verify(addressRepository, times(1)).findById(anyLong());
        verify(userRepository, never()).findById(anyLong());
        verify(addressRepository, never()).save(any(Address.class));
        verify(addressDtoConverter, never()).convert(any(Address.class));
    }

    @Test
    @DisplayName("updateAddress - Should throw EntityNotFoundException")
    void testUpdateAddress_ShouldThrowEntityNotFoundException_WhenUserNotFound() {
        // Arrange
        when(addressRepository.findById(anyLong())).thenReturn(Optional.of(address1));
        when(userRepository.findById(anyLong())).thenReturn(Optional.empty());

        // Act & Assert
        EntityNotFoundException exception = assertThrows(
                EntityNotFoundException.class,
                () -> addressService.updateAddress(ur));

        verify(addressRepository, times(1)).findById(anyLong());
        verify(userRepository, times(1)).findById(anyLong());
        verify(addressRepository, never()).save(any(Address.class));
        verify(addressDtoConverter, never()).convert(any(Address.class));
    }

    @Test
    @DisplayName("updateAddress - Should throw EntityUpdateException")
    void testUpdateAddress_ShouldThrowEntityUpdateException_WhenAddressDoesNotBelongToUser() {
        // Arrange
        address1.setUser(user);
        User user2 = new User();
        user2.setId(99L);

        when(addressRepository.findById(anyLong())).thenReturn(Optional.of(address1));
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user2));

        // Act & Assert
        EntityUpdateException exception = assertThrows(
                EntityUpdateException.class,
                () -> addressService.updateAddress(ur));

        verify(addressRepository, times(1)).findById(anyLong());
        verify(userRepository, times(1)).findById(anyLong());
        verify(addressRepository, never()).save(any(Address.class));
        verify(addressDtoConverter, never()).convert(any(Address.class));
    }

    @Test
    @DisplayName("updateAddress - Should throw InvalidEntityException")
    void testUpdateAddress_ShouldThrowInvalidEntityException_WhenError() {
        // Arrange
        address1.setUser(user);

        when(addressRepository.findById(anyLong())).thenReturn(Optional.of(address1));
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));
        when(addressRepository.save(any(Address.class))).thenThrow(new RuntimeException());

        // Act & Assert
        InvalidEntityException exception = assertThrows(
                InvalidEntityException.class,
                () -> addressService.updateAddress(ur));

        verify(addressRepository, times(1)).findById(anyLong());
        verify(userRepository, times(1)).findById(anyLong());
        verify(addressRepository, times(1)).save(any(Address.class));
        verify(addressDtoConverter, never()).convert(any(Address.class));
    }

    @Test
    @DisplayName("deleteAddress - Should delete address")
    void deleteAddress() {
        // Arrange
        when(addressRepository.existsById(anyLong())).thenReturn(true);

        // Act
        addressService.deleteAddress(25L);

        // Assert
        verify(addressRepository, times(1)).existsById(anyLong());
        verify(addressRepository, times(1)).deleteById(anyLong());
    }

    @Test
    @DisplayName("deleteAddress - Should throw EntityNotFoundException")
    void deleteAddress_ShouldThrowEntityNotFoundException_WhenAddressNotFound() {
        // Arrange
        when(addressRepository.existsById(anyLong())).thenReturn(false);

        // Act & Assert
        assertThrows(EntityNotFoundException.class,
                () -> addressService.deleteAddress(25L));

        verify(addressRepository, times(1)).existsById(anyLong());
        verify(addressRepository, never()).deleteById(anyLong());
    }
}