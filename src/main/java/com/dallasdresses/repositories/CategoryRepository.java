package com.dallasdresses.repositories;

import com.dallasdresses.entities.Category;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface CategoryRepository extends JpaRepository<Category, Long> {

    Optional<Category> findByName(String name);
    Optional<Category> findBySlug(String slug);
    boolean existsByNameIgnoreCase(String name);
    Integer deleteByName(String name);

    List<Category> findBySlugIn(Collection<String> slugs);
}
