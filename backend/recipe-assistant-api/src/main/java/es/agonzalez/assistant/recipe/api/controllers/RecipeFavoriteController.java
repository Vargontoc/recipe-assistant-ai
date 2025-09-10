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
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import es.agonzalez.assistant.recipe.api.dto.CreateRecipeFavoriteRequest;
import es.agonzalez.assistant.recipe.api.dto.RecipeFavoriteResponse;
import es.agonzalez.assistant.recipe.api.services.RecipeFavoriteService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

/**
 * REST controller for recipe favorites operations
 */
@RestController
@RequestMapping("/api/v1/favorites")
@Validated
@Tag(name = "Recipe Favorites", description = "Operations for managing recipe favorites")
public class RecipeFavoriteController {

    private final RecipeFavoriteService favoriteService;

    public RecipeFavoriteController(RecipeFavoriteService favoriteService) {
        this.favoriteService = favoriteService;
    }

    @PostMapping
    @Operation(summary = "Add recipe to favorites", description = "Add a recipe to user's favorites list")
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "Recipe added to favorites successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid request data"),
        @ApiResponse(responseCode = "404", description = "Recipe not found"),
        @ApiResponse(responseCode = "409", description = "Recipe is already in favorites")
    })
    public ResponseEntity<RecipeFavoriteResponse> addFavorite(
            @Valid @RequestBody CreateRecipeFavoriteRequest request,
            @RequestHeader(value = "X-User-Identifier", required = false) String userIdentifier) {

        CreateRecipeFavoriteRequest updatedRequest = new CreateRecipeFavoriteRequest(
                request.recipeId(), 
                userIdentifier
        );

        RecipeFavoriteResponse favorite = favoriteService.addFavorite(updatedRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(favorite);
    }

    @DeleteMapping("/recipes/{recipeId}")
    @Operation(summary = "Remove recipe from favorites", description = "Remove a recipe from user's favorites list")
    @ApiResponses({
        @ApiResponse(responseCode = "204", description = "Recipe removed from favorites successfully"),
        @ApiResponse(responseCode = "404", description = "Recipe not found in favorites")
    })
    public ResponseEntity<Void> removeFavorite(
            @Parameter(description = "Recipe ID") @PathVariable UUID recipeId,
            @RequestHeader(value = "X-User-Identifier", required = false) String userIdentifier) {

        favoriteService.removeFavorite(recipeId, userIdentifier);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    @Operation(summary = "Get user's favorite recipes", description = "Retrieve all favorite recipes for the current user with pagination")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Favorites retrieved successfully")
    })
    public ResponseEntity<Page<RecipeFavoriteResponse>> getUserFavorites(
            @RequestHeader(value = "X-User-Identifier", required = false) String userIdentifier,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        Pageable pageable = PageRequest.of(page, size);
        Page<RecipeFavoriteResponse> favorites = favoriteService.getUserFavorites(userIdentifier, pageable);
        return ResponseEntity.ok(favorites);
    }

    @GetMapping("/recipes/{recipeId}/check")
    @Operation(summary = "Check if recipe is favorited", description = "Check whether the current user has favorited this recipe")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Check completed successfully")
    })
    public ResponseEntity<Boolean> isFavorited(
            @Parameter(description = "Recipe ID") @PathVariable UUID recipeId,
            @RequestHeader(value = "X-User-Identifier", required = false) String userIdentifier) {

        boolean isFavorited = favoriteService.isFavorited(recipeId, userIdentifier);
        return ResponseEntity.ok(isFavorited);
    }

    @GetMapping("/recipes/{recipeId}/count")
    @Operation(summary = "Get favorite count for recipe", description = "Get the total number of times this recipe has been favorited")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Count retrieved successfully")
    })
    public ResponseEntity<Long> getFavoriteCount(
            @Parameter(description = "Recipe ID") @PathVariable UUID recipeId) {

        long count = favoriteService.getFavoriteCount(recipeId);
        return ResponseEntity.ok(count);
    }

    @GetMapping("/count")
    @Operation(summary = "Get user's favorite count", description = "Get the total number of recipes favorited by the current user")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Count retrieved successfully")
    })
    public ResponseEntity<Long> getUserFavoriteCount(
            @RequestHeader(value = "X-User-Identifier", required = false) String userIdentifier) {

        long count = favoriteService.getUserFavoriteCount(userIdentifier);
        return ResponseEntity.ok(count);
    }
}
