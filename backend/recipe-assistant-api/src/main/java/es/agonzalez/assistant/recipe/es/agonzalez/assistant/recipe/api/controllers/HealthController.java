package es.agonzalez.assistant.recipe.es.agonzalez.assistant.recipe.api.controllers;

import java.util.Map;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HealthController 
{
    @GetMapping("/api/health")
    public Map<String, Object> ping() {
        return Map.of(
            "status", "ok"
        );
    }    
}
