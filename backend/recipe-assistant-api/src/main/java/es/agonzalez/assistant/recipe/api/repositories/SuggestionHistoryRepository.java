package es.agonzalez.assistant.recipe.api.repositories;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import es.agonzalez.assistant.recipe.api.models.SuggestionHistory;

@Repository
public interface SuggestionHistoryRepository extends JpaRepository<SuggestionHistory, UUID> {
    
}
