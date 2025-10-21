package com.dallasdresses.dtos.response;

import com.dallasdresses.entities.enums.enums.UserRole;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;
import java.util.HashSet;
import java.util.Set;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserDto {

    private Long id;
    private String email;
    private UserRole role;
    private String locale;
    private Boolean emailVerified;
    private String firstName;
    private String lastName;
    private String telephone;
    private String avatar;
    private Timestamp createdAt;
    private Timestamp updatedAt;

    private Set<AddressDto> addresses =  new HashSet<>();
}
