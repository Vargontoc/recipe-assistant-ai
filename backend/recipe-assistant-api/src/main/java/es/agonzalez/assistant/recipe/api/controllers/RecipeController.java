package es.agonzalez.assistant.recipe.api.controllers;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import es.agonzalez.assistant.recipe.api.dtos.RecipeCreateRequest;
import es.agonzalez.assistant.recipe.api.dtos.RecipeResponse;
import es.agonzalez.assistant.recipe.api.dtos.SuggestRequest;
import es.agonzalez.assistant.recipe.api.dtos.SuggestResponse;
import es.agonzalez.assistant.recipe.api.services.RecipeService;
import es.agonzalez.assistant.recipe.api.services.SuggestionService;
import jakarta.validation.Valid;


@RestController
@RequestMapping("/api/recipes")
public class RecipeController {
    @Autowired
    private  RecipeService recipeService;
    @Autowired
    private SuggestionService suggestService;
    
    @GetMapping
    public Page<RecipeResponse> search(
        @RequestParam(required = false) String q,
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "10") int size) {
        return recipeService.search(q, PageRequest.of(Math.max(0, page), Math.min(100, Math.max(1, size))));
    }
    
    @GetMapping("/{id}")
    public RecipeResponse get(@PathVariable UUID id) {
        
        return recipeService.get(id);
    }

    @PostMapping
    public RecipeResponse create(@Valid @RequestBody RecipeCreateRequest recipeCreateRequest) {
        return recipeService.create(recipeCreateRequest);
    }

    @PostMapping("/suggest")
    public SuggestResponse suggest(@Valid @RequestBody SuggestRequest request) {
        return suggestService.suggest(request);
    }
}
