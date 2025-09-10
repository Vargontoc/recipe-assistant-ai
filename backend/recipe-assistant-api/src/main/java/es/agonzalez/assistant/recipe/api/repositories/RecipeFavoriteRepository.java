package es.agonzalez.assistant.recipe.api.repositories;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import es.agonzalez.assistant.recipe.api.models.RecipeFavorite;

/**
 * Repository for recipe favorites operations
 */
@Repository
public interface RecipeFavoriteRepository extends JpaRepository<RecipeFavorite, UUID> {

    /**
     * Find favorite by recipe ID and user identifier
     */
    Optional<RecipeFavorite> findByRecipeIdAndUserIdentifier(UUID recipeId, String userIdentifier);

    /**
     * Get all favorites for a user with pagination
     */
    @Query("SELECT rf FROM RecipeFavorite rf JOIN FETCH rf.recipe WHERE rf.userIdentifier = :userIdentifier ORDER BY rf.createdAt DESC")
    Page<RecipeFavorite> findByUserIdentifierOrderByCreatedAtDesc(@Param("userIdentifier") String userIdentifier, Pageable pageable);

    /**
     * Check if a recipe is favorited by a user
     */
    boolean existsByRecipeIdAndUserIdentifier(UUID recipeId, String userIdentifier);

    /**
     * Delete favorite by recipe ID and user identifier
     */
    void deleteByRecipeIdAndUserIdentifier(UUID recipeId, String userIdentifier);

    /**
     * Count favorites for a recipe
     */
    @Query("SELECT COUNT(rf) FROM RecipeFavorite rf WHERE rf.recipe.id = :recipeId")
    long countByRecipeId(@Param("recipeId") UUID recipeId);

    /**
     * Count total favorites for a user
     */
    long countByUserIdentifier(String userIdentifier);
}
