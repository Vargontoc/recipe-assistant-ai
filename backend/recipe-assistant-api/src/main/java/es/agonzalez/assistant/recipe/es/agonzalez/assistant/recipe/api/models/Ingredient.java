package es.agonzalez.assistant.recipe.es.agonzalez.assistant.recipe.api.models;

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
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "ingredients", uniqueConstraints= {
    @UniqueConstraint(columnNames = {"name"}, name="uk_ingredient_name"),
})
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
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
    @Builder.Default
    private Set<String> aliases = new HashSet<>();
}
