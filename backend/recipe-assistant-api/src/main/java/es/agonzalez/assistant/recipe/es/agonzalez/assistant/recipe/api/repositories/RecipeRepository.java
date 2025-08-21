package es.agonzalez.assistant.recipe.es.agonzalez.assistant.recipe.api.repositories;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import es.agonzalez.assistant.recipe.es.agonzalez.assistant.recipe.api.models.Recipe;

@Repository
public interface RecipeRepository extends JpaRepository<Recipe, UUID> {

    @Query("""
            select distinct r from recipes r
            left join r.tags t
            where lower(r.title) like lower(concat('%', :q, '%'))
            or lower(t) like lower(concat('%', :q, '%'))
            """)
    List<Recipe> findByNameContainingIgnoreCase(@Param("q") String q);
    
}
