package es.agonzalez.assistant.recipe.api.repositories;

import java.util.List;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import es.agonzalez.assistant.recipe.api.models.SearchHistory;

/**
 * Repository for search history operations
 */
@Repository
public interface SearchHistoryRepository extends JpaRepository<SearchHistory, UUID> {

    /**
     * Get search history for a user with pagination
     */
    Page<SearchHistory> findByUserIdentifierOrderByCreatedAtDesc(String userIdentifier, Pageable pageable);

    /**
     * Get recent search queries for a user (for autocomplete)
     */
    @Query("""
            SELECT DISTINCT sh.searchQuery 
            FROM SearchHistory sh 
            WHERE sh.userIdentifier = :userIdentifier 
            AND sh.searchQuery LIKE CONCAT('%', :query, '%')
            ORDER BY sh.createdAt DESC
            """)
    List<String> findRecentQueriesForUser(@Param("userIdentifier") String userIdentifier, 
                                         @Param("query") String query, 
                                         Pageable pageable);

    /**
     * Get most popular search queries
     */
    @Query("""
            SELECT sh.searchQuery, COUNT(sh.searchQuery) as searchCount
            FROM SearchHistory sh 
            GROUP BY sh.searchQuery 
            ORDER BY searchCount DESC
            """)
    List<Object[]> findMostPopularQueries(Pageable pageable);

    /**
     * Get search statistics for a user
     */
    @Query("""
            SELECT COUNT(sh), 
                   AVG(CAST(sh.resultsCount AS DOUBLE)),
                   MAX(sh.createdAt)
            FROM SearchHistory sh 
            WHERE sh.userIdentifier = :userIdentifier
            """)
    Object[] getUserSearchStats(@Param("userIdentifier") String userIdentifier);

    /**
     * Delete old search history entries (for cleanup)
     */
    @Query("DELETE FROM SearchHistory sh WHERE sh.createdAt < :cutoffDate")
    void deleteOldEntries(@Param("cutoffDate") java.time.Instant cutoffDate);

    /**
     * Count searches by user
     */
    long countByUserIdentifier(String userIdentifier);
}
