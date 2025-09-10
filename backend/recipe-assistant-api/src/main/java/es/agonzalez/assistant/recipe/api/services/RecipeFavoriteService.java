package es.agonzalez.assistant.recipe.api.services;

import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import es.agonzalez.assistant.recipe.api.dto.CreateRecipeFavoriteRequest;
import es.agonzalez.assistant.recipe.api.dto.RecipeFavoriteResponse;

/**
 * Service interface for recipe favorites operations
 */
public interface RecipeFavoriteService {

    /**
     * Add a recipe to user's favorites
     */
    RecipeFavoriteResponse addFavorite(CreateRecipeFavoriteRequest request);

    /**
     * Remove a recipe from user's favorites
     */
    void removeFavorite(UUID recipeId, String userIdentifier);

    /**
     * Get all favorites for a user with pagination
     */
    Page<RecipeFavoriteResponse> getUserFavorites(String userIdentifier, Pageable pageable);

    /**
     * Check if a recipe is favorited by a user
     */
    boolean isFavorited(UUID recipeId, String userIdentifier);

    /**
     * Get favorite count for a recipe
     */
    long getFavoriteCount(UUID recipeId);

    /**
     * Get total favorites count for a user
     */
    long getUserFavoriteCount(String userIdentifier);
}
