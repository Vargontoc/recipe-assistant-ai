package es.agonzalez.assistant.recipe.api.controllers;

import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import es.agonzalez.assistant.recipe.api.dto.CreateRecipeRatingRequest;
import es.agonzalez.assistant.recipe.api.dto.RecipeRatingResponse;
import es.agonzalez.assistant.recipe.api.dto.RecipeRatingStats;
import es.agonzalez.assistant.recipe.api.services.RecipeRatingService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

/**
 * REST controller for recipe rating operations
 */
@RestController
@RequestMapping("/api/v1/recipes")
@Validated
@Tag(name = "Recipe Ratings", description = "Operations for managing recipe ratings")
public class RecipeRatingController {

    private final RecipeRatingService ratingService;

    public RecipeRatingController(RecipeRatingService ratingService) {
        this.ratingService = ratingService;
    }

    @PostMapping("/{recipeId}/ratings")
    @Operation(summary = "Create a rating for a recipe", description = "Add a new rating to a recipe")
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "Rating created successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid rating data"),
        @ApiResponse(responseCode = "404", description = "Recipe not found"),
        @ApiResponse(responseCode = "409", description = "User has already rated this recipe")
    })
    public ResponseEntity<RecipeRatingResponse> createRating(
            @Parameter(description = "Recipe ID") @PathVariable UUID recipeId,
            @Valid @RequestBody CreateRecipeRatingRequest request,
            @RequestHeader(value = "X-User-Identifier", required = false) String userIdentifier) {

        // Set the recipe ID from the path
        CreateRecipeRatingRequest updatedRequest = new CreateRecipeRatingRequest(
                recipeId, 
                request.rating(), 
                request.comment(), 
                userIdentifier
        );

        RecipeRatingResponse rating = ratingService.createRating(updatedRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(rating);
    }

    @PutMapping("/{recipeId}/ratings/{ratingId}")
    @Operation(summary = "Update a recipe rating", description = "Update an existing rating for a recipe")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Rating updated successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid rating data"),
        @ApiResponse(responseCode = "401", description = "User not authorized to update this rating"),
        @ApiResponse(responseCode = "404", description = "Rating or recipe not found")
    })
    public ResponseEntity<RecipeRatingResponse> updateRating(
            @Parameter(description = "Recipe ID") @PathVariable UUID recipeId,
            @Parameter(description = "Rating ID") @PathVariable UUID ratingId,
            @Valid @RequestBody CreateRecipeRatingRequest request,
            @RequestHeader(value = "X-User-Identifier", required = false) String userIdentifier) {

        CreateRecipeRatingRequest updatedRequest = new CreateRecipeRatingRequest(
                recipeId, 
                request.rating(), 
                request.comment(), 
                userIdentifier
        );

        RecipeRatingResponse rating = ratingService.updateRating(ratingId, updatedRequest, userIdentifier);
        return ResponseEntity.ok(rating);
    }

    @DeleteMapping("/{recipeId}/ratings/{ratingId}")
    @Operation(summary = "Delete a recipe rating", description = "Delete an existing rating for a recipe")
    @ApiResponses({
        @ApiResponse(responseCode = "204", description = "Rating deleted successfully"),
        @ApiResponse(responseCode = "401", description = "User not authorized to delete this rating"),
        @ApiResponse(responseCode = "404", description = "Rating not found")
    })
    public ResponseEntity<Void> deleteRating(
            @Parameter(description = "Recipe ID") @PathVariable UUID recipeId,
            @Parameter(description = "Rating ID") @PathVariable UUID ratingId,
            @RequestHeader(value = "X-User-Identifier", required = false) String userIdentifier) {

        ratingService.deleteRating(ratingId, userIdentifier);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{recipeId}/ratings")
    @Operation(summary = "Get ratings for a recipe", description = "Retrieve all ratings for a specific recipe with pagination")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Ratings retrieved successfully"),
        @ApiResponse(responseCode = "404", description = "Recipe not found")
    })
    public ResponseEntity<Page<RecipeRatingResponse>> getRecipeRatings(
            @Parameter(description = "Recipe ID") @PathVariable UUID recipeId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        Pageable pageable = PageRequest.of(page, size);
        Page<RecipeRatingResponse> ratings = ratingService.getRatingsByRecipeId(recipeId, pageable);
        return ResponseEntity.ok(ratings);
    }

    @GetMapping("/{recipeId}/ratings/stats")
    @Operation(summary = "Get rating statistics for a recipe", description = "Retrieve comprehensive rating statistics including average, distribution, and count")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Rating statistics retrieved successfully"),
        @ApiResponse(responseCode = "404", description = "Recipe not found")
    })
    public ResponseEntity<RecipeRatingStats> getRatingStats(
            @Parameter(description = "Recipe ID") @PathVariable UUID recipeId) {

        RecipeRatingStats stats = ratingService.getRatingStats(recipeId);
        return ResponseEntity.ok(stats);
    }

    @GetMapping("/{recipeId}/ratings/user")
    @Operation(summary = "Get user's rating for a recipe", description = "Retrieve the current user's rating for a specific recipe")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "User rating retrieved successfully"),
        @ApiResponse(responseCode = "404", description = "Recipe or user rating not found")
    })
    public ResponseEntity<RecipeRatingResponse> getUserRating(
            @Parameter(description = "Recipe ID") @PathVariable UUID recipeId,
            @RequestHeader(value = "X-User-Identifier", required = false) String userIdentifier) {

        return ratingService.getUserRatingForRecipe(recipeId, userIdentifier)
                .map(rating -> ResponseEntity.ok(rating))
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/{recipeId}/ratings/check")
    @Operation(summary = "Check if user has rated recipe", description = "Check whether the current user has already rated this recipe")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Check completed successfully")
    })
    public ResponseEntity<Boolean> hasUserRated(
            @Parameter(description = "Recipe ID") @PathVariable UUID recipeId,
            @RequestHeader(value = "X-User-Identifier", required = false) String userIdentifier) {

        boolean hasRated = ratingService.hasUserRatedRecipe(recipeId, userIdentifier);
        return ResponseEntity.ok(hasRated);
    }

    @GetMapping("/ratings/recent")
    @Operation(summary = "Get recent ratings with comments", description = "Retrieve recent ratings that include comments from all recipes")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Recent ratings retrieved successfully")
    })
    public ResponseEntity<Page<RecipeRatingResponse>> getRecentRatingsWithComments(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        Pageable pageable = PageRequest.of(page, size);
        Page<RecipeRatingResponse> ratings = ratingService.getRecentRatingsWithComments(pageable);
        return ResponseEntity.ok(ratings);
    }
}
