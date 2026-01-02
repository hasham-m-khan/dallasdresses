package com.dallasdresses.repositories;

import com.dallasdresses.entities.Item;
import com.dallasdresses.entities.enums.DiscountType;
import com.dallasdresses.entities.enums.DressSize;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

@Repository
public interface ItemRepository extends JpaRepository<Item, Long> {

    @Query("SELECT i FROM Item i LEFT JOIN FETCH i.categories")
    List<Item> findAllWithCategories();

    @Query("SELECT i FROM Item i JOIN i.categories c WHERE c.slug = :slug")
    List<Item> findItemsByCategorySlug(String slug);

    boolean existsByNameAndColorAndSize(String name, String color, DressSize size);
}
