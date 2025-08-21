package es.agonzalez.assistant.recipe.es.agonzalez.assistant.recipe.api.repositories;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import es.agonzalez.assistant.recipe.es.agonzalez.assistant.recipe.api.models.RecipeIngredient;

@Repository
public interface  RecipeIngredientRepository  extends JpaRepository<RecipeIngredient, UUID> {

    // Additional methods for RecipeIngredient can be defined here if needed
    
}
