package com.dallasdresses.services;

import com.dallasdresses.dtos.request.ItemRatingCreateRequest;
import com.dallasdresses.dtos.request.ItemRatingUpdateRequest;
import com.dallasdresses.dtos.response.ItemRatingDto;
import com.dallasdresses.dtos.response.RatingBreakdownDto;

import java.util.List;

public interface ItemRatingService {
    List<ItemRatingDto> getRatingsByItemId(Long itemId);
    RatingBreakdownDto getRatingBreakdown(Long itemId);
    ItemRatingDto createRating(ItemRatingCreateRequest request);
    ItemRatingDto updateRating(ItemRatingUpdateRequest request);
    void deleteRating(Long ratingId, Long userId);
}
