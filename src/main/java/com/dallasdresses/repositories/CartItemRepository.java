package com.dallasdresses.repositories;

import com.dallasdresses.models.CartItem;
import com.dallasdresses.models.CartItemId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartItemRepository extends JpaRepository<CartItem, CartItemId> {
}
