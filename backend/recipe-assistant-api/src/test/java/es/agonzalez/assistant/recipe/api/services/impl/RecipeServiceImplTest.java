package es.agonzalez.assistant.recipe.api.services.impl;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyDouble;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.ActiveProfiles;

import es.agonzalez.assistant.recipe.api.dto.AdvancedSearchRequest;
import es.agonzalez.assistant.recipe.api.dto.SearchFiltersResponse;
import es.agonzalez.assistant.recipe.api.dtos.IngredientRequest;
import es.agonzalez.assistant.recipe.api.dtos.RecipeCreateRequest;
import es.agonzalez.assistant.recipe.api.dtos.RecipeResponse;
import es.agonzalez.assistant.recipe.api.models.Ingredient;
import es.agonzalez.assistant.recipe.api.models.Recipe;
import es.agonzalez.assistant.recipe.api.models.RecipeIngredient;
import es.agonzalez.assistant.recipe.api.repositories.IngredientRepository;
import es.agonzalez.assistant.recipe.api.repositories.RecipeRepository;
import es.agonzalez.assistant.recipe.api.services.SearchHistoryService;

/**
 * Unit tests for RecipeServiceImpl
 */
@ExtendWith(MockitoExtension.class)
@ActiveProfiles("test")
class RecipeServiceImplTest {

    @Mock
    private RecipeRepository recipeRepository;

    @Mock
    private IngredientRepository ingredientRepository;

    @Mock
    private SearchHistoryService searchHistoryService;

    @InjectMocks
    private RecipeServiceImpl recipeService;

    private Recipe testRecipe;
    private RecipeCreateRequest createRequest;
    private UUID testRecipeId;

    @BeforeEach
    void setUp() {
        testRecipeId = UUID.randomUUID();
        
        // Setup test recipe
        testRecipe = new Recipe();
        testRecipe.setId(testRecipeId);
        testRecipe.setTitle("Test Recipe");
        testRecipe.setSummary("A delicious test recipe");
        testRecipe.setSteps("1. Mix ingredients\n2. Cook\n3. Serve");
        testRecipe.setTags(Set.of("test", "easy"));
        
        // Setup ingredient
        Ingredient ingredient = new Ingredient();
        ingredient.setId(UUID.randomUUID());
        ingredient.setName("Tomato");
        
        RecipeIngredient recipeIngredient = new RecipeIngredient();
        recipeIngredient.setRecipe(testRecipe);
        recipeIngredient.setIngredient(ingredient);
        recipeIngredient.setQuantity("2 cups");
        
        testRecipe.setIngredients(List.of(recipeIngredient));
        
        // Setup create request
        createRequest = new RecipeCreateRequest(
            "Test Recipe",
            "A delicious test recipe",
            "1. Mix ingredients\n2. Cook\n3. Serve",
            Set.of("test", "easy"),
            List.of(new IngredientRequest("Tomato", "2 cups"))
        );
    }

