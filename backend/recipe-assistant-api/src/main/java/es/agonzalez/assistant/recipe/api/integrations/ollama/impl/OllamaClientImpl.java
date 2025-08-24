package es.agonzalez.assistant.recipe.api.integrations.ollama.impl;



import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import es.agonzalez.assistant.recipe.api.config.AiProperties;
import es.agonzalez.assistant.recipe.api.integrations.ollama.GenerateRequest;
import es.agonzalez.assistant.recipe.api.integrations.ollama.GenerateResponse;
import es.agonzalez.assistant.recipe.api.integrations.ollama.OllamaClient;

@Service
public class OllamaClientImpl implements OllamaClient{

    @Autowired
    private  AiProperties props;
    private final RestTemplate restTemplate = buildTemplate();

    @SuppressWarnings("null")
    @Override
    public String generate(String prompt) {
        String url = props.getUrl().replace("/+$", "") + "/api/generate";
        GenerateRequest request = new GenerateRequest(props.getModel(), prompt, false, 0.2);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<GenerateRequest> req = new HttpEntity<>(request, headers);

        int attempts = 0;
        RestClientException last = null;
        while(attempts++ < 2) {
            try {
                ResponseEntity<GenerateResponse> response = restTemplate.exchange(url, HttpMethod.POST, req, GenerateResponse.class);
                if(response == null || !response.getStatusCode().is2xxSuccessful() || response.getBody() == null) {
                    throw new IllegalStateException("Unexpected AI response: " + response.getStatusCode());
                }
    
                return response.getBody().response();   
            } catch (RestClientException e) {
                last = e;

                try { Thread.sleep(300L * attempts); } catch (InterruptedException ignored) {}
            }
        }

        throw new IllegalStateException("Could connect IA Server " + (last != null ? last.getMessage() : "Unknown"));
    }

    private RestTemplate buildTemplate() {
        var factory = new HttpComponentsClientHttpRequestFactory();
        int timeout = 20_000;
        factory.setConnectTimeout(timeout);
        return new RestTemplate(factory);
    }

}
