package es.agonzalez.assistant.recipe.api.models;

import java.util.UUID;

import org.hibernate.annotations.UuidGenerator;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;

/**
 * Entity representing a user rating for a recipe
 */
@Entity
@Table(name = "recipe_ratings", 
    uniqueConstraints = {
        @UniqueConstraint(columnNames = {"recipe_id", "user_identifier"}, name = "uk_recipe_user_rating")
    },
    indexes = {
        @Index(name = "idx_recipe_rating_recipe", columnList = "recipe_id"),
        @Index(name = "idx_recipe_rating_user", columnList = "user_identifier"),
        @Index(name = "idx_recipe_rating_score", columnList = "rating")
    })
public class RecipeRating extends BaseEntity {

    @Id
    @GeneratedValue
    @UuidGenerator
    private UUID id;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "recipe_id", nullable = false, foreignKey = @ForeignKey(name = "fk_rating_recipe"))
    private Recipe recipe;

    @Column(name = "user_identifier", nullable = false, length = 100)
    private String userIdentifier; // Could be session ID, user ID, or anonymous identifier

    @Column(name = "rating", nullable = false)
    @Min(value = 1, message = "Rating must be at least 1")
    @Max(value = 5, message = "Rating must be at most 5")
    private Integer rating;

    @Column(name = "comment", length = 500)
    @Size(max = 500, message = "Comment must not exceed 500 characters")
    private String comment;



    // Constructors
    public RecipeRating() {}

    public RecipeRating(Recipe recipe, String userIdentifier, Integer rating) {
        this.recipe = recipe;
        this.userIdentifier = userIdentifier;
        this.rating = rating;
    }

    public RecipeRating(Recipe recipe, String userIdentifier, Integer rating, String comment) {
        this.recipe = recipe;
        this.userIdentifier = userIdentifier;
        this.rating = rating;
        this.comment = comment;
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

    public Integer getRating() {
        return rating;
    }

    public void setRating(Integer rating) {
        this.rating = rating;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }



    // Builder pattern
    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private Recipe recipe;
        private Integer rating;
        private String comment;
        private String userIdentifier;

        public Builder recipe(Recipe recipe) {
            this.recipe = recipe;
            return this;
        }

        public Builder rating(Integer rating) {
            this.rating = rating;
            return this;
        }

        public Builder comment(String comment) {
            this.comment = comment;
            return this;
        }

        public Builder userIdentifier(String userIdentifier) {
            this.userIdentifier = userIdentifier;
            return this;
        }



        public RecipeRating build() {
            RecipeRating entity = new RecipeRating();
            entity.setRecipe(this.recipe);
            entity.setRating(this.rating);
            entity.setComment(this.comment);
            entity.setUserIdentifier(this.userIdentifier);
            return entity;
        }
    }

    @Override
    public String toString() {
        return "RecipeRating{" +
                "id=" + id +
                ", rating=" + rating +
                ", userIdentifier='" + userIdentifier + '\'' +
                '}';
    }
}
