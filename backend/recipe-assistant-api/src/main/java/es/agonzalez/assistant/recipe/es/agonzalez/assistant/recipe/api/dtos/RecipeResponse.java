package es.agonzalez.assistant.recipe.es.agonzalez.assistant.recipe.api.dtos;

import java.util.List;
import java.util.Set;
import java.util.UUID;


public record RecipeResponse(UUID id, String title, String summary, String steps, Set<String> tags, List<IngredientResponse> ingredients) {}
