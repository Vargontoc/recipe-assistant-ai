package es.agonzalez.assistant.recipe.api.dtos;

import java.util.List;
import java.util.Set;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

public record RecipeCreateRequest(
    @NotBlank @Size(max=160) String tittle,
    @Size(max=500) String summary,
    String steps,
    Set<@Size(max=50)String> tags,
    @NotEmpty List<IngredientRequest> ingredients) {}
