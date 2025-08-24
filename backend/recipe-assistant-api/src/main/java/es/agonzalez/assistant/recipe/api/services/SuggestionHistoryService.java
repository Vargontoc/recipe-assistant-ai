package es.agonzalez.assistant.recipe.api.services;

import java.util.UUID;

import org.springframework.data.domain.Page;

import es.agonzalez.assistant.recipe.api.dtos.HistoryDetail;
import es.agonzalez.assistant.recipe.api.dtos.HistoryItem;
import es.agonzalez.assistant.recipe.api.models.SuggestionHistory;

public interface SuggestionHistoryService {
    
    Page<HistoryItem> list(int page, int size);
    
    HistoryDetail getDetail(UUID id);

    SuggestionHistory save(String model, Object ingredients, Object preferences, Object response, Long duration);
}
