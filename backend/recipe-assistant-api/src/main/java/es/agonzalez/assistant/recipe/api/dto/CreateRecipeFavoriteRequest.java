package es.agonzalez.assistant.recipe.api.dto;

import java.util.UUID;

import jakarta.validation.constraints.NotNull;

/**
 * DTO for creating a recipe favorite
 */
public record CreateRecipeFavoriteRequest(
        @NotNull UUID recipeId,
        String userIdentifier
) {
}
