package es.agonzalez.assistant.recipe.api.dto;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * DTO for search history response
 */
public record SearchHistoryResponse(
        UUID id,
        String userIdentifier,
        String searchQuery,
        String searchType,
        Integer resultsCount,
        LocalDateTime createdAt
) {
}
