package es.agonzalez.assistant.recipe.api.services.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import es.agonzalez.assistant.recipe.api.dtos.Preferences;
import es.agonzalez.assistant.recipe.api.dtos.PreferencesResponse;
import es.agonzalez.assistant.recipe.api.dtos.SuggestRequest;
import es.agonzalez.assistant.recipe.api.dtos.SuggestResponse;
import es.agonzalez.assistant.recipe.api.integrations.multiagent.MultiAgentClient;
import es.agonzalez.assistant.recipe.api.services.SuggestionHistoryService;
import es.agonzalez.assistant.recipe.api.services.SuggestionService;
import es.agonzalez.assistant.recipe.api.services.UserPreferencesService;

@Service
public class SuggestionServiceImpl implements SuggestionService {

    @Autowired
    private MultiAgentClient multiAgentClient;
    
    @Autowired
    private SuggestionHistoryService historyService;
    
    @Autowired
    private UserPreferencesService preferencesService;

    @Override
    public SuggestResponse suggest(SuggestRequest req) {
        long startTime = System.currentTimeMillis();
        
        // Ensure preferences are set
        if (req.getPreferences() == null) {
            req.setPreferences(mapPreferences(preferencesService.getOrCreate()));
        }
        
        try {
            // Delegate to multi-agent service for AI processing
            SuggestResponse response = multiAgentClient.generateRecipeSuggestions(req);
            
            // Save to history
            long processingTime = System.currentTimeMillis() - startTime;
            historyService.save("multi-agent-service", req.getIngredients(), req.getPreferences(), response, processingTime);
            
            return response;
            
        } catch (Exception e) {
            // Fallback response in case of service failure
            return createFallbackResponse("Error processing request: " + e.getMessage());
        }
    }

    /**
     * Map PreferencesResponse to Preferences DTO
     */
    private Preferences mapPreferences(PreferencesResponse preferencesResponse) {
        Preferences preferences = new Preferences();
        preferences.setDiet(preferencesResponse.diet());
        preferences.setExludeIngredients(preferencesResponse.excludes());
        preferences.setAllergens(preferencesResponse.allergns());
        return preferences;
    }

    /**
     * Create a fallback response when the service is unavailable
     */
    private SuggestResponse createFallbackResponse(String message) {
        return new SuggestResponse(
            "Service Unavailable",
            "Unable to generate recipe suggestions at this time: " + message,
            java.util.List.of("Please try again later"),
            java.util.Set.of("error")
        );
    }
}
