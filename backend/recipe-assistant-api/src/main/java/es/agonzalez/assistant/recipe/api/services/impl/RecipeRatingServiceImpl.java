package es.agonzalez.assistant.recipe.api.services.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import es.agonzalez.assistant.recipe.api.dto.CreateRecipeRatingRequest;
import es.agonzalez.assistant.recipe.api.dto.RecipeRatingResponse;
import es.agonzalez.assistant.recipe.api.dto.RecipeRatingStats;
import es.agonzalez.assistant.recipe.api.exceptions.ResourceNotFoundException;
import es.agonzalez.assistant.recipe.api.exceptions.UnauthorizedException;
import es.agonzalez.assistant.recipe.api.models.Recipe;
import es.agonzalez.assistant.recipe.api.models.RecipeRating;
import es.agonzalez.assistant.recipe.api.repositories.RecipeRatingRepository;
import es.agonzalez.assistant.recipe.api.repositories.RecipeRepository;
import es.agonzalez.assistant.recipe.api.services.RecipeRatingService;

@Service
@Transactional
public class RecipeRatingServiceImpl implements RecipeRatingService {

    private final RecipeRatingRepository ratingRepository;
    private final RecipeRepository recipeRepository;

    public RecipeRatingServiceImpl(RecipeRatingRepository ratingRepository, RecipeRepository recipeRepository) {
        this.ratingRepository = ratingRepository;
        this.recipeRepository = recipeRepository;
    }

    @Override
    public RecipeRatingResponse createRating(CreateRecipeRatingRequest request) {
        System.out.println("Creating rating for recipe: " + request.recipeId() + " by user: " + request.userIdentifier());

        // Validate recipe exists
        Recipe recipe = recipeRepository.findById(request.recipeId())
                .orElseThrow(() -> new ResourceNotFoundException("Recipe not found with id: " + request.recipeId()));

        // Check if user has already rated this recipe
        String userIdentifier = request.userIdentifier() != null ? request.userIdentifier() : "anonymous";
        Optional<RecipeRating> existingRating = ratingRepository.findByRecipeIdAndUserIdentifier(
                request.recipeId(), userIdentifier);

        if (existingRating.isPresent()) {
            throw new IllegalStateException("User has already rated this recipe. Use update instead.");
        }

        // Create new rating
        RecipeRating rating = RecipeRating.builder()
                .recipe(recipe)
                .rating(request.rating())
                .comment(request.comment())
                .userIdentifier(userIdentifier)
                .isAnonymous(request.userIdentifier() == null)
                .build();

        RecipeRating savedRating = ratingRepository.save(rating);
        System.out.println("Created rating " + savedRating.getId() + " for recipe " + request.recipeId() + " by user " + userIdentifier);

        return toDto(savedRating);
    }

    @Override
    public RecipeRatingResponse updateRating(UUID ratingId, CreateRecipeRatingRequest request, String userIdentifier) {
        System.out.println("Updating rating: " + ratingId + " by user: " + userIdentifier);

        RecipeRating rating = ratingRepository.findById(ratingId)
                .orElseThrow(() -> new ResourceNotFoundException("Rating not found with id: " + ratingId));

        // Check authorization
        String actualUserIdentifier = userIdentifier != null ? userIdentifier : "anonymous";
        if (!rating.getUserIdentifier().equals(actualUserIdentifier)) {
            throw new UnauthorizedException("User not authorized to update this rating");
        }

        // Update rating fields
        rating.setRating(request.rating());
        rating.setComment(request.comment());

        RecipeRating updatedRating = ratingRepository.save(rating);
        System.out.println("Updated rating " + ratingId + " for recipe " + rating.getRecipe().getId());

        return toDto(updatedRating);
    }

    @Override
    public void deleteRating(UUID ratingId, String userIdentifier) {
        System.out.println("Deleting rating: " + ratingId + " by user: " + userIdentifier);

        RecipeRating rating = ratingRepository.findById(ratingId)
                .orElseThrow(() -> new ResourceNotFoundException("Rating not found with id: " + ratingId));

        // Check authorization
        String actualUserIdentifier = userIdentifier != null ? userIdentifier : "anonymous";
        if (!rating.getUserIdentifier().equals(actualUserIdentifier)) {
            throw new UnauthorizedException("User not authorized to delete this rating");
        }

        ratingRepository.delete(rating);
        System.out.println("Deleted rating " + ratingId + " for recipe " + rating.getRecipe().getId());
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<RecipeRatingResponse> getRatingById(UUID ratingId) {
        return ratingRepository.findById(ratingId)
                .map(this::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<RecipeRatingResponse> getRatingsByRecipeId(UUID recipeId, Pageable pageable) {
        return ratingRepository.findByRecipeIdOrderByCreatedAtDesc(recipeId, pageable)
                .map(this::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public RecipeRatingStats getRatingStats(UUID recipeId) {
        // Get rating distribution
        List<Object[]> distribution = ratingRepository.getRatingDistributionByRecipeId(recipeId);
        
        // Initialize counts
        Map<Integer, Long> ratingCounts = new HashMap<>();
        for (int i = 1; i <= 5; i++) {
            ratingCounts.put(i, 0L);
        }
        
        Long totalRatings = 0L;
        Double totalScore = 0.0;
        
        // Process distribution results
        for (Object[] row : distribution) {
            Integer rating = (Integer) row[0];
            Long count = (Long) row[1];
            ratingCounts.put(rating, count);
            totalRatings += count;
            totalScore += rating * count;
        }
        
        Double averageRating = totalRatings > 0 ? totalScore / totalRatings : 0.0;
        
        return new RecipeRatingStats(
                averageRating,
                totalRatings,
                ratingCounts.get(5),
                ratingCounts.get(4),
                ratingCounts.get(3),
                ratingCounts.get(2),
                ratingCounts.get(1)
        );
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<RecipeRatingResponse> getUserRatingForRecipe(UUID recipeId, String userIdentifier) {
        String actualUserIdentifier = userIdentifier != null ? userIdentifier : "anonymous";
        return ratingRepository.findByRecipeIdAndUserIdentifier(recipeId, actualUserIdentifier)
                .map(this::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean hasUserRatedRecipe(UUID recipeId, String userIdentifier) {
        String actualUserIdentifier = userIdentifier != null ? userIdentifier : "anonymous";
        return ratingRepository.existsByRecipeIdAndUserIdentifier(recipeId, actualUserIdentifier);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<RecipeRatingResponse> getRecentRatingsWithComments(Pageable pageable) {
        return ratingRepository.findRecentRatingsWithComments(pageable)
                .map(this::toDto);
    }

    @Override
    public RecipeRatingResponse toDto(RecipeRating rating) {
        return new RecipeRatingResponse(
                rating.getId(),
                rating.getRecipe().getId(),
                rating.getRating(),
                rating.getComment(),
                rating.getUserIdentifier(),
                rating.isAnonymous(),
                rating.getCreatedAt().atZone(java.time.ZoneId.systemDefault()).toLocalDateTime(),
                rating.getUpdatedAt().atZone(java.time.ZoneId.systemDefault()).toLocalDateTime()
        );
    }
}
