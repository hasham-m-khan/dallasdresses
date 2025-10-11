package com.dallasdresses.repositories;

import com.dallasdresses.entities.CartItem;
import com.dallasdresses.entities.CartItemId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartItemRepository extends JpaRepository<CartItem, CartItemId> {
}
