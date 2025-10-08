package com.dallasdresses.models;

import com.dallasdresses.models.Enums.ProviderId;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.Data;

import java.io.Serializable;

@Data
@Embeddable
public class CredentialId implements Serializable {

    private ProviderId providerId;
    private String providerKey;
}
