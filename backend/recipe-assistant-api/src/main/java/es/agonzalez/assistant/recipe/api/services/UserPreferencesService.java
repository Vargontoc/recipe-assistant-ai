package es.agonzalez.assistant.recipe.api.services;

import es.agonzalez.assistant.recipe.api.dtos.PreferencesResponse;
import es.agonzalez.assistant.recipe.api.dtos.PreferencesUpdateRequest;

public interface UserPreferencesService {

    PreferencesResponse getOrCreate();

    PreferencesResponse update(PreferencesUpdateRequest request);
}
