package com.dallasdresses.entities;

import com.dallasdresses.entities.enums.Hasher;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
@Entity
public class Credential {

    @EmbeddedId
    private CredentialId id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @NotBlank
    private Hasher hasher;

    @NotBlank
    private String password_hash;

    @NotBlank
    private String passwordSalt;
}
