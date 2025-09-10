package es.agonzalez.assistant.recipe.api.controllers;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import es.agonzalez.assistant.recipe.api.integrations.multiagent.MultiAgentClient;

@RestController
@RequestMapping("/api/health")
public class HealthController 
{   
    @Autowired
    private MultiAgentClient client;
    @GetMapping()
    public Map<String, Object> ping() {
        return Map.of(
            "status", "ok",
            "multiagent", client.isServiceAvailable()
        );
    }    
}
