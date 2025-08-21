package es.agonzalez.assistant.recipe.es.agonzalez.assistant.recipe.api.services;

import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import es.agonzalez.assistant.recipe.es.agonzalez.assistant.recipe.api.dtos.RecipeCreateRequest;
import es.agonzalez.assistant.recipe.es.agonzalez.assistant.recipe.api.dtos.RecipeResponse;

public interface RecipeService {
    
    Page<RecipeResponse> search(String q, Pageable pageable);

    RecipeResponse get(UUID id);
    
    RecipeResponse create(RecipeCreateRequest recipeCreateRequest);
}
