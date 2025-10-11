package com.dallasdresses.entities;

import com.dallasdresses.entities.enums.enums.ProviderId;
import jakarta.persistence.Embeddable;
import lombok.Data;

import java.io.Serializable;

@Data
@Embeddable
public class CredentialId implements Serializable {

    private ProviderId providerId;
    private String providerKey;
}
