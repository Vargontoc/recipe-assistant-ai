package es.agonzalez.assistant.recipe.api.dtos;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

public class SuggestRequest{
    @NotEmpty
    @JsonProperty("ingredients")
    private List<@Size(min=1, max=80) String> ingredients;
    @JsonProperty("preferences")
    private Preferences preferences;

    
    public List<String> getIngredients() {
        return ingredients;
    }

    public void setIngredients(List<String> ingredients) {
        this.ingredients = ingredients;
    }

    public Preferences getPreferences() {
        return preferences;
    }

    public void setPreferences(Preferences preferences) {
        this.preferences = preferences;
    }


}
