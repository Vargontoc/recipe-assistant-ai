package es.agonzalez.assistant.recipe.api.models;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import org.hibernate.annotations.UuidGenerator;

import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Table;

@Entity
@Table(name="user_preferences")
public class UserPreferences extends BaseEntity {

    @Id
    @GeneratedValue
    @UuidGenerator
    private UUID id;

    @Enumerated(EnumType.STRING)
    @Column( name="diet", nullable=false, length=20)
    private Diet diet = Diet.NONE;

    @ElementCollection
    @CollectionTable(name="user_pref_excluded", joinColumns= @JoinColumn(name="pref_id"))
    @Column(name="ingredients", length=60)
    private Set<String> excludeIngredients = new HashSet<>();

    @ElementCollection
    @CollectionTable(name="user_pref_allergens", joinColumns= @JoinColumn(name="pref_id"))
    @Column(name="allergen", length=60)
    private Set<String> allergens = new HashSet<>();

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public Diet getDiet() {
        return diet;
    }

    public void setDiet(Diet diet) {
        this.diet = diet;
    }

    public Set<String> getExcludeIngredients() {
        return excludeIngredients;
    }

    public void setExcludeIngredients(Set<String> excludeIngredients) {
        this.excludeIngredients = excludeIngredients;
    }

    public Set<String> getAllergens() {
        return allergens;
    }

    public void setAllergens(Set<String> allergens) {
        this.allergens = allergens;
    }

    
}
