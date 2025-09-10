package es.agonzalez.assistant.recipe.api.services.impl;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import es.agonzalez.assistant.recipe.api.dto.SearchHistoryResponse;
import es.agonzalez.assistant.recipe.api.models.SearchHistory;
import es.agonzalez.assistant.recipe.api.repositories.SearchHistoryRepository;
import es.agonzalez.assistant.recipe.api.services.SearchHistoryService;

/**
 * Implementation of search history service
 */
@Service
@Transactional
public class SearchHistoryServiceImpl implements SearchHistoryService {

    private final SearchHistoryRepository searchHistoryRepository;

    public SearchHistoryServiceImpl(SearchHistoryRepository searchHistoryRepository) {
        this.searchHistoryRepository = searchHistoryRepository;
    }

    @Override
    public void recordSearch(String userIdentifier, String searchQuery, String searchType, Integer resultsCount) {
        String actualUserIdentifier = userIdentifier != null ? userIdentifier : "anonymous";
        
        SearchHistory searchHistory = SearchHistory.builder()
                .userIdentifier(actualUserIdentifier)
                .searchQuery(searchQuery)
                .searchType(searchType)
                .resultsCount(resultsCount)
                .build();
        
        searchHistoryRepository.save(searchHistory);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<SearchHistoryResponse> getUserSearchHistory(String userIdentifier, Pageable pageable) {
        String actualUserIdentifier = userIdentifier != null ? userIdentifier : "anonymous";
        
        return searchHistoryRepository
                .findByUserIdentifierOrderByCreatedAtDesc(actualUserIdentifier, pageable)
                .map(this::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public List<String> getSearchSuggestions(String userIdentifier, String query, int limit) {
        String actualUserIdentifier = userIdentifier != null ? userIdentifier : "anonymous";
        
        return searchHistoryRepository.findRecentQueriesForUser(
                actualUserIdentifier, 
                query != null ? query : "", 
                PageRequest.of(0, limit)
        );
    }

    @Override
    @Transactional(readOnly = true)
    public List<String> getPopularQueries(int limit) {
        return searchHistoryRepository.findMostPopularQueries(PageRequest.of(0, limit))
                .stream()
                .map(result -> (String) result[0])
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public SearchStats getUserSearchStats(String userIdentifier) {
        String actualUserIdentifier = userIdentifier != null ? userIdentifier : "anonymous";
        
        Object[] stats = searchHistoryRepository.getUserSearchStats(actualUserIdentifier);
        
        if (stats == null || stats.length < 3) {
            return new SearchStats(0L, 0.0, null);
        }
        
        long totalSearches = stats[0] != null ? ((Number) stats[0]).longValue() : 0L;
        double averageResults = stats[1] != null ? ((Number) stats[1]).doubleValue() : 0.0;
        LocalDateTime lastSearchAt = stats[2] != null ? 
                ((Instant) stats[2]).atZone(ZoneId.systemDefault()).toLocalDateTime() : null;
        
        return new SearchStats(totalSearches, averageResults, lastSearchAt);
    }

    @Override
    public void cleanupOldEntries(int daysToKeep) {
        Instant cutoffDate = Instant.now().minusSeconds(daysToKeep * 24L * 60L * 60L);
        searchHistoryRepository.deleteOldEntries(cutoffDate);
    }

    private SearchHistoryResponse toDto(SearchHistory searchHistory) {
        return new SearchHistoryResponse(
                searchHistory.getId(),
                searchHistory.getUserIdentifier(),
                searchHistory.getSearchQuery(),
                searchHistory.getSearchType(),
                searchHistory.getResultsCount(),
                searchHistory.getCreatedAt().atZone(ZoneId.systemDefault()).toLocalDateTime()
        );
    }
}
