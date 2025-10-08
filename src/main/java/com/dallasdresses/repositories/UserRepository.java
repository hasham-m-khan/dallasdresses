package com.dallasdresses.repositories;

import com.dallasdresses.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}
