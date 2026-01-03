package com.dallasdresses.entities;

import com.dallasdresses.entities.enums.DiscountType;
import com.dallasdresses.entities.enums.DressSize;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "items")
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Item {

    @Id
    @EqualsAndHashCode.Include
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    private String name;

    @Lob
    @NotBlank
    private String description;

    private String color;

    @NotNull
    @Enumerated(EnumType.STRING)
    private DressSize size;

    @NotNull
    @Min(0)
    private Integer stock;

    @NotNull
    private BigDecimal price;

    @Builder.Default
    @Column(nullable = false)
    private Double averageRating = 0.0;

    @Builder.Default
    @Column(nullable = false)
    private Integer totalRatings = 0;

    @Builder.Default
    @OneToMany(mappedBy = "item", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<ItemRating> ratings = new HashSet<>();

    @Enumerated(EnumType.STRING)
    private DiscountType discountType;

    @Min(0)
    private Double discountValue;

    @NotNull
    private Boolean isParent;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id")
    private Item parent;

    @OneToMany(mappedBy = "parent", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private Set<Item> children = new HashSet<>();

    @Builder.Default
    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(
            name = "item_category",
            joinColumns = @JoinColumn(name = "item_id"),
            inverseJoinColumns = @JoinColumn(name = "category_id")
    )
    private Set<Category> categories = new HashSet<>();

    @Builder.Default
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "item", orphanRemoval = true, fetch = FetchType.LAZY)
    private Set<ItemImage> itemImages = new HashSet<>();

    @CreationTimestamp
    private Timestamp createdAt;

    @UpdateTimestamp
    private Timestamp updatedAt;

    public void addImage(ItemImage itemImage) {
        itemImages.add(itemImage);
        itemImage.setItem(this);
    }

    public void addCategory(Category category) {
        categories.add(category);
        category.getItems().add(this);
    }

    public void removeCategory(Category category) {
        if (category != null) {
            categories.remove(category);
            if (category.getItems() != null) {
                category.getItems().remove(this);
            }
        }
    }

    public void addChild(Item child) {
        if (child != null && !child.equals(this)) {
            children.add(child);
            child.setParent(this);
        }
    }

    public void removeChild(Item child) {
        if (child != null) {
            children.remove(child);
            child.setParent(null);
        }
    }

    /**
     * Check if item is a parent
     */
    public boolean isParent() {
        return children != null && !children.isEmpty();
    }

    /**
     * Check if item is a child
     */
    public boolean isChild() {
        return parent != null;
    }

    /**
     * Add a rating and update aggregates.
     */
    public void addRating(ItemRating rating) {
        if (rating != null) {
            ratings.add(rating);
            rating.setItem(this);
            updateRatingAggregates();
        }
    }

    /**
     * Remove a rating and update aggregates.
     */
    public void removeRating(ItemRating rating) {
        if (rating != null) {
            ratings.remove(rating);
            rating.setItem(null);
            updateRatingAggregates();
        }
    }

    /**
     * Recalculates average rating and total count.
     * Call this after adding/removing/updating ratings.
     */
    public void updateRatingAggregates() {
        if (ratings == null || ratings.isEmpty()) {
            this.averageRating = 0.0;
            this.totalRatings = 0;
            return;
        }

        this.totalRatings = ratings.size();
        this.averageRating = ratings.stream()
                .mapToInt(ItemRating::getRating)
                .average()
                .orElse(0.0);
    }
}
