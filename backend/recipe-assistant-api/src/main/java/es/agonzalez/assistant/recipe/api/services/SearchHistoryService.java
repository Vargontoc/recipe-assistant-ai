package es.agonzalez.assistant.recipe.api.services;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import es.agonzalez.assistant.recipe.api.dto.SearchHistoryResponse;

/**
 * Service interface for search history operations
 */
public interface SearchHistoryService {

    /**
     * Record a search query
     */
    void recordSearch(String userIdentifier, String searchQuery, String searchType, Integer resultsCount);

    /**
     * Get search history for a user with pagination
     */
    Page<SearchHistoryResponse> getUserSearchHistory(String userIdentifier, Pageable pageable);

    /**
     * Get recent search suggestions for autocomplete
     */
    List<String> getSearchSuggestions(String userIdentifier, String query, int limit);

    /**
     * Get most popular search queries
     */
    List<String> getPopularQueries(int limit);

    /**
     * Get user search statistics
     */
    SearchStats getUserSearchStats(String userIdentifier);

    /**
     * Clean up old search history entries
     */
    void cleanupOldEntries(int daysToKeep);

    /**
     * DTO for search statistics
     */
    record SearchStats(
            long totalSearches,
            double averageResults,
            LocalDateTime lastSearchAt
    ) {
    }
}
