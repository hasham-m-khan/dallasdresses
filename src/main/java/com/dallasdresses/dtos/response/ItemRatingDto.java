package com.dallasdresses.dtos.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ItemRatingDto {

    private Long id;
    private Long itemId;
    private Long userId;
    private String userFirstName;
    private String userLastName;
    private Integer rating;
    private String title;
    private String reviewText;
    private Boolean verifiedPurchase;
    private Integer helpfulVotes;
    private Timestamp createdAt;
    private Timestamp updatedAt;
}
