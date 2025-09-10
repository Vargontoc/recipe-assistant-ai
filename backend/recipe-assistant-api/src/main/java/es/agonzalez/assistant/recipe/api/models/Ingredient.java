package es.agonzalez.assistant.recipe.api.models;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import org.hibernate.annotations.UuidGenerator;

import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;

@Entity
@Table(name = "ingredients", 
    uniqueConstraints= {
        @UniqueConstraint(columnNames = {"name"}, name="uk_ingredient_name")
    },
    indexes = {
        @Index(name = "idx_ingredient_name", columnList = "name")
    })
public class Ingredient extends BaseEntity {
    
    @Id
    @GeneratedValue
    @UuidGenerator
    private UUID id;

    @Column(name = "name", nullable = false, length = 120)
    private String name;

    @ElementCollection
    @CollectionTable(name = "ingredient_alias", 
        joinColumns = @jakarta.persistence.JoinColumn(name = "ingredient_id"))
    @Column(name = "alias", length = 120)
    private Set<String> aliases = new HashSet<>();

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<String> getAliases() {
        return aliases;
    }

    public void setAliases(Set<String> aliases) {
        this.aliases = aliases;
    }


}
