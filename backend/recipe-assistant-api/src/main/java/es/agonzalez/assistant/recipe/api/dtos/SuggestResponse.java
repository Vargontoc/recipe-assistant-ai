package es.agonzalez.assistant.recipe.api.dtos;

import java.util.List;
import java.util.Set;

public record SuggestResponse(String title, String summary, List<String> steps, Set<String> tags) {}
