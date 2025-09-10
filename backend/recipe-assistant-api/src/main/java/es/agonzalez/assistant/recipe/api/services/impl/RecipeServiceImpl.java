package es.agonzalez.assistant.recipe.api.services.impl;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import es.agonzalez.assistant.recipe.api.dtos.IngredientResponse;
import es.agonzalez.assistant.recipe.api.dtos.RecipeCreateRequest;
import es.agonzalez.assistant.recipe.api.dtos.RecipeResponse;
import es.agonzalez.assistant.recipe.api.models.Ingredient;
import es.agonzalez.assistant.recipe.api.models.Recipe;
import es.agonzalez.assistant.recipe.api.models.RecipeIngredient;
import es.agonzalez.assistant.recipe.api.repositories.IngredientRepository;
import es.agonzalez.assistant.recipe.api.repositories.RecipeRepository;
import es.agonzalez.assistant.recipe.api.services.RecipeService;
import io.micrometer.common.util.StringUtils;

@Service
public class RecipeServiceImpl implements RecipeService {

    @Autowired
    private  RecipeRepository recipeRepository;
    @Autowired
    private IngredientRepository ingredientRepository;

    @Override
    public Page<RecipeResponse> search(String q, Pageable pageable) {
        Page<Recipe> page;
        if(StringUtils.isEmpty(q)) {
            page = recipeRepository.findAll(pageable);
        }else {
            List<Recipe> recipes = recipeRepository.searchByTitleOrTag(q);
            int start = (int)pageable.getOffset();
            int end = Math.min(start + pageable.getPageSize(), recipes.size());
            List<Recipe> content = (start <= end) ? recipes.subList(start, end) : List.of();
            page = new PageImpl<>(content, pageable, recipes.size());
        }

        return page.map(this::toResponse); // Placeholder return
    }

    @Override
    public RecipeResponse get(UUID id) {
        Recipe r = recipeRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Recipe not found with id: " + id));
        return toResponse(r);
    }

    @Override
    @Transactional
    public RecipeResponse create(RecipeCreateRequest recipeCreateRequest) {
        

        Recipe recipe = new Recipe();
            recipe.setTitle(recipeCreateRequest.title());
            recipe.setSummary(recipeCreateRequest.summary());
            recipe.setSteps(recipeCreateRequest.steps());
            recipe.setTags(Optional.ofNullable(recipeCreateRequest.tags()).orElseGet(Set::of).stream().map(String::trim).filter(s -> !s.isBlank()).collect(Collectors.toSet()));

        if(recipeCreateRequest.ingredients() != null){
            for(var line: recipeCreateRequest.ingredients()) {
                Ingredient ing = ingredientRepository.findByNameIgnoreCase(line.name().trim())
                .orElseGet(() -> {
                    Ingredient i = new Ingredient();
                    i.setName(line.name().trim());
                    return ingredientRepository.save(i);
                });

                RecipeIngredient ri = new RecipeIngredient();
                ri.setIngredient(ing);
                ri.setQuantity(line.quantity());


                recipe.addIngredient(ri);
            }
        }


        return toResponse(recipeRepository.save(recipe)); // Placeholder return
    }
    
    private RecipeResponse toResponse(Recipe recipe) {
       var lines = recipe.getIngredients().stream().map(ri -> new IngredientResponse(ri.getIngredient().getId(), ri.getIngredient().getName(), ri.getQuantity())).toList();
        return new RecipeResponse(
            recipe.getId(),
            recipe.getTitle(),
            recipe.getSummary(),
            recipe.getSteps(),
            recipe.getTags(),
            lines
        );
    }   
}
