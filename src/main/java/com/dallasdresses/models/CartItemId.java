package com.dallasdresses.models;

import jakarta.persistence.Embeddable;
import lombok.Data;

import java.io.Serializable;

@Data
@Embeddable
public class CartItemId implements Serializable {

    private Long cartId;
    private Long itemId;
}
