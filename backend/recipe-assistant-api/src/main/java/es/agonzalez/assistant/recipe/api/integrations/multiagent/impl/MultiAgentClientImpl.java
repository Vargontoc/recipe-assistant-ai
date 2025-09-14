package es.agonzalez.assistant.recipe.api.integrations.multiagent.impl;

import java.net.http.HttpResponse;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;

import es.agonzalez.assistant.recipe.api.dtos.SuggestRequest;
import es.agonzalez.assistant.recipe.api.dtos.SuggestResponse;
import es.agonzalez.assistant.recipe.api.integrations.multiagent.MultiAgentClient;
import jakarta.annotation.PostConstruct;

/**
 * Implementation of MultiAgentClient that communicates with the Multi-Agent Service
 * for AI-powered recipe generation.
 */
@Service
public class MultiAgentClientImpl implements MultiAgentClient {
    Logger logger = LoggerFactory.getLogger(MultiAgentClient.class);
    @Value("${multiagent.service.url:http://localhost:8081}")
    private String multiAgentServiceUrl;
    @Value("${multiagent.service.apikey:}")
    private String apiKey;
    private RestClient http;

    @PostConstruct
    public void initialize() {
        http = RestClient.builder().baseUrl(multiAgentServiceUrl).build();
    }


    @Override
    public  SuggestResponse generateRecipeSuggestions(SuggestRequest request) {
        try {
            
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.set("X-API-Key", apiKey);
            logger.info("Headers: {}", headers);
            Map<?, ? > body = Map.of(
                "userId", "",
                "text", "!recipe Generar receta",
                "params", Map.of(
                    "ingredients", request.getIngredients(),
                    "diet", request.getPreferences().getDiet().name()
                )
            );
            
            logger.info("Payload: {}", body);

            Map<? ,?> res = http.post().uri("/api/v1/ai")
            .header("Content-Type", "application/json")
            .header("X-API-Key", apiKey)
            .body(body).retrieve().body(Map.class);
            
            logger.info("Response: {}", res);

            if(res != null && res.containsKey("data")) 
            {
                String title = readString(res, "title", "Receta generada");
                String summary = readString(res, "summary", "Descripcion receta");
                List<String> steps = readList(res, "steps", List.of());
                Set<String> tags = readSet(res, "steps", Set.of());
                return new SuggestResponse(title, summary, steps, tags);
            }

            return new SuggestResponse("","summary", List.of(), Set.of());
            
        } catch (RestClientException e) {
            throw new IllegalStateException("Failed to communicate with multi-agent service: " + e.getMessage(), e);
        }
    }

    private List<String> readList(Map<?,?> map, String key, List<String> defaultValue) {
        if(map.containsKey(key) && map.get(key) instanceof List l && !l.isEmpty() && l.get(0) instanceof String){
            return l;
        }
        return defaultValue;
    }

    private Set<String> readSet(Map<?,?> map, String key, Set<String> defaultValue) {
        if(map.containsKey(key) && map.get(key) instanceof Set l && !l.isEmpty() && l.iterator().next() instanceof String){
            return l;
        }
        return defaultValue;
    }



    private String readString(Map<?,?> map, String param, String defaultValue) {
        if(map.containsKey(param) && map.get(param) instanceof String s)
            return s;
        return defaultValue;
    }


    @Override
    public boolean isServiceAvailable() {
        try {
            Map<? ,?> res = http.get().uri("/admin/health")
            .header("Content-Type", "application/json")
            .header("X-API-Key", apiKey)
            .retrieve().body(Map.class);
            return res != null  && res.containsKey("status") && res.get("status") instanceof Integer i && i == 200;
        } catch (RestClientException e) {
            return false;
        }
    }

}
