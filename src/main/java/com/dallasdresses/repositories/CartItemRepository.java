package com.dallasdresses.repositories;

import com.dallasdresses.entities.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CartItemRepository extends JpaRepository<CartItem, Long> {

    Optional<CartItem> findByCartIdAndItemId(Long cartId, Long itemId);
    boolean existsByCartIdAndItemId(Long cartId, Long itemId);
    void deleteByCartIdAndItemId(Long cartId, Long itemId);
    void deleteByItemId(Long itemId);
    void deleteByCartId(Long cartId);
}
