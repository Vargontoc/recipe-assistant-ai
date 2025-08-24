package es.agonzalez.assistant.recipe.api.services.impl;

import java.time.Instant;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;

import es.agonzalez.assistant.recipe.api.dtos.HistoryDetail;
import es.agonzalez.assistant.recipe.api.dtos.HistoryItem;
import es.agonzalez.assistant.recipe.api.models.SuggestionHistory;
import es.agonzalez.assistant.recipe.api.repositories.SuggestionHistoryRepository;
import es.agonzalez.assistant.recipe.api.services.SuggestionHistoryService;

@Service
public class SuggestionHistoryServiceImpl  implements SuggestionHistoryService{
    @Autowired
    private SuggestionHistoryRepository repository;
    private final ObjectMapper om = new ObjectMapper();
    @Override
    public Page<HistoryItem> list(int page, int size) {
        Pageable p = PageRequest.of(Math.max(0, page), Math.min(100, Math.max(1, size)),Sort.by(Sort.Direction.DESC, "createAt"));
        return repository.findAll(p).map(this::toItem);
    }

    @Override
    public HistoryDetail getDetail(UUID id) {
        var e = repository.findById(id).orElseThrow();
        return toDetailItem(e);
    }

    private HistoryDetail toDetailItem(SuggestionHistory e) {
        HistoryDetail hd = new HistoryDetail();
        hd.setCreateAt(e.getCreateAt());
        hd.setDuration(e.getDuration());
        hd.setId(e.getId());
        hd.setIngredients(e.getInputIngredients());
        hd.setModelUser(e.getModelUsed());
        hd.setPreferences(e.getPreferencesSnapshot());
        hd.setResponse(e.getResponse());
        
        return hd;
    }

    @Override
    public SuggestionHistory save(String model, Object ingredients, Object preferences, Object response, Long duration) {
        try {
          String ingJson =   om.writeValueAsString(ingredients);
          String prefJson = (preferences != null) ? om.writeValueAsString(preferences) : null;
          String respJson = (response != null) ? om.writeValueAsString(response) : null;

          String title =  null;
          if(response != null){
            var node = om.valueToTree(response);
            if(node.has("title")) {
                title = node.get("title").asText(null);
            }
          }

          SuggestionHistory sh = new SuggestionHistory();
          sh.setDuration(duration);
          sh.setInputIngredients(ingJson);
          sh.setModelUsed(model);
          sh.setPreferencesSnapshot(prefJson);
          sh.setResponse(respJson);
          sh.setResponseTitle(title);
          sh.setCreateAt(Instant.now());

          return repository.save(sh);
        }catch(Exception e) 
        {
            SuggestionHistory fail = new SuggestionHistory();
            fail.setModelUsed(model);
            fail.setInputIngredients(String.valueOf(ingredients));
            fail.setPreferencesSnapshot(String.valueOf(preferences));
            fail.setResponseTitle(null);
            fail.setDuration(duration);
            fail.setResponse(String.valueOf(response));
            fail.setCreateAt(Instant.now());
            return repository.save(fail);
        }
        
    }

    private HistoryItem toItem(SuggestionHistory e) {
        HistoryItem hi = new HistoryItem();
        hi.setResponseTitle(e.getResponseTitle());
        hi.setCreatedAt(e.getCreateAt());
        hi.setDuration(e.getDuration());
        hi.setId(e.getId());
        hi.setModelUsed(e.getModelUsed());
        
        return hi;
    }
}
