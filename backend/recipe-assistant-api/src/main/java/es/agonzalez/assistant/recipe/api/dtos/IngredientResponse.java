package es.agonzalez.assistant.recipe.api.dtos;

import java.util.UUID;

public record IngredientResponse(UUID id, String name, String quantity) {}
