package es.agonzalez.assistant.recipe.api.repositories;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import es.agonzalez.assistant.recipe.api.models.RecipeRating;

@Repository
public interface RecipeRatingRepository extends JpaRepository<RecipeRating, UUID> {

    /**
     * Find rating by recipe and user identifier
     */
    Optional<RecipeRating> findByRecipeIdAndUserIdentifier(UUID recipeId, String userIdentifier);

    /**
     * Find all ratings for a specific recipe with pagination
     */
    Page<RecipeRating> findByRecipeIdOrderByCreatedAtDesc(UUID recipeId, Pageable pageable);

    /**
     * Find all ratings by a specific user
     */
    List<RecipeRating> findByUserIdentifierOrderByCreatedAtDesc(String userIdentifier);

    /**
     * Get average rating for a recipe
     */
    @Query("""
            SELECT AVG(CAST(r.rating AS double)) 
            FROM RecipeRating r 
            WHERE r.recipe.id = :recipeId
            """)
    Optional<Double> getAverageRatingByRecipeId(@Param("recipeId") UUID recipeId);

    /**
     * Get rating count for a recipe
     */
    @Query("""
            SELECT COUNT(r) 
            FROM RecipeRating r 
            WHERE r.recipe.id = :recipeId
            """)
    Long getRatingCountByRecipeId(@Param("recipeId") UUID recipeId);

    /**
     * Get rating distribution for a recipe
     */
    @Query("""
            SELECT r.rating, COUNT(r) 
            FROM RecipeRating r 
            WHERE r.recipe.id = :recipeId 
            GROUP BY r.rating 
            ORDER BY r.rating DESC
            """)
    List<Object[]> getRatingDistributionByRecipeId(@Param("recipeId") UUID recipeId);

    /**
     * Find top rated recipes
     */
    @Query("""
            SELECT r.recipe.id, AVG(CAST(r.rating AS double)) as avgRating, COUNT(r) as ratingCount
            FROM RecipeRating r 
            GROUP BY r.recipe.id 
            HAVING COUNT(r) >= :minRatings 
            ORDER BY avgRating DESC, ratingCount DESC
            """)
    Page<Object[]> findTopRatedRecipes(@Param("minRatings") long minRatings, Pageable pageable);

    /**
     * Check if user has rated a recipe
     */
    boolean existsByRecipeIdAndUserIdentifier(UUID recipeId, String userIdentifier);

    /**
     * Delete all ratings for a recipe
     */
    void deleteByRecipeId(UUID recipeId);

    /**
     * Find recent ratings with comments
     */
    @Query("""
            SELECT r FROM RecipeRating r 
            WHERE r.comment IS NOT NULL 
            AND LENGTH(TRIM(r.comment)) > 0 
            ORDER BY r.createdAt DESC
            """)
    Page<RecipeRating> findRecentRatingsWithComments(Pageable pageable);
}
