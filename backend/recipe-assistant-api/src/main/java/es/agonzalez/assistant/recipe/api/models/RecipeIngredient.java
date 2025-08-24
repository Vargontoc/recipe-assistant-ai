package es.agonzalez.assistant.recipe.api.models;

import java.util.UUID;

import org.hibernate.annotations.UuidGenerator;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;


@Entity
@Table(name = "recipe_ingredients", uniqueConstraints= {
    @UniqueConstraint(columnNames = {"recipe_id", "ingredient_id"}, name="uk_recipe_ingredient_unique"),
})
public class RecipeIngredient extends BaseEntity {

    @Id
    @GeneratedValue
    @UuidGenerator
    private UUID id;

    @ManyToOne(optional = false, fetch= FetchType.LAZY)
    @JoinColumn(name = "recipe_id", nullable = false, foreignKey= @ForeignKey(name = "fk_ri_recipe"))
    private Recipe recipe;

    @ManyToOne(optional = false, fetch= FetchType.LAZY)
    @JoinColumn(name = "ingredient_id", nullable = false, foreignKey= @ForeignKey(name = "fk_ri_ingredient"))
    private Ingredient ingredient;
    
    @Column(name = "quantity", nullable = false, length = 80)
    private String quantity;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public Recipe getRecipe() {
        return recipe;
    }

    public void setRecipe(Recipe recipe) {
        this.recipe = recipe;
    }

    public Ingredient getIngredient() {
        return ingredient;
    }

    public void setIngredient(Ingredient ingredient) {
        this.ingredient = ingredient;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }
}

