package com.dallasdresses.dtos.request;

import com.dallasdresses.entities.enums.UserRole;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.URL;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserCreateRequest {

    @Email
    @NotBlank(message = "User email is required")
    private String email;

    @NotNull(message = "User role is required")
    private UserRole role;

    @NotBlank(message = "User locale is required")
    @Size(min = 2, max = 2)
    private String locale;

    @Size(min = 1, max = 50, message = "First name must be between 1 and 50 characters")
    private String firstName;

    @Size(min = 1, max = 50, message = "Last name must be between 1 and 50 characters")
    private String lastName;

    @Pattern(
            regexp = "^\\+?[1-9]\\d{1,14}$",
            message = "Invalid phone number format (E.164 format)"
    )
    private String telephone;

    @URL
    private String avatar;

    @Valid
    @Size(max = 3, message = "Maximum 3 addresses allowed during registration")
    private List<AddressEmbedRequest> addresses;
}
