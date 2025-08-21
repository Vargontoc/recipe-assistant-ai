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
            select distinct r from Recipe r
            where lower(r.title) like lower(concat('%', :q, '%'))
            """)
    List<Recipe> searchByTitleOrTag(@Param("q") String q);
    
}
