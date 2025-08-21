package es.agonzalez.assistant.recipe.es.agonzalez.assistant.recipe.api.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record IngredientRequest(@NotBlank @Size(max=120) String name, @Size(max=80) String quantity) {}
