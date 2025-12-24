package com.dallasdresses.repositories;

import com.dallasdresses.entities.ItemImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;

@Repository
public interface ItemImageRepository extends JpaRepository<ItemImage, Long> {

    List<ItemImage> findByItemId(Long itemId);
    List<ItemImage> findByUrlIn(Collection<String> urls);
}
