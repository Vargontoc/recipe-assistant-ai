package es.agonzalez.assistant.recipe.api.integrations.multiagent.impl;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import es.agonzalez.assistant.recipe.api.dtos.SuggestRequest;
import es.agonzalez.assistant.recipe.api.dtos.SuggestResponse;
import es.agonzalez.assistant.recipe.api.integrations.multiagent.MultiAgentClient;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Timer;

/**
 * Implementation of MultiAgentClient that communicates with the Multi-Agent Service
 * for AI-powered recipe generation.
 */
@Service
public class MultiAgentClientImpl implements MultiAgentClient {

    @Value("${multiagent.service.url:http://localhost:8081}")
    private String multiAgentServiceUrl;

    private final RestTemplate restTemplate;
    private final MeterRegistry meterRegistry;

    public MultiAgentClientImpl(RestTemplate restTemplate, MeterRegistry meterRegistry) {
        this.restTemplate = restTemplate;
        this.meterRegistry = meterRegistry;
    }

    @Override
    public SuggestResponse generateRecipeSuggestions(SuggestRequest request) {
        return Timer.builder("multiagent.recipe.generation")
                .tag("service", "recipe-assistant")
                .register(meterRegistry)
                .record(() -> doGenerateRecipeSuggestions(request));
    }

    private SuggestResponse doGenerateRecipeSuggestions(SuggestRequest request) {
        try {
            String url = multiAgentServiceUrl + "/api/recipes/generate";
            
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            
            HttpEntity<SuggestRequest> requestEntity = new HttpEntity<>(request, headers);
            
            ResponseEntity<SuggestResponse> response = restTemplate.exchange(
                url, 
                HttpMethod.POST, 
                requestEntity, 
                SuggestResponse.class
            );
            
            if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
                return response.getBody();
            } else {
                throw new IllegalStateException("Multi-agent service returned unexpected response: " + response.getStatusCode());
            }
            
        } catch (RestClientException e) {
            throw new IllegalStateException("Failed to communicate with multi-agent service: " + e.getMessage(), e);
        }
    }

    @Override
    public boolean isServiceAvailable() {
        try {
            String healthUrl = multiAgentServiceUrl + "/actuator/health";
            ResponseEntity<String> response = restTemplate.getForEntity(healthUrl, String.class);
            return response.getStatusCode().is2xxSuccessful();
        } catch (RestClientException e) {
            return false;
        }
    }
}
