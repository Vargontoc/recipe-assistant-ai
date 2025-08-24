package es.agonzalez.assistant.recipe.api.dtos;

import java.util.Set;

import es.agonzalez.assistant.recipe.api.models.Diet;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record PreferencesUpdateRequest(
    @NotNull Diet diet,
    Set<@Size(max=60)String> excludes,
    Set<@Size(max=60)String> allergns)  {}
