package es.agonzalez.assistant.recipe.es.agonzalez.assistant.recipe.api.models;

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
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "recipies")
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
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
    @Builder.Default
    private Set<String> tags = new HashSet<>();

    @OneToMany(mappedBy = "recipe", orphanRemoval = true, cascade= CascadeType.ALL)
    @Builder.Default
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
    
}
