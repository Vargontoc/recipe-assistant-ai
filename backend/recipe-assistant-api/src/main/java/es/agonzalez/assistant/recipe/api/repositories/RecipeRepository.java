package es.agonzalez.assistant.recipe.api.repositories;

import java.util.List;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import es.agonzalez.assistant.recipe.api.models.Recipe;

@Repository
public interface RecipeRepository extends JpaRepository<Recipe, UUID> {

    /**
     * Search recipes by title with optimized query and pagination support
     */
    @Query("""
            SELECT DISTINCT r FROM Recipe r
            WHERE (:q IS NULL OR :q = '' OR LOWER(r.title) LIKE LOWER(CONCAT('%', :q, '%')))
            ORDER BY r.createdAt DESC
            """)
    Page<Recipe> searchByTitle(@Param("q") String q, Pageable pageable);

    /**
     * Search recipes by title or tags with full-text capabilities
     */
    @Query("""
            SELECT DISTINCT r FROM Recipe r
            LEFT JOIN r.tags t
            WHERE (:q IS NULL OR :q = '' OR 
                   LOWER(r.title) LIKE LOWER(CONCAT('%', :q, '%')) OR
                   LOWER(t) LIKE LOWER(CONCAT('%', :q, '%')))
            ORDER BY r.createdAt DESC
            """)
    Page<Recipe> searchByTitleOrTag(@Param("q") String q, Pageable pageable);

    /**
     * Legacy method for backward compatibility
     */
    @Query("""
            SELECT DISTINCT r FROM Recipe r
            WHERE LOWER(r.title) LIKE LOWER(CONCAT('%', :q, '%'))
            """)
    List<Recipe> searchByTitleOrTag(@Param("q") String q);

    /**
     * Search recipes by ingredients
     */
    @Query("""
            SELECT DISTINCT r FROM Recipe r
            JOIN r.ingredients ri
            JOIN ri.ingredient i
            WHERE LOWER(i.name) IN :ingredientNames
            ORDER BY r.createdAt DESC
            """)
    Page<Recipe> findByIngredientsIn(@Param("ingredientNames") List<String> ingredientNames, Pageable pageable);

    /**
     * Find recipes that contain all specified ingredients
     */
    @Query("""
            SELECT r FROM Recipe r
            WHERE (
                SELECT COUNT(DISTINCT i.name) 
                FROM r.ingredients ri 
                JOIN ri.ingredient i 
                WHERE LOWER(i.name) IN :ingredientNames
            ) = :ingredientCount
            ORDER BY r.createdAt DESC
            """)
    Page<Recipe> findByAllIngredients(@Param("ingredientNames") List<String> ingredientNames, 
                                      @Param("ingredientCount") long ingredientCount, 
                                      Pageable pageable);

    /**
     * Advanced search with multiple criteria
     */
    @Query("""
            SELECT DISTINCT r FROM Recipe r
            LEFT JOIN r.ingredients ri
            LEFT JOIN ri.ingredient i
            LEFT JOIN r.tags t
            LEFT JOIN r.ratings rat
            WHERE (:query IS NULL OR :query = '' OR 
                   LOWER(r.title) LIKE LOWER(CONCAT('%', :query, '%')) OR
                   LOWER(r.description) LIKE LOWER(CONCAT('%', :query, '%')) OR
                   LOWER(t) LIKE LOWER(CONCAT('%', :query, '%')))
            AND (:diet IS NULL OR r.diet = :diet)
            AND (:minCookingTime IS NULL OR r.cookingTime >= :minCookingTime)
            AND (:maxCookingTime IS NULL OR r.cookingTime <= :maxCookingTime)
            AND (:minServings IS NULL OR r.servings >= :minServings)
            AND (:maxServings IS NULL OR r.servings <= :maxServings)
            AND (:minRating IS NULL OR (
                SELECT AVG(CAST(rat2.rating AS DOUBLE)) 
                FROM r.ratings rat2
            ) >= :minRating)
            GROUP BY r.id, r.title, r.description, r.cookingTime, r.servings, r.diet, r.createdAt
            """)
    Page<Recipe> advancedSearch(
            @Param("query") String query,
            @Param("diet") String diet,
            @Param("minCookingTime") Integer minCookingTime,
            @Param("maxCookingTime") Integer maxCookingTime,
            @Param("minServings") Integer minServings,
            @Param("maxServings") Integer maxServings,
            @Param("minRating") Double minRating,
            Pageable pageable
    );

    /**
     * Get distinct diets available in recipes
     */
    @Query("SELECT DISTINCT r.diet FROM Recipe r WHERE r.diet IS NOT NULL ORDER BY r.diet")
    List<String> findDistinctDiets();

    /**
     * Get distinct ingredients available
     */
    @Query("""
            SELECT DISTINCT i.name 
            FROM Recipe r 
            JOIN r.ingredients ri 
            JOIN ri.ingredient i 
            ORDER BY i.name
            """)
    List<String> findDistinctIngredients();

    /**
     * Get cooking time range
     */
    @Query("SELECT MIN(r.cookingTime), MAX(r.cookingTime) FROM Recipe r WHERE r.cookingTime IS NOT NULL")
    Object[] findCookingTimeRange();

    /**
     * Get servings range
     */
    @Query("SELECT MIN(r.servings), MAX(r.servings) FROM Recipe r WHERE r.servings IS NOT NULL")
    Object[] findServingsRange();
    
}
