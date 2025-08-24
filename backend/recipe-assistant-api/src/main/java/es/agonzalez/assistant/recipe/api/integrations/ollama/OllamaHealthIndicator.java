package es.agonzalez.assistant.recipe.api.integrations.ollama;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.web.client.RestTemplate;

import es.agonzalez.assistant.recipe.api.config.AiProperties;

public class OllamaHealthIndicator implements HealthIndicator {
    
    @Autowired
    private  AiProperties props;
    private final RestTemplate rt = new RestTemplate();

    @Override
    public Health health() {
        try {
            var url = props.getUrl().replaceAll("/+$", "") + "/api/tags";
            rt.getForEntity(url, String.class);
            return Health.up().withDetail("ollama", "reachable").build();
        } catch (Exception e) {
            return Health.down().withDetail("ollama", "unreachable").build();
        }
    }
}
