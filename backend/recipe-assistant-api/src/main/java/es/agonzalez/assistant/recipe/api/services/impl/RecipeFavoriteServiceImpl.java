package es.agonzalez.assistant.recipe.api.services.impl;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import es.agonzalez.assistant.recipe.api.dto.CreateRecipeFavoriteRequest;
import es.agonzalez.assistant.recipe.api.dto.RecipeFavoriteResponse;
import es.agonzalez.assistant.recipe.api.exceptions.ResourceNotFoundException;
import es.agonzalez.assistant.recipe.api.models.Recipe;
import es.agonzalez.assistant.recipe.api.models.RecipeFavorite;
import es.agonzalez.assistant.recipe.api.repositories.RecipeFavoriteRepository;
import es.agonzalez.assistant.recipe.api.repositories.RecipeRepository;
import es.agonzalez.assistant.recipe.api.services.RecipeFavoriteService;
import es.agonzalez.assistant.recipe.api.services.RecipeService;

/**
 * Implementation of recipe favorites service
 */
@Service
@Transactional
public class RecipeFavoriteServiceImpl implements RecipeFavoriteService {

    private final RecipeFavoriteRepository favoriteRepository;
    private final RecipeRepository recipeRepository;
    private final RecipeService recipeService;

    public RecipeFavoriteServiceImpl(RecipeFavoriteRepository favoriteRepository, 
                                   RecipeRepository recipeRepository,
                                   RecipeService recipeService) {
        this.favoriteRepository = favoriteRepository;
        this.recipeRepository = recipeRepository;
        this.recipeService = recipeService;
    }

    @Override
    public RecipeFavoriteResponse addFavorite(CreateRecipeFavoriteRequest request) {
        // Validate recipe exists
        Recipe recipe = recipeRepository.findById(request.recipeId())
                .orElseThrow(() -> new ResourceNotFoundException("Recipe not found with id: " + request.recipeId()));

        String userIdentifier = request.userIdentifier() != null ? request.userIdentifier() : "anonymous";

        // Check if already favorited
        Optional<RecipeFavorite> existingFavorite = favoriteRepository
                .findByRecipeIdAndUserIdentifier(request.recipeId(), userIdentifier);

        if (existingFavorite.isPresent()) {
            throw new IllegalStateException("Recipe is already in user's favorites");
        }

        // Create new favorite
        RecipeFavorite favorite = RecipeFavorite.builder()
                .recipe(recipe)
                .userIdentifier(userIdentifier)
                .build();

        RecipeFavorite savedFavorite = favoriteRepository.save(favorite);
        return toDto(savedFavorite);
    }

    @Override
    public void removeFavorite(UUID recipeId, String userIdentifier) {
        String actualUserIdentifier = userIdentifier != null ? userIdentifier : "anonymous";
        
        RecipeFavorite favorite = favoriteRepository
                .findByRecipeIdAndUserIdentifier(recipeId, actualUserIdentifier)
                .orElseThrow(() -> new ResourceNotFoundException("Favorite not found"));

        favoriteRepository.delete(favorite);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<RecipeFavoriteResponse> getUserFavorites(String userIdentifier, Pageable pageable) {
        String actualUserIdentifier = userIdentifier != null ? userIdentifier : "anonymous";
        
        return favoriteRepository
                .findByUserIdentifierOrderByCreatedAtDesc(actualUserIdentifier, pageable)
                .map(this::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean isFavorited(UUID recipeId, String userIdentifier) {
        String actualUserIdentifier = userIdentifier != null ? userIdentifier : "anonymous";
        return favoriteRepository.existsByRecipeIdAndUserIdentifier(recipeId, actualUserIdentifier);
    }

    @Override
    @Transactional(readOnly = true)
    public long getFavoriteCount(UUID recipeId) {
        return favoriteRepository.countByRecipeId(recipeId);
    }

    @Override
    @Transactional(readOnly = true)
    public long getUserFavoriteCount(String userIdentifier) {
        String actualUserIdentifier = userIdentifier != null ? userIdentifier : "anonymous";
        return favoriteRepository.countByUserIdentifier(actualUserIdentifier);
    }

    private RecipeFavoriteResponse toDto(RecipeFavorite favorite) {
        return new RecipeFavoriteResponse(
                favorite.getId(),
                recipeService.get(favorite.getRecipe().getId()),
                favorite.getUserIdentifier(),
                favorite.getCreatedAt().atZone(java.time.ZoneId.systemDefault()).toLocalDateTime()
        );
    }
}
