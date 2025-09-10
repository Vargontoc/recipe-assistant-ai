package es.agonzalez.assistant.recipe.api.services.impl;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.Assertions.within;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.verify;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.ActiveProfiles;

import es.agonzalez.assistant.recipe.api.dto.CreateRecipeRatingRequest;
import es.agonzalez.assistant.recipe.api.dto.RecipeRatingResponse;
import es.agonzalez.assistant.recipe.api.dto.RecipeRatingStats;
import es.agonzalez.assistant.recipe.api.exceptions.ResourceNotFoundException;
import es.agonzalez.assistant.recipe.api.exceptions.UnauthorizedException;
import es.agonzalez.assistant.recipe.api.models.Recipe;
import es.agonzalez.assistant.recipe.api.models.RecipeRating;
import es.agonzalez.assistant.recipe.api.repositories.RecipeRatingRepository;
import es.agonzalez.assistant.recipe.api.repositories.RecipeRepository;

/**
 * Unit tests for RecipeRatingServiceImpl
 */
@ExtendWith(MockitoExtension.class)
@ActiveProfiles("test")
class RecipeRatingServiceImplTest {

    @Mock
    private RecipeRatingRepository ratingRepository;

    @Mock
    private RecipeRepository recipeRepository;

    @InjectMocks
    private RecipeRatingServiceImpl ratingService;

    private Recipe testRecipe;
    private RecipeRating testRating;
    private CreateRecipeRatingRequest createRequest;
    private UUID testRecipeId;
    private UUID testRatingId;
    private String testUserId;

    @BeforeEach
    void setUp() {
        testRecipeId = UUID.randomUUID();
        testRatingId = UUID.randomUUID();
        testUserId = "test-user-123";
        
        // Setup test recipe
        testRecipe = new Recipe();
        testRecipe.setId(testRecipeId);
        testRecipe.setTitle("Test Recipe");
        
        // Setup test rating
        testRating = RecipeRating.builder()
            .recipe(testRecipe)
            .rating(5)
            .comment("Excellent recipe!")
            .userIdentifier(testUserId)
            .isAnonymous(false)
            .build();
        testRating.setId(testRatingId);
        testRating.setCreatedAt(Instant.now());
        testRating.setUpdatedAt(Instant.now());
        
        // Setup create request
        createRequest = new CreateRecipeRatingRequest(
            testRecipeId, 
            5, 
            "Excellent recipe!",
            testUserId
        );
    }

