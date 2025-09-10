package es.agonzalez.assistant.recipe.api.controllers;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import es.agonzalez.assistant.recipe.api.dto.SearchHistoryResponse;
import es.agonzalez.assistant.recipe.api.services.SearchHistoryService;
import es.agonzalez.assistant.recipe.api.services.SearchHistoryService.SearchStats;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

/**
 * REST controller for search history operations
 */
@RestController
@RequestMapping("/api/v1/search-history")
@Validated
@Tag(name = "Search History", description = "Operations for managing search history")
public class SearchHistoryController {

    private final SearchHistoryService searchHistoryService;

    public SearchHistoryController(SearchHistoryService searchHistoryService) {
        this.searchHistoryService = searchHistoryService;
    }

    @GetMapping
    @Operation(summary = "Get user's search history", description = "Retrieve search history for the current user with pagination")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Search history retrieved successfully")
    })
    public ResponseEntity<Page<SearchHistoryResponse>> getUserSearchHistory(
            @RequestHeader(value = "X-User-Identifier", required = false) String userIdentifier,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {

        Pageable pageable = PageRequest.of(page, size);
        Page<SearchHistoryResponse> history = searchHistoryService.getUserSearchHistory(userIdentifier, pageable);
        return ResponseEntity.ok(history);
    }

    @GetMapping("/suggestions")
    @Operation(summary = "Get search suggestions", description = "Get search suggestions based on user's search history")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Search suggestions retrieved successfully")
    })
    public ResponseEntity<List<String>> getSearchSuggestions(
            @RequestHeader(value = "X-User-Identifier", required = false) String userIdentifier,
            @RequestParam(required = false) String query,
            @RequestParam(defaultValue = "10") int limit) {

        List<String> suggestions = searchHistoryService.getSearchSuggestions(userIdentifier, query, limit);
        return ResponseEntity.ok(suggestions);
    }

    @GetMapping("/popular")
    @Operation(summary = "Get popular search queries", description = "Retrieve most popular search queries across all users")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Popular queries retrieved successfully")
    })
    public ResponseEntity<List<String>> getPopularQueries(
            @RequestParam(defaultValue = "10") int limit) {

        List<String> popularQueries = searchHistoryService.getPopularQueries(limit);
        return ResponseEntity.ok(popularQueries);
    }

    @GetMapping("/stats")
    @Operation(summary = "Get user's search statistics", description = "Retrieve search statistics for the current user")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Search statistics retrieved successfully")
    })
    public ResponseEntity<SearchStats> getUserSearchStats(
            @RequestHeader(value = "X-User-Identifier", required = false) String userIdentifier) {

        SearchStats stats = searchHistoryService.getUserSearchStats(userIdentifier);
        return ResponseEntity.ok(stats);
    }

    @DeleteMapping("/cleanup")
    @Operation(summary = "Clean up old search history", description = "Remove old search history entries (admin operation)")
    @ApiResponses({
        @ApiResponse(responseCode = "204", description = "Cleanup completed successfully")
    })
    public ResponseEntity<Void> cleanupOldEntries(
            @RequestParam(defaultValue = "90") int daysToKeep) {

        searchHistoryService.cleanupOldEntries(daysToKeep);
        return ResponseEntity.noContent().build();
    }
}
