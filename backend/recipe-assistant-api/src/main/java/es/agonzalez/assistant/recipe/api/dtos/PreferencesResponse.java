package es.agonzalez.assistant.recipe.api.dtos;

import java.util.Set;
import java.util.UUID;

import es.agonzalez.assistant.recipe.api.models.Diet;

public record PreferencesResponse(UUID id, Diet diet, Set<String> excludes, Set<String> allergns) { }
