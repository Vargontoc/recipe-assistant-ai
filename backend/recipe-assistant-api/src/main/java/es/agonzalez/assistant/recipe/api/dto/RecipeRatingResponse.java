package es.agonzalez.assistant.recipe.api.dto;

import java.time.LocalDateTime;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonFormat;

/**
 * DTO for recipe rating response
 */
public record RecipeRatingResponse(
        UUID id,
        UUID recipeId,
        Integer rating,
        String comment,
        String userIdentifier,
        boolean isAnonymous,
        
        @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
        LocalDateTime createdAt,
        
        @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
        LocalDateTime updatedAt
) {
}
