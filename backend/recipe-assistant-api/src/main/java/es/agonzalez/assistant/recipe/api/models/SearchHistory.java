package es.agonzalez.assistant.recipe.api.models;

import java.time.Instant;
import java.util.UUID;

import org.hibernate.annotations.CreationTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * Entity representing a user's search history
 */
@Entity
@Table(name = "search_history", 
       indexes = {
           @Index(name = "idx_search_history_user", columnList = "user_identifier"),
           @Index(name = "idx_search_history_created", columnList = "created_at"),
           @Index(name = "idx_search_history_query", columnList = "search_query")
       })
public class SearchHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "user_identifier", nullable = false, length = 100)
    @NotBlank
    @Size(max = 100)
    private String userIdentifier;

    @Column(name = "search_query", nullable = false, length = 500)
    @NotBlank
    @Size(max = 500)
    private String searchQuery;

    @Column(name = "search_type", length = 50)
    @Size(max = 50)
    private String searchType; // "simple", "advanced", "suggestion"

    @Column(name = "results_count")
    private Integer resultsCount;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;

    // Default constructor
    public SearchHistory() {
    }

    // Constructor with parameters
    public SearchHistory(String userIdentifier, String searchQuery, String searchType, Integer resultsCount) {
        this.userIdentifier = userIdentifier;
        this.searchQuery = searchQuery;
        this.searchType = searchType;
        this.resultsCount = resultsCount;
    }

    // Getters and Setters
    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getUserIdentifier() {
        return userIdentifier;
    }

    public void setUserIdentifier(String userIdentifier) {
        this.userIdentifier = userIdentifier;
    }

    public String getSearchQuery() {
        return searchQuery;
    }

    public void setSearchQuery(String searchQuery) {
        this.searchQuery = searchQuery;
    }

    public String getSearchType() {
        return searchType;
    }

    public void setSearchType(String searchType) {
        this.searchType = searchType;
    }

    public Integer getResultsCount() {
        return resultsCount;
    }

    public void setResultsCount(Integer resultsCount) {
        this.resultsCount = resultsCount;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    // Builder pattern
    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private String userIdentifier;
        private String searchQuery;
        private String searchType;
        private Integer resultsCount;

        public Builder userIdentifier(String userIdentifier) {
            this.userIdentifier = userIdentifier;
            return this;
        }

        public Builder searchQuery(String searchQuery) {
            this.searchQuery = searchQuery;
            return this;
        }

        public Builder searchType(String searchType) {
            this.searchType = searchType;
            return this;
        }

        public Builder resultsCount(Integer resultsCount) {
            this.resultsCount = resultsCount;
            return this;
        }

        public SearchHistory build() {
            return new SearchHistory(userIdentifier, searchQuery, searchType, resultsCount);
        }
    }
}
