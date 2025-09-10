package es.agonzalez.assistant.recipe.api.models;

import java.time.Instant;
import java.util.UUID;

import org.hibernate.annotations.CreationTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

/**
 * Entity representing a user's favorite recipe
 */
@Entity
@Table(name = "recipe_favorites", 
       uniqueConstraints = {
           @UniqueConstraint(columnNames = {"recipe_id", "user_identifier"})
       },
       indexes = {
           @Index(name = "idx_recipe_favorite_user", columnList = "user_identifier"),
           @Index(name = "idx_recipe_favorite_recipe", columnList = "recipe_id"),
           @Index(name = "idx_recipe_favorite_created", columnList = "created_at")
       })
public class RecipeFavorite {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "recipe_id", nullable = false)
    @NotNull
    private Recipe recipe;

    @Column(name = "user_identifier", nullable = false, length = 100)
    @NotBlank
    @Size(max = 100)
    private String userIdentifier;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;

    // Default constructor
    public RecipeFavorite() {
    }

    // Constructor with parameters
    public RecipeFavorite(Recipe recipe, String userIdentifier) {
        this.recipe = recipe;
        this.userIdentifier = userIdentifier;
    }

    // Getters and Setters
    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public Recipe getRecipe() {
        return recipe;
    }

    public void setRecipe(Recipe recipe) {
        this.recipe = recipe;
    }

    public String getUserIdentifier() {
        return userIdentifier;
    }

    public void setUserIdentifier(String userIdentifier) {
        this.userIdentifier = userIdentifier;
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
        private Recipe recipe;
        private String userIdentifier;

        public Builder recipe(Recipe recipe) {
            this.recipe = recipe;
            return this;
        }

        public Builder userIdentifier(String userIdentifier) {
            this.userIdentifier = userIdentifier;
            return this;
        }

        public RecipeFavorite build() {
            return new RecipeFavorite(recipe, userIdentifier);
        }
    }
}