    @Test
    @DisplayName("Should create rating successfully")
    void shouldCreateRating() {
        // Given
        given(recipeRepository.findById(testRecipeId)).willReturn(Optional.of(testRecipe));
        given(ratingRepository.findByRecipeIdAndUserIdentifier(testRecipeId, testUserId))
            .willReturn(Optional.empty());
        given(ratingRepository.save(any(RecipeRating.class))).willReturn(testRating);

        // When
        RecipeRatingResponse result = ratingService.createRating(createRequest);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.rating()).isEqualTo(5);
        assertThat(result.comment()).isEqualTo("Excellent recipe!");
        assertThat(result.userIdentifier()).isEqualTo(testUserId);
        verify(ratingRepository).save(any(RecipeRating.class));
    }

    @Test
    @DisplayName("Should throw exception when recipe not found")
    void shouldThrowExceptionWhenRecipeNotFound() {
        // Given
        given(recipeRepository.findById(testRecipeId)).willReturn(Optional.empty());

        // When & Then
        assertThatThrownBy(() -> ratingService.createRating(createRequest))
            .isInstanceOf(ResourceNotFoundException.class)
            .hasMessageContaining("Recipe not found");
    }

    @Test
    @DisplayName("Should throw exception when user already rated recipe")
    void shouldThrowExceptionWhenUserAlreadyRated() {
        // Given
        given(recipeRepository.findById(testRecipeId)).willReturn(Optional.of(testRecipe));
        given(ratingRepository.findByRecipeIdAndUserIdentifier(testRecipeId, testUserId))
            .willReturn(Optional.of(testRating));

        // When & Then
        assertThatThrownBy(() -> ratingService.createRating(createRequest))
            .isInstanceOf(IllegalStateException.class)
            .hasMessageContaining("User has already rated this recipe");
    }

    @Test
    @DisplayName("Should update existing rating")
    void shouldUpdateExistingRating() {
        // Given
        CreateRecipeRatingRequest updateRequest = new CreateRecipeRatingRequest(
            testRecipeId, 4, "Good recipe", testUserId
        );
        
        RecipeRating updatedRating = RecipeRating.builder()
            .recipe(testRecipe)
            .rating(4)
            .comment("Good recipe")
            .userIdentifier(testUserId)
            .isAnonymous(false)
            .build();
        updatedRating.setId(testRatingId);
        updatedRating.setCreatedAt(Instant.now());
        updatedRating.setUpdatedAt(Instant.now());
        
        given(ratingRepository.findById(testRatingId)).willReturn(Optional.of(testRating));
        given(ratingRepository.save(any(RecipeRating.class))).willReturn(updatedRating);

        // When
        RecipeRatingResponse result = ratingService.updateRating(testRatingId, updateRequest, testUserId);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.rating()).isEqualTo(4);
        assertThat(result.comment()).isEqualTo("Good recipe");
        verify(ratingRepository).save(any(RecipeRating.class));
    }

    @Test
    @DisplayName("Should throw exception when rating not found for update")
    void shouldThrowExceptionWhenRatingNotFoundForUpdate() {
        // Given
        CreateRecipeRatingRequest updateRequest = new CreateRecipeRatingRequest(
            testRecipeId, 4, "Good recipe", testUserId
        );
        given(ratingRepository.findById(testRatingId)).willReturn(Optional.empty());

        // When & Then
        assertThatThrownBy(() -> ratingService.updateRating(testRatingId, updateRequest, testUserId))
            .isInstanceOf(ResourceNotFoundException.class)
            .hasMessageContaining("Rating not found");
    }

    @Test
    @DisplayName("Should throw exception when user not authorized to update")
    void shouldThrowExceptionWhenUserNotAuthorizedToUpdate() {
        // Given
        CreateRecipeRatingRequest updateRequest = new CreateRecipeRatingRequest(
            testRecipeId, 4, "Good recipe", "other-user"
        );
        given(ratingRepository.findById(testRatingId)).willReturn(Optional.of(testRating));

        // When & Then
        assertThatThrownBy(() -> ratingService.updateRating(testRatingId, updateRequest, "other-user"))
            .isInstanceOf(UnauthorizedException.class)
            .hasMessageContaining("User not authorized");
    }

    @Test
    @DisplayName("Should delete rating successfully")
    void shouldDeleteRating() {
        // Given
        given(ratingRepository.findById(testRatingId)).willReturn(Optional.of(testRating));

        // When
        ratingService.deleteRating(testRatingId, testUserId);

        // Then
        verify(ratingRepository).delete(testRating);
    }

    @Test
    @DisplayName("Should get rating by ID")
    void shouldGetRatingById() {
        // Given
        given(ratingRepository.findById(testRatingId)).willReturn(Optional.of(testRating));

        // When
        Optional<RecipeRatingResponse> result = ratingService.getRatingById(testRatingId);

        // Then
        assertThat(result).isPresent();
        assertThat(result.get().id()).isEqualTo(testRatingId);
        assertThat(result.get().rating()).isEqualTo(5);
        assertThat(result.get().comment()).isEqualTo("Excellent recipe!");
    }

    @Test
    @DisplayName("Should return empty when rating not found by ID")
    void shouldReturnEmptyWhenRatingNotFoundById() {
        // Given
        given(ratingRepository.findById(testRatingId)).willReturn(Optional.empty());

        // When
        Optional<RecipeRatingResponse> result = ratingService.getRatingById(testRatingId);

        // Then
        assertThat(result).isEmpty();
    }

    @Test
    @DisplayName("Should get ratings by recipe ID")
    void shouldGetRatingsByRecipeId() {
        // Given
        Pageable pageable = PageRequest.of(0, 10);
        Page<RecipeRating> ratingsPage = new PageImpl<>(List.of(testRating));
        
        given(ratingRepository.findByRecipeIdOrderByCreatedAtDesc(testRecipeId, pageable))
            .willReturn(ratingsPage);

        // When
        Page<RecipeRatingResponse> result = ratingService.getRatingsByRecipeId(testRecipeId, pageable);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getContent()).hasSize(1);
        assertThat(result.getContent().get(0).recipeId()).isEqualTo(testRecipeId);
        assertThat(result.getContent().get(0).rating()).isEqualTo(5);
    }

    @Test
    @DisplayName("Should get rating statistics")
    void shouldGetRatingStatistics() {
        // Given
        List<Object[]> distribution = List.of(
            new Object[]{5, 10L},
            new Object[]{4, 5L},
            new Object[]{3, 2L},
            new Object[]{2, 1L},
            new Object[]{1, 0L}
        );
        given(ratingRepository.getRatingDistributionByRecipeId(testRecipeId))
            .willReturn(distribution);

        // When
        RecipeRatingStats result = ratingService.getRatingStats(testRecipeId);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.averageRating()).isEqualTo(4.33, within(0.01));
        assertThat(result.totalRatings()).isEqualTo(18L);
        assertThat(result.fiveStarCount()).isEqualTo(10L);
        assertThat(result.fourStarCount()).isEqualTo(5L);
        assertThat(result.threeStarCount()).isEqualTo(2L);
        assertThat(result.twoStarCount()).isEqualTo(1L);
        assertThat(result.oneStarCount()).isEqualTo(0L);
    }

    @Test
    @DisplayName("Should handle empty rating statistics")
    void shouldHandleEmptyRatingStatistics() {
        // Given
        given(ratingRepository.getRatingDistributionByRecipeId(testRecipeId))
            .willReturn(List.of());

        // When
        RecipeRatingStats result = ratingService.getRatingStats(testRecipeId);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.averageRating()).isEqualTo(0.0);
        assertThat(result.totalRatings()).isEqualTo(0L);
        assertThat(result.fiveStarCount()).isEqualTo(0L);
        assertThat(result.fourStarCount()).isEqualTo(0L);
        assertThat(result.threeStarCount()).isEqualTo(0L);
        assertThat(result.twoStarCount()).isEqualTo(0L);
        assertThat(result.oneStarCount()).isEqualTo(0L);
    }

    @Test
    @DisplayName("Should get user rating for recipe")
    void shouldGetUserRatingForRecipe() {
        // Given
        given(ratingRepository.findByRecipeIdAndUserIdentifier(testRecipeId, testUserId))
            .willReturn(Optional.of(testRating));

        // When
        Optional<RecipeRatingResponse> result = ratingService.getUserRatingForRecipe(testRecipeId, testUserId);

        // Then
        assertThat(result).isPresent();
        assertThat(result.get().recipeId()).isEqualTo(testRecipeId);
        assertThat(result.get().userIdentifier()).isEqualTo(testUserId);
        assertThat(result.get().rating()).isEqualTo(5);
    }

    @Test
    @DisplayName("Should check if user has rated recipe")
    void shouldCheckIfUserHasRatedRecipe() {
        // Given
        given(ratingRepository.existsByRecipeIdAndUserIdentifier(testRecipeId, testUserId))
            .willReturn(true);

        // When
        boolean result = ratingService.hasUserRatedRecipe(testRecipeId, testUserId);

        // Then
        assertThat(result).isTrue();
    }

    @Test
    @DisplayName("Should get recent ratings with comments")
    void shouldGetRecentRatingsWithComments() {
        // Given
        Pageable pageable = PageRequest.of(0, 10);
        Page<RecipeRating> ratingsPage = new PageImpl<>(List.of(testRating));
        
        given(ratingRepository.findRecentRatingsWithComments(pageable))
            .willReturn(ratingsPage);

        // When
        Page<RecipeRatingResponse> result = ratingService.getRecentRatingsWithComments(pageable);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getContent()).hasSize(1);
        assertThat(result.getContent().get(0).comment()).isEqualTo("Excellent recipe!");
    }

    @Test
    @DisplayName("Should handle anonymous user rating")
    void shouldHandleAnonymousUserRating() {
        // Given
        CreateRecipeRatingRequest anonymousRequest = new CreateRecipeRatingRequest(
            testRecipeId, 4, "Good recipe", null
        );
        
        RecipeRating anonymousRating = RecipeRating.builder()
            .recipe(testRecipe)
            .rating(4)
            .comment("Good recipe")
            .userIdentifier("anonymous")
            .isAnonymous(true)
            .build();
        anonymousRating.setId(UUID.randomUUID());
        anonymousRating.setCreatedAt(Instant.now());
        anonymousRating.setUpdatedAt(Instant.now());
        
        given(recipeRepository.findById(testRecipeId)).willReturn(Optional.of(testRecipe));
        given(ratingRepository.findByRecipeIdAndUserIdentifier(testRecipeId, "anonymous"))
            .willReturn(Optional.empty());
        given(ratingRepository.save(any(RecipeRating.class))).willReturn(anonymousRating);

        // When
        RecipeRatingResponse result = ratingService.createRating(anonymousRequest);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.rating()).isEqualTo(4);
        assertThat(result.userIdentifier()).isEqualTo("anonymous");
        assertThat(result.isAnonymous()).isTrue();
    }
}
