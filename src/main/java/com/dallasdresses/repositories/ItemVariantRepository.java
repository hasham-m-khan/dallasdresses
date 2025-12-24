package com.dallasdresses.repositories;

import com.dallasdresses.entities.ItemVariant;
import com.dallasdresses.entities.enums.DressSize;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ItemVariantRepository extends JpaRepository<ItemVariant, Long> {

    @Query("SELECT iv FROM ItemVariant iv LEFT JOIN FETCH iv.item")
    Page<ItemVariant> findAllWithItem(Pageable pageable);

    @Query("SELECT iv FROM ItemVariant iv WHERE iv.item.id  = :itemId")
    Page<ItemVariant> getItemVariantsByItemId(@Param("itemId") Long itemId, Pageable pageable);

    @Query("SELECT iv FROM ItemVariant iv LEFT JOIN FETCH iv.item WHERE iv.id = :id")
    Optional<ItemVariant> findByIdWithItem(@Param("id") Long id);

    Boolean existsByItemIdAndColorAndSize(Long itemId, String color, DressSize size);
    boolean existsByItemIdAndColorAndSizeAndIdNot(Long itemId, String color, DressSize size, Long id);
}
