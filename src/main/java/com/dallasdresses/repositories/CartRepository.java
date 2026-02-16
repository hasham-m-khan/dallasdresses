package com.dallasdresses.repositories;

import com.dallasdresses.entities.Cart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CartRepository extends JpaRepository<Cart,Long> {

    @Query("""
        SELECT c FROM Cart c
        LEFT JOIN FETCH c.items
        WHERE c.user.id = :userId
    """)
    Optional<Cart> findByUserIdWithItems(@Param("userId") Long userId);
    Optional<Cart> findByUserId(Long userId);
    boolean existsByUserId(Long userId);
    void deleteByUserId(Long userId);
}
