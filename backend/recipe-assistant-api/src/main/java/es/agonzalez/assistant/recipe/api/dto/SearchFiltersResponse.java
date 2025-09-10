package es.agonzalez.assistant.recipe.api.dto;

import java.util.List;

/**
 * DTO for search filter options
 */
public record SearchFiltersResponse(
        List<String> availableIngredients,
        List<String> availableDiets,
        List<String> availableAllergens,
        Integer minCookingTime,
        Integer maxCookingTime,
        Integer minServings,
        Integer maxServings
) {
}
