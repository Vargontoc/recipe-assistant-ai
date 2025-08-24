package es.agonzalez.assistant.recipe.api.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;



@Component
public class AiProperties {
    @Value("${ai.ollama.model}")
    private String model;
    @Value("${ai.ollama.url}")
    private String url;

    public String getUrl() {
        return url;
    }

    public String getModel() {
        return model;
    }
}
