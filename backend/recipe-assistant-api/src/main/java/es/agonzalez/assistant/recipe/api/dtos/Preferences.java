package es.agonzalez.assistant.recipe.api.dtos;

import java.util.Set;

import com.fasterxml.jackson.annotation.JsonProperty;

import es.agonzalez.assistant.recipe.api.models.Diet;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class Preferences {
    @NotNull 
    @JsonProperty("diet")
    private Diet diet;
    @JsonProperty("exludeIngredients")
    private Set<@Size(max=60) String> exludeIngredients;
    @JsonProperty("allergens")
    private Set<@Size(max=60) String> allergens;
    
    public Diet getDiet() {
        return diet;
    }

    public void setDiet(Diet diet) {
        this.diet = diet;
    }

    public Set<String> getExludeIngredients() {
        return exludeIngredients;
    }

    public void setExludeIngredients(Set<String> exludeIngredients) {
        this.exludeIngredients = exludeIngredients;
    }

    public Set<String> getAllergens() {
        return allergens;
    }

    public void setAllergens(Set<String> allergens) {
        this.allergens = allergens;
    }

    
    
}
