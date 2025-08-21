package es.agonzalez.assistant.recipe.es.agonzalez.assistant.recipe.api.repositories;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import es.agonzalez.assistant.recipe.es.agonzalez.assistant.recipe.api.models.Ingredient;

@Repository
public interface IngredientRepository extends JpaRepository<Ingredient, UUID> {
    Optional<Ingredient> findByNameIgnoreCase(String name);
    
}
