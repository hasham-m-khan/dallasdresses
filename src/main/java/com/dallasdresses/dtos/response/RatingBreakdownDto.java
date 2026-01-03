package com.dallasdresses.dtos.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RatingBreakdownDto {
    private Double averageRating;
    private Integer totalRatings;
    private Integer fiveStars;
    private Integer fourStars;
    private Integer threeStars;
    private Integer twoStars;
    private Integer oneStar;
}
