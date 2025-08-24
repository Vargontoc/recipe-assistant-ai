package es.agonzalez.assistant.recipe.api.services;

import es.agonzalez.assistant.recipe.api.dtos.SuggestRequest;
import es.agonzalez.assistant.recipe.api.dtos.SuggestResponse;

public interface SuggestionService {
    SuggestResponse suggest(SuggestRequest req);
}
