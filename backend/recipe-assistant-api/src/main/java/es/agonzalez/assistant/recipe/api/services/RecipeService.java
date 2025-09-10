package es.agonzalez.assistant.recipe.api.services;

import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import es.agonzalez.assistant.recipe.api.dto.AdvancedSearchRequest;
import es.agonzalez.assistant.recipe.api.dto.SearchFiltersResponse;
import es.agonzalez.assistant.recipe.api.dtos.RecipeCreateRequest;
import es.agonzalez.assistant.recipe.api.dtos.RecipeResponse;

public interface RecipeService {
    
    Page<RecipeResponse> search(String q, Pageable pageable);

    /**
     * Advanced search with multiple criteria
     */
    Page<RecipeResponse> advancedSearch(AdvancedSearchRequest request, Pageable pageable);

    /**
     * Get available filter options for search
     */
    SearchFiltersResponse getSearchFilters();

    RecipeResponse get(UUID id);
    
    RecipeResponse create(RecipeCreateRequest recipeCreateRequest);
}
