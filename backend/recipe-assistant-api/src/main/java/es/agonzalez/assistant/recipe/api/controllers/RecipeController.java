package es.agonzalez.assistant.recipe.api.controllers;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;

import es.agonzalez.assistant.recipe.api.dto.AdvancedSearchRequest;
import es.agonzalez.assistant.recipe.api.dto.SearchFiltersResponse;
import es.agonzalez.assistant.recipe.api.dtos.RecipeCreateRequest;
import es.agonzalez.assistant.recipe.api.dtos.RecipeResponse;
import es.agonzalez.assistant.recipe.api.dtos.SuggestRequest;
import es.agonzalez.assistant.recipe.api.dtos.SuggestResponse;
import es.agonzalez.assistant.recipe.api.integrations.multiagent.MultiAgentClient;
import es.agonzalez.assistant.recipe.api.services.MetricsService;
import es.agonzalez.assistant.recipe.api.services.RecipeService;
import io.micrometer.core.instrument.Timer;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.PostConstruct;
import jakarta.validation.Valid;


@RestController
@RequestMapping("/api/recipes")
@Tag(name = "Recipes", description = "Recipe management and search operations")
public class RecipeController {
    @Autowired
    private  RecipeService recipeService;
    @Autowired
    private MetricsService metricsService;
    @Autowired
    private MultiAgentClient client;    
    @GetMapping
    @Operation(summary = "Search recipes", description = "Simple search by title or tags")
    public Page<RecipeResponse> search(
        @RequestParam(required = false) String q,
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "10") int size) {
        
        Timer.Sample sample = metricsService.startSearchTimer();
        try {
            metricsService.recordSearch("simple");
            return recipeService.search(q, PageRequest.of(Math.max(0, page), Math.min(100, Math.max(1, size))));
        } finally {
            metricsService.recordSearchDuration(sample);
        }
    }
    
    @PostMapping("/search/advanced")
    @Operation(summary = "Advanced recipe search", description = "Search recipes with multiple criteria including ingredients, cooking time, diet, and rating")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Search completed successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid search criteria")
    })
    public ResponseEntity<Page<RecipeResponse>> advancedSearch(
            @Valid @RequestBody AdvancedSearchRequest request,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        
        Timer.Sample sample = metricsService.startSearchTimer();
        try {
            metricsService.recordSearch("advanced");
            PageRequest pageable = PageRequest.of(Math.max(0, page), Math.min(100, Math.max(1, size)));
            Page<RecipeResponse> results = recipeService.advancedSearch(request, pageable);
            return ResponseEntity.ok(results);
        } finally {
            metricsService.recordSearchDuration(sample);
        }
    }

    @GetMapping("/search/filters")
    @Operation(summary = "Get search filter options", description = "Retrieve available filter options for advanced search")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Filter options retrieved successfully")
    })
    public ResponseEntity<SearchFiltersResponse> getSearchFilters() {
        SearchFiltersResponse filters = recipeService.getSearchFilters();
        return ResponseEntity.ok(filters);
    }
    
    @GetMapping("/{id}")
    @Operation(summary = "Get recipe by ID", description = "Retrieve a specific recipe by its ID")
    public RecipeResponse get(@PathVariable @Parameter(description = "Recipe ID") UUID id) {
        metricsService.recordRecipeView(id.toString());
        return recipeService.get(id);
    }

    @PostMapping
    @Operation(summary = "Create new recipe", description = "Create a new recipe with ingredients and instructions")
    public RecipeResponse create(@Valid @RequestBody RecipeCreateRequest recipeCreateRequest) {
        return recipeService.create(recipeCreateRequest);
    }

    @Value("${multiagent.service.url:http://localhost:8081}")
    private String multiAgentServiceUrl;
    @Value("${multiagent.service.apikey:}")
    private String apiKey;
    private RestClient http;

    @PostConstruct
    public void initialize() {
        http = RestClient.builder().baseUrl(multiAgentServiceUrl).build();
    }



    @PostMapping("/suggest")
    @Operation(summary = "Get recipe suggestions", description = "Get AI-powered recipe suggestions based on preferences")
    public SuggestResponse suggest(@Valid @RequestBody SuggestRequest request) {

         try {
            
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.set("X-API-Key", apiKey);
            Map<?, ? > body = Map.of(
                "userId", "",
                "text", "!recipe Generar receta",
                "params", Map.of(
                    "ingredients", request.getIngredients(),
                    "diet", request.getPreferences().getDiet().name()
                )
            );
            

            Map<? ,?> res = http.post().uri("/api/v1/ai")
            .header("Content-Type", "application/json")
            .header("X-API-Key", apiKey)
            .body(body).retrieve().body(Map.class);

            if(res != null && res.containsKey("data")) 
            {
                String title = readString(res, "title", "Receta generada");
                String summary = readString(res, "summary", "Descripcion receta");
                List<String> steps = readList(res, "steps", List.of());
                Set<String> tags = readSet(res, "steps", Set.of());
                return new SuggestResponse(title, summary, steps, tags);
            }

            return new SuggestResponse("","summary", List.of(), Set.of());
            
        } catch (RestClientException e) {
            throw new IllegalStateException("Failed to communicate with multi-agent service: " + e.getMessage(), e);
        }
    }


        private List<String> readList(Map<?,?> map, String key, List<String> defaultValue) {
        if(map.containsKey(key) && map.get(key) instanceof List l && !l.isEmpty() && l.get(0) instanceof String){
            return l;
        }
        return defaultValue;
    }

    private Set<String> readSet(Map<?,?> map, String key, Set<String> defaultValue) {
        if(map.containsKey(key) && map.get(key) instanceof Set l && !l.isEmpty() && l.iterator().next() instanceof String){
            return l;
        }
        return defaultValue;
    }



    private String readString(Map<?,?> map, String param, String defaultValue) {
        if(map.containsKey(param) && map.get(param) instanceof String s)
            return s;
        return defaultValue;
    }

}
