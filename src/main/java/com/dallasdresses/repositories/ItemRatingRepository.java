package com.dallasdresses.repositories;

import com.dallasdresses.entities.ItemRating;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ItemRatingRepository extends JpaRepository<ItemRating, Long> {

    /**
     * Find all ratings for an item.
     */
    @Query("SELECT r FROM ItemRating r " +
            "LEFT JOIN FETCH r.user " +
            "WHERE r.item.id = :itemId " +
            "ORDER BY r.createdAt DESC")
    List<ItemRating> findByItemIdWithUser(@Param("itemId") Long itemId);

    /**
     * Find all ratings by a user.
     */
    @Query("SELECT r FROM ItemRating r " +
            "LEFT JOIN FETCH r.item " +
            "WHERE r.user.id = :userId " +
            "ORDER BY r.createdAt DESC")
    List<ItemRating> findByUserIdWithItem(@Param("userId") Long userId);

    /**
     * Find a specific user's rating for an item.
     */
    Optional<ItemRating> findByItemIdAndUserId(Long itemId, Long userId);

    /**
     * Check if a user has already rated an item.
     */
    boolean existsByItemIdAndUserId(Long itemId, Long userId);

    /**
     * Get average rating for an item.
     */
    @Query("SELECT AVG(r.rating) FROM ItemRating r WHERE r.item.id = :itemId")
    Double getAverageRatingByItemId(@Param("itemId") Long itemId);

    /**
     * Count ratings for an item.
     */
    long countByItemId(Long itemId);

    /**
     * Get ratings by star level for an item (for rating breakdown).
     */
    @Query("SELECT r.rating, COUNT(r) FROM ItemRating r " +
            "WHERE r.item.id = :itemId " +
            "GROUP BY r.rating " +
            "ORDER BY r.rating DESC")
    List<Object[]> getRatingBreakdownByItemId(@Param("itemId") Long itemId);

    /**
     * Find top-rated items.
     */
    @Query("SELECT r.item.id, AVG(r.rating) as avgRating, COUNT(r) as ratingCount " +
            "FROM ItemRating r " +
            "GROUP BY r.item.id " +
            "HAVING COUNT(r) >= :minRatings " +
            "ORDER BY avgRating DESC")
    List<Object[]> findTopRatedItems(@Param("minRatings") int minRatings);
}
