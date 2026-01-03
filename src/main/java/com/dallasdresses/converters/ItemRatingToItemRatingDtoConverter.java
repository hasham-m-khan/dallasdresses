package com.dallasdresses.converters;

import com.dallasdresses.dtos.response.ItemRatingDto;
import com.dallasdresses.entities.ItemRating;
import org.springframework.core.convert.converter.Converter;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

@Component
public class ItemRatingToItemRatingDtoConverter implements Converter<ItemRating, ItemRatingDto> {

    @Override
    public ItemRatingDto convert(@NonNull ItemRating source) {
        return ItemRatingDto.builder()
                .id(source.getId())
                .itemId(source.getItem().getId())
                .userId(source.getUser().getId())
                .userFirstName(source.getUser().getFirstName())
                .userLastName(source.getUser().getLastName())
                .rating(source.getRating())
                .title(source.getTitle())
                .reviewText(source.getReviewText())
                .verifiedPurchase(source.getVerifiedPurchase())
                .helpfulVotes(source.getHelpfulVotes())
                .createdAt(source.getCreatedAt())
                .updatedAt(source.getUpdatedAt())
                .build();
    }
}
