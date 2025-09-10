package es.agonzalez.assistant.recipe.api.integrations.multiagent;

import es.agonzalez.assistant.recipe.api.dtos.SuggestRequest;
import es.agonzalez.assistant.recipe.api.dtos.SuggestResponse;

/**
 * Client interface for communicating with the Multi-Agent Service
 * for AI-powered recipe generation and suggestions.
 */
public interface MultiAgentClient {
    
    /**
     * Generate recipe suggestions using the multi-agent service
     * 
     * @param request the suggestion request containing user preferences and ingredients
     * @return the AI-generated recipe suggestions
     */
    SuggestResponse generateRecipeSuggestions(SuggestRequest request);
    
    /**
     * Check if the multi-agent service is available
     * 
     * @return true if the service is healthy and responsive
     */
    boolean isServiceAvailable();
}
