package com.dallasdresses.services;

import com.dallasdresses.converters.ItemRatingToItemRatingDtoConverter;
import com.dallasdresses.dtos.request.ItemRatingCreateRequest;
import com.dallasdresses.dtos.request.ItemRatingUpdateRequest;
import com.dallasdresses.dtos.response.ItemRatingDto;
import com.dallasdresses.dtos.response.RatingBreakdownDto;
import com.dallasdresses.entities.Item;
import com.dallasdresses.entities.ItemRating;
import com.dallasdresses.entities.User;
import com.dallasdresses.exceptions.DuplicateEntityException;
import com.dallasdresses.exceptions.EntityNotFoundException;
import com.dallasdresses.exceptions.UnauthorizedException;
import com.dallasdresses.repositories.ItemRatingRepository;
import com.dallasdresses.repositories.ItemRepository;
import com.dallasdresses.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Service
@Transactional(readOnly = true)
public class ItemRatingServiceImpl implements ItemRatingService {

    private final ItemRatingRepository ratingRepository;
    private final ItemRepository itemRepository;
    private final UserRepository userRepository;
    private final ItemRatingToItemRatingDtoConverter ratingDtoConverter;

    public ItemRatingServiceImpl(ItemRatingRepository ratingRepository,
                                 ItemRepository itemRepository,
                                 UserRepository userRepository,
                                 ItemRatingToItemRatingDtoConverter ratingDtoConverter) {
        this.ratingRepository = ratingRepository;
        this.itemRepository = itemRepository;
        this.userRepository = userRepository;
        this.ratingDtoConverter = ratingDtoConverter;
    }

    @Override
    public List<ItemRatingDto> getRatingsByItemId(Long itemId) {
        // Verify item exists
        if (!itemRepository.existsById(itemId)) {
            throw new EntityNotFoundException("item", itemId);
        }

        List<ItemRating> ratings = ratingRepository.findByItemIdWithUser(itemId);

        return ratings.stream()
                .map(ratingDtoConverter::convert)
                .collect(Collectors.toList());
    }

    @Override
    public RatingBreakdownDto getRatingBreakdown(Long itemId) {
        // Verify item exists
        if (!itemRepository.existsById(itemId)) {
            throw new EntityNotFoundException("item", itemId);
        }

        List<Object[]> breakdown = ratingRepository.getRatingBreakdownByItemId(itemId);

        Map<Integer, Long> ratingCounts = new HashMap<>();
        for (Object[] row : breakdown) {
            Integer rating = (Integer) row[0];
            Long count = (Long) row[1];
            ratingCounts.put(rating, count);
        }

        Double avgRating = ratingRepository.getAverageRatingByItemId(itemId);
        long totalRatings = ratingRepository.countByItemId(itemId);

        return RatingBreakdownDto.builder()
                .averageRating(avgRating != null ? avgRating : 0.0)
                .totalRatings((int) totalRatings)
                .fiveStars(ratingCounts.getOrDefault(5, 0L).intValue())
                .fourStars(ratingCounts.getOrDefault(4, 0L).intValue())
                .threeStars(ratingCounts.getOrDefault(3, 0L).intValue())
                .twoStars(ratingCounts.getOrDefault(2, 0L).intValue())
                .oneStar(ratingCounts.getOrDefault(1, 0L).intValue())
                .build();
    }

    @Override
    public ItemRatingDto createRating(ItemRatingCreateRequest request) {
        // Validate item exists
        Item item = itemRepository.findById(request.getItemId())
                .orElseThrow(() -> new EntityNotFoundException("item", request.getItemId()));

        // Validate user exists
        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new EntityNotFoundException("user", request.getUserId()));

        // Check if user already rated this item
        if (ratingRepository.existsByItemIdAndUserId(request.getItemId(), request.getUserId())) {
            throw new DuplicateEntityException("User has already rated this item. Use update instead.");
        }

        // Create rating
        ItemRating rating = ItemRating.builder()
                .item(item)
                .user(user)
                .rating(request.getRating())
                .title(request.getTitle())
                .reviewText(request.getReviewText())
                .verifiedPurchase(false) // Set based on order history
                .helpfulVotes(0)
                .build();

        // Add rating to item (updates aggregates)
        item.addRating(rating);

        // Save
        ItemRating savedRating = ratingRepository.save(rating);
        itemRepository.save(item); // Save updated aggregates

        log.info("User {} rated item {} with {} stars",
                request.getUserId(), request.getItemId(), request.getRating());

        return ratingDtoConverter.convert(savedRating);
    }

    @Override
    public ItemRatingDto updateRating(ItemRatingUpdateRequest request) {
        // Get existing rating
        ItemRating existingRating = ratingRepository.findById(request.getId())
                .orElseThrow(() -> new EntityNotFoundException("rating", request.getId()));

        // Update fields
        existingRating.setRating(request.getRating());
        existingRating.setTitle(request.getTitle());
        existingRating.setReviewText(request.getReviewText());

        // Update item aggregates
        Item item = existingRating.getItem();
        item.updateRatingAggregates();

        // Save
        ItemRating savedRating = ratingRepository.save(existingRating);
        itemRepository.save(item);

        return ratingDtoConverter.convert(savedRating);
    }

    @Override
    public void deleteRating(Long ratingId, Long userId) {
        // Get rating
        ItemRating rating = ratingRepository.findById(ratingId)
                .orElseThrow(() -> new EntityNotFoundException("rating", ratingId));

        // Verify ownership
        if (!rating.getUser().getId().equals(userId)) {
            throw new UnauthorizedException("You can only delete your own ratings");
        }

        // Remove from item and update aggregates
        Item item = rating.getItem();
        item.removeRating(rating);

        // Delete rating
        ratingRepository.delete(rating);
        itemRepository.save(item);
    }
}
