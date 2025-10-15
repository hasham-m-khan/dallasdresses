package com.dallasdresses.repositories;

import com.dallasdresses.entities.User;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findUserByEmail(String email);

    @EntityGraph(attributePaths = {"addresses", "credentials"})
    @Query("SELECT u from User u")
    List<User> findAllWithAddresses();

    @EntityGraph(attributePaths = {"addresses", "credentials"})
    @Query("SELECT u FROM User u WHERE u.id = :id")
    Optional<User> findByIdWithAddresses(Long id);

    @EntityGraph(attributePaths = {"addresses", "credentials"})
    @Query("SELECT u FROM User u WHERE u.email = :email")
    Optional<User> findByEmailWithAddresses(String email);
}
