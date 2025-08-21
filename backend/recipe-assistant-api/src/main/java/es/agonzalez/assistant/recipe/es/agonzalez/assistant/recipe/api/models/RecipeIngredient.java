package es.agonzalez.assistant.recipe.es.agonzalez.assistant.recipe.api.models;

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
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "recipe_ingredients", uniqueConstraints= {
    @UniqueConstraint(columnNames = {"recipe_id", "ingredient_id"}, name="uk_recipe_ingredient_unique"),
})
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
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
}