    @Test
    @DisplayName("Should retrieve recipe by ID successfully")
    void shouldGetRecipeById() {
        // Given
        given(recipeRepository.findById(testRecipeId)).willReturn(Optional.of(testRecipe));

        // When
        RecipeResponse result = recipeService.get(testRecipeId);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.id()).isEqualTo(testRecipeId);
        assertThat(result.title()).isEqualTo("Test Recipe");
        assertThat(result.summary()).isEqualTo("A delicious test recipe");
        assertThat(result.tags()).containsExactlyInAnyOrder("test", "easy");
        assertThat(result.ingredients()).hasSize(1);
        assertThat(result.ingredients().get(0).name()).isEqualTo("Tomato");
        assertThat(result.ingredients().get(0).quantity()).isEqualTo("2 cups");
    }

    @Test
    @DisplayName("Should throw exception when recipe not found")
    void shouldThrowExceptionWhenRecipeNotFound() {
        // Given
        UUID nonExistentId = UUID.randomUUID();
        given(recipeRepository.findById(nonExistentId)).willReturn(Optional.empty());

        // When & Then
        assertThatThrownBy(() -> recipeService.get(nonExistentId))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessageContaining("Recipe not found with id");
    }

    @Test
    @DisplayName("Should create recipe successfully")
    void shouldCreateRecipe() {
        // Given
        Ingredient existingIngredient = new Ingredient();
        existingIngredient.setId(UUID.randomUUID());
        existingIngredient.setName("Tomato");
        
        given(ingredientRepository.findByNameIgnoreCase("Tomato"))
            .willReturn(Optional.of(existingIngredient));
        given(recipeRepository.save(any(Recipe.class))).willReturn(testRecipe);

        // When
        RecipeResponse result = recipeService.create(createRequest);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.title()).isEqualTo("Test Recipe");
        assertThat(result.summary()).isEqualTo("A delicious test recipe");
        verify(recipeRepository).save(any(Recipe.class));
    }

    @Test
    @DisplayName("Should create new ingredient when not exists")
    void shouldCreateNewIngredientWhenNotExists() {
        // Given
        Ingredient newIngredient = new Ingredient();
        newIngredient.setId(UUID.randomUUID());
        newIngredient.setName("Tomato");
        
        given(ingredientRepository.findByNameIgnoreCase("Tomato"))
            .willReturn(Optional.empty());
        given(ingredientRepository.save(any(Ingredient.class)))
            .willReturn(newIngredient);
        given(recipeRepository.save(any(Recipe.class))).willReturn(testRecipe);

        // When
        RecipeResponse result = recipeService.create(createRequest);

        // Then
        assertThat(result).isNotNull();
        verify(ingredientRepository).save(any(Ingredient.class));
        verify(recipeRepository).save(any(Recipe.class));
    }

    @Test
    @DisplayName("Should search recipes by query")
    void shouldSearchRecipesByQuery() {
        // Given
        String query = "pasta";
        Pageable pageable = PageRequest.of(0, 10);
        Page<Recipe> recipePage = new PageImpl<>(List.of(testRecipe));
        
        given(recipeRepository.searchByTitleOrTag(query, pageable))
            .willReturn(recipePage);

        // When
        Page<RecipeResponse> result = recipeService.search(query, pageable);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getContent()).hasSize(1);
        assertThat(result.getContent().get(0).title()).isEqualTo("Test Recipe");
        verify(recipeRepository).searchByTitleOrTag(query, pageable);
    }

    @Test
    @DisplayName("Should perform advanced search")
    void shouldPerformAdvancedSearch() {
        // Given
        AdvancedSearchRequest request = new AdvancedSearchRequest(
            "pasta",
            List.of("tomato"),
            List.of(),
            List.of("vegetarian"),
            List.of(),
            30,
            120,
            1,
            4,
            4.0,
            "relevance",
            "desc"
        );
        Pageable pageable = PageRequest.of(0, 10);
        Page<Recipe> recipePage = new PageImpl<>(List.of(testRecipe));
        
        given(recipeRepository.advancedSearch(anyString(), anyString(), anyInt(), anyInt(), 
                anyInt(), anyInt(), anyDouble(), any(Pageable.class)))
            .willReturn(recipePage);

        // When
        Page<RecipeResponse> result = recipeService.advancedSearch(request, pageable);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getContent()).hasSize(1);
        verify(recipeRepository).advancedSearch(
            eq("pasta"), eq("vegetarian"), eq(30), eq(120), 
            eq(1), eq(4), eq(4.0), any(Pageable.class)
        );
    }

    @Test
    @DisplayName("Should get search filters")
    void shouldGetSearchFilters() {
        // Given
        given(recipeRepository.findDistinctIngredients())
            .willReturn(List.of("tomato", "pasta", "cheese"));
        given(recipeRepository.findDistinctDiets())
            .willReturn(List.of("vegetarian", "vegan", "gluten-free"));
        given(recipeRepository.findCookingTimeRange())
            .willReturn(new Object[]{15, 180});
        given(recipeRepository.findServingsRange())
            .willReturn(new Object[]{1, 8});

        // When
        SearchFiltersResponse result = recipeService.getSearchFilters();

        // Then
        assertThat(result).isNotNull();
        assertThat(result.availableIngredients()).contains("tomato", "pasta", "cheese");
        assertThat(result.availableDiets()).contains("vegetarian", "vegan", "gluten-free");
        assertThat(result.minCookingTime()).isEqualTo(15);
        assertThat(result.maxCookingTime()).isEqualTo(180);
        assertThat(result.minServings()).isEqualTo(1);
        assertThat(result.maxServings()).isEqualTo(8);
    }

    @Test
    @DisplayName("Should handle null search query")
    void shouldHandleNullSearchQuery() {
        // Given
        Pageable pageable = PageRequest.of(0, 10);
        Page<Recipe> recipePage = new PageImpl<>(List.of(testRecipe));
        
        given(recipeRepository.findAll(pageable)).willReturn(recipePage);

        // When
        Page<RecipeResponse> result = recipeService.search(null, pageable);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getContent()).hasSize(1);
        verify(recipeRepository).findAll(pageable);
        verify(searchHistoryService, never()).recordSearch(anyString(), anyString(), anyString(), anyInt());
    }

    @Test
    @DisplayName("Should handle empty search query")
    void shouldHandleEmptySearchQuery() {
        // Given
        Pageable pageable = PageRequest.of(0, 10);
        Page<Recipe> recipePage = new PageImpl<>(List.of(testRecipe));
        
        given(recipeRepository.findAll(pageable)).willReturn(recipePage);

        // When
        Page<RecipeResponse> result = recipeService.search("", pageable);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getContent()).hasSize(1);
        verify(recipeRepository).findAll(pageable);
        verify(searchHistoryService, never()).recordSearch(anyString(), anyString(), anyString(), anyInt());
    }
}
