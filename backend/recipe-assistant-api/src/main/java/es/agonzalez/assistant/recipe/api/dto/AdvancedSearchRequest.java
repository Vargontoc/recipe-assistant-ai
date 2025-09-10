package es.agonzalez.assistant.recipe.api.dto;

import java.util.List;

/**
 * DTO for advanced recipe search request
 */
public record AdvancedSearchRequest(
        String query,
        List<String> includedIngredients,
        List<String> excludedIngredients,
        List<String> diets,
        List<String> allergens,
        Integer minCookingTime,
        Integer maxCookingTime,
        Integer minServings,
        Integer maxServings,
        Double minRating,
        String sortBy, // "title", "cookingTime", "rating", "createdAt"
        String sortDirection // "asc", "desc"
) {
    
    public static AdvancedSearchRequest simple(String query) {
        return new AdvancedSearchRequest(
                query, null, null, null, null,
                null, null, null, null, null,
                "rating", "desc"
        );
    }
}
