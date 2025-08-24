package es.agonzalez.assistant.recipe.api.services.impl;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import es.agonzalez.assistant.recipe.api.dtos.SuggestRequest;
import es.agonzalez.assistant.recipe.api.dtos.SuggestResponse;
import es.agonzalez.assistant.recipe.api.integrations.ollama.OllamaClient;
import es.agonzalez.assistant.recipe.api.integrations.ollama.PromptBuilder;
import es.agonzalez.assistant.recipe.api.services.SuggestionHistoryService;
import es.agonzalez.assistant.recipe.api.services.SuggestionService;

@Service
public class SuggestionServiceImpl implements SuggestionService {

    @Autowired
    private OllamaClient client;
    private final  ObjectMapper mapper = new ObjectMapper();
    @Autowired
    private SuggestionHistoryService historyService;

    @Override
    public SuggestResponse suggest(SuggestRequest req) {
        long t0 = System.currentTimeMillis();
        String raw = client.generate(PromptBuilder.build(req));

        SuggestResponse out;
        try {
            JsonNode root = mapper.readTree(raw);
            String title = safeText(root, "title", "Receta sugerida");
            String summary = safeText(root, "summary", "");
            List<String> steps = mapper.convertValue(root.path("steps"), mapper.getTypeFactory().constructCollectionType(List.class, String.class));
            Set<String> tags = new HashSet<>(mapper.convertValue(root.path("steps"), mapper.getTypeFactory().constructCollectionType(List.class, String.class)));
            out = new SuggestResponse(title, summary, steps, tags);
        } catch (Exception ex) 
        {
            out = new SuggestResponse("Receta generada", "Texto mal formado", List.of(), Set.of());
        }

        long dt = System.currentTimeMillis() - t0;
        historyService.save("llama3.3:2b", req.getIngredients(), req.getPreferences(), out, dt);
        return out;
    }

    static String safeText(JsonNode node, String field, String def) {
        JsonNode n = node.get(field);
        return (n != null && n.isTextual()) ? n.asText() : def;
    }
    
}
