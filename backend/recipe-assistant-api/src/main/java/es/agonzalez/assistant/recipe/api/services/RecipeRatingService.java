package es.agonzalez.assistant.recipe.api.services;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import es.agonzalez.assistant.recipe.api.dto.CreateRecipeRatingRequest;
import es.agonzalez.assistant.recipe.api.dto.RecipeRatingResponse;
import es.agonzalez.assistant.recipe.api.dto.RecipeRatingStats;
import es.agonzalez.assistant.recipe.api.models.RecipeRating;

/**
 * Service interface for recipe rating operations
 */
public interface RecipeRatingService {

    /**
     * Create a new rating for a recipe
     */
    RecipeRatingResponse createRating(CreateRecipeRatingRequest request);

    /**
     * Update an existing rating
     */
    RecipeRatingResponse updateRating(UUID ratingId, CreateRecipeRatingRequest request, String userIdentifier);

    /**
     * Delete a rating by ID
     */
    void deleteRating(UUID ratingId, String userIdentifier);

    /**
     * Get a rating by ID
     */
    Optional<RecipeRatingResponse> getRatingById(UUID ratingId);

    /**
     * Get all ratings for a recipe with pagination
     */
    Page<RecipeRatingResponse> getRatingsByRecipeId(UUID recipeId, Pageable pageable);

    /**
     * Get rating statistics for a recipe
     */
    RecipeRatingStats getRatingStats(UUID recipeId);

    /**
     * Get user's rating for a specific recipe
     */
    Optional<RecipeRatingResponse> getUserRatingForRecipe(UUID recipeId, String userIdentifier);

    /**
     * Check if user has rated a recipe
     */
    boolean hasUserRatedRecipe(UUID recipeId, String userIdentifier);

    /**
     * Get recent ratings with comments
     */
    Page<RecipeRatingResponse> getRecentRatingsWithComments(Pageable pageable);

    /**
     * Convert RecipeRating entity to DTO
     */
    RecipeRatingResponse toDto(RecipeRating rating);
}
