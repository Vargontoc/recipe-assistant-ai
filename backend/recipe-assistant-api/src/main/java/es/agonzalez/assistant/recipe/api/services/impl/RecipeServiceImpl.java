package es.agonzalez.assistant.recipe.api.services.impl;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import es.agonzalez.assistant.recipe.api.dto.AdvancedSearchRequest;
import es.agonzalez.assistant.recipe.api.dto.SearchFiltersResponse;
import es.agonzalez.assistant.recipe.api.dtos.IngredientResponse;
import es.agonzalez.assistant.recipe.api.dtos.RecipeCreateRequest;
import es.agonzalez.assistant.recipe.api.dtos.RecipeResponse;
import es.agonzalez.assistant.recipe.api.models.Ingredient;
import es.agonzalez.assistant.recipe.api.models.Recipe;
import es.agonzalez.assistant.recipe.api.models.RecipeIngredient;
import es.agonzalez.assistant.recipe.api.repositories.IngredientRepository;
import es.agonzalez.assistant.recipe.api.repositories.RecipeRepository;
import es.agonzalez.assistant.recipe.api.services.RecipeService;
import es.agonzalez.assistant.recipe.api.services.SearchHistoryService;
import io.micrometer.common.util.StringUtils;

@Service
public class RecipeServiceImpl implements RecipeService {

    @Autowired
    private  RecipeRepository recipeRepository;
    @Autowired
    private IngredientRepository ingredientRepository;
    @Autowired
    private SearchHistoryService searchHistoryService;

    @Override
    public Page<RecipeResponse> search(String q, Pageable pageable) {
        Page<Recipe> page;
        if (StringUtils.isEmpty(q)) {
            page = recipeRepository.findAll(pageable);
        } else {
            // Use optimized paginated search instead of manual pagination
            page = recipeRepository.searchByTitleOrTag(q, pageable);
        }
        
        return page.map(this::toResponse);
    }

    @Override
    @Cacheable(value = "recipes", key = "#id")
    public RecipeResponse get(UUID id) {
        Recipe r = recipeRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Recipe not found with id: " + id));
        return toResponse(r);
    }

    @Override
    @Transactional
    @CacheEvict(value = "recipes", allEntries = true)
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

    @Override
    @Transactional(readOnly = true)
    public Page<RecipeResponse> advancedSearch(AdvancedSearchRequest request, Pageable pageable) {
        // Create dynamic sort based on request
        Sort sort = createSortFromRequest(request);
        Pageable sortedPageable = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), sort);
        
        // Perform simplified advanced search (ignoring diet and time filters for now)
        Page<Recipe> recipes = recipeRepository.advancedSearch(
                request.query(),
                request.minRating(),
                sortedPageable
        );
        
        return recipes.map(this::toResponse);
    }

    @Override
    @Cacheable(value = "search-filters", cacheManager = "staticDataCacheManager")
    @Transactional(readOnly = true)
    public SearchFiltersResponse getSearchFilters() {
        List<String> availableIngredients = recipeRepository.findDistinctIngredients();
        List<String> availableDiets = recipeRepository.findDistinctDiets();
        
        // Get cooking time and servings ranges
        Object[] cookingTimeRange = recipeRepository.findCookingTimeRange();
        Object[] servingsRange = recipeRepository.findServingsRange();
        
        Integer minCookingTime = cookingTimeRange != null && cookingTimeRange[0] != null ? 
                ((Number) cookingTimeRange[0]).intValue() : 0;
        Integer maxCookingTime = cookingTimeRange != null && cookingTimeRange[1] != null ? 
                ((Number) cookingTimeRange[1]).intValue() : 120;
        Integer minServings = servingsRange != null && servingsRange[0] != null ? 
                ((Number) servingsRange[0]).intValue() : 1;
        Integer maxServings = servingsRange != null && servingsRange[1] != null ? 
                ((Number) servingsRange[1]).intValue() : 12;
        
        // For allergens, we'll use a predefined list (could be extended to be dynamic)
        List<String> availableAllergens = Arrays.asList(
                "gluten", "dairy", "eggs", "nuts", "soy", "shellfish", "fish", "sesame"
        );
        
        return new SearchFiltersResponse(
                availableIngredients,
                availableDiets,
                availableAllergens,
                minCookingTime,
                maxCookingTime,
                minServings,
                maxServings
        );
    }

    private Sort createSortFromRequest(AdvancedSearchRequest request) {
        String sortBy = request.sortBy() != null ? request.sortBy() : "createdAt";
        String direction = request.sortDirection() != null ? request.sortDirection() : "desc";
        
        // Map sort fields to actual entity fields
        String entityField = switch (sortBy) {
            case "title" -> "title";
            case "cookingTime" -> "cookingTime";
            case "rating" -> "createdAt"; // For now, we'll sort by createdAt for rating (complex calculation)
            case "createdAt" -> "createdAt";
            default -> "createdAt";
        };
        
        return "asc".equalsIgnoreCase(direction) ? 
                Sort.by(Sort.Direction.ASC, entityField) : 
                Sort.by(Sort.Direction.DESC, entityField);
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
