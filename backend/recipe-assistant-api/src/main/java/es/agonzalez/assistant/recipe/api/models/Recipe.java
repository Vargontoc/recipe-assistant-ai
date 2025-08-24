package es.agonzalez.assistant.recipe.api.models;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import org.hibernate.annotations.UuidGenerator;

import jakarta.persistence.CascadeType;
import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "recipes")
public class Recipe extends BaseEntity {
    @Id
    @GeneratedValue
    @UuidGenerator
    private UUID id;

    @Column(name = "title", nullable = false, length = 166)
    private String title;

    @Column(name = "summary", length = 500)
    private String summary;

    @Column(name = "steps", columnDefinition = "TEXT")
    private String steps;

    @ElementCollection
    @CollectionTable(name = "recipe_tags", 
        joinColumns = @jakarta.persistence.JoinColumn(name = "recipe_id"))
    @Column(name = "tag", length = 50)
    private Set<String> tags = new HashSet<>();

    @OneToMany(mappedBy = "recipe", orphanRemoval = true, cascade= CascadeType.ALL)
    private List<RecipeIngredient> ingredients = new ArrayList<>();

    public void addIngredient(RecipeIngredient ingredient) {
        if (ingredient != null) {
            ingredient.setRecipe(this);
            this.ingredients.add(ingredient);
        }
    }

    public void removeIngredient(RecipeIngredient ingredient) {
        if (ingredient != null) {
            ingredient.setRecipe(null);
            this.ingredients.remove(ingredient);
        }
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public String getSteps() {
        return steps;
    }

    public void setSteps(String steps) {
        this.steps = steps;
    }

    public Set<String> getTags() {
        return tags;
    }

    public void setTags(Set<String> tags) {
        this.tags = tags;
    }

    public List<RecipeIngredient> getIngredients() {
        return ingredients;
    }

    public void setIngredients(List<RecipeIngredient> ingredients) {
        this.ingredients = ingredients;
    }

    
    
}
