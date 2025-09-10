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
    
}
