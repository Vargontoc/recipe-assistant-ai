package es.agonzalez.assistant.recipe.api.dto;

import java.time.LocalDateTime;
import java.util.UUID;

import es.agonzalez.assistant.recipe.api.dtos.RecipeResponse;

/**
 * DTO for recipe favorite response
 */
public record RecipeFavoriteResponse(
        UUID id,
        RecipeResponse recipe,
        String userIdentifier,
        LocalDateTime createdAt
) {
}
