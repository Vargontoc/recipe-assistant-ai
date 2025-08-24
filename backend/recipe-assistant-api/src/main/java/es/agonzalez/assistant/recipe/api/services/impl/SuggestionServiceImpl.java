package es.agonzalez.assistant.recipe.api.services.impl;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import es.agonzalez.assistant.recipe.api.dtos.Preferences;
import es.agonzalez.assistant.recipe.api.dtos.PreferencesResponse;
import es.agonzalez.assistant.recipe.api.dtos.SuggestRequest;
import es.agonzalez.assistant.recipe.api.dtos.SuggestResponse;
import es.agonzalez.assistant.recipe.api.integrations.ollama.OllamaClient;
import es.agonzalez.assistant.recipe.api.integrations.ollama.PromptBuilder;
import es.agonzalez.assistant.recipe.api.services.SuggestionHistoryService;
import es.agonzalez.assistant.recipe.api.services.SuggestionService;
import es.agonzalez.assistant.recipe.api.services.UserPreferencesService;
import es.agonzalez.assistant.recipe.api.utils.JsonExtractor;

@Service
public class SuggestionServiceImpl implements SuggestionService {

    @Autowired
    private OllamaClient client;
    private final  ObjectMapper mapper = new ObjectMapper();
    @Autowired
    private SuggestionHistoryService historyService;
    @Autowired
    private UserPreferencesService preferencesService;

    @Override
    public SuggestResponse suggest(SuggestRequest req) {
        long t0 = System.currentTimeMillis();
        if(req.getPreferences() == null) {
            req.setPreferences(read(preferencesService.getOrCreate()));
        }
        String raw = client.generate(PromptBuilder.build(req));

        JsonNode root = tryParse(raw).orElseGet(() -> JsonExtractor.firstJsonObject(raw).flatMap(this::tryParse).orElse(null));
    

        SuggestResponse out;
        if(root != null && root.hasNonNull("title") && root.path("steps").isArray()) {
            String title = safeText(root, "title", "Receta sugerida");
            String summary = safeText(root, "summary", "");
            List<String> steps = mapper.convertValue(root.path("steps"), mapper.getTypeFactory().constructCollectionType(List.class, String.class));
            Set<String> tags = new HashSet<>(mapper.convertValue(root.path("steps"), mapper.getTypeFactory().constructCollectionType(List.class, String.class)));
            out = new SuggestResponse(title, summary, steps, tags);
            
            long dt = System.currentTimeMillis() - t0;
            historyService.save("llama3.3:2b", req.getIngredients(), req.getPreferences(), out, dt);
        }else {
            out = new SuggestResponse("Receta generada", "La IA no devolvió JSON válido. Se muestra texto sin formato", List.of(),  Set.of());
        }

        return out;
    }

    private Preferences read(PreferencesResponse e) {
        Preferences p = new Preferences();
        p.setDiet(e.diet());
        p.setExludeIngredients(e.excludes());
        p.setAllergens(e.allergns());
        return p;
    }

    private Optional<JsonNode> tryParse(String text) {
        try { return Optional.of(mapper.readTree(text)); } catch (Exception e) { return Optional.empty(); }
    }

    static String safeText(JsonNode node, String field, String def) {
        JsonNode n = node.get(field);
        return (n != null && n.isTextual()) ? n.asText() : def;
    }
    
}
