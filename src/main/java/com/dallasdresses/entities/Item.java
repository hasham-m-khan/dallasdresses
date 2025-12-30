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
    private Integer quantity;

    @NotNull
    private BigDecimal price;

    @Enumerated(EnumType.STRING)
    private DiscountType discountType;

    @Min(0)
    private Double discountValue;

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

    public boolean isParent() {
        return children != null && !children.isEmpty();
    }

    public boolean isVariant() {
        return parent != null;
    }
}
