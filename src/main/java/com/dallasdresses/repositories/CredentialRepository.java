package com.dallasdresses.repositories;

import com.dallasdresses.entities.Credential;
import com.dallasdresses.entities.CredentialId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CredentialRepository extends JpaRepository<Credential, CredentialId> {
}
