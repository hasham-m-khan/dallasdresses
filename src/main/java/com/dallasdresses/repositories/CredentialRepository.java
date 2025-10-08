package com.dallasdresses.repositories;

import com.dallasdresses.models.Credential;
import com.dallasdresses.models.CredentialId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CredentialRepository extends JpaRepository<Credential, CredentialId> {
}
