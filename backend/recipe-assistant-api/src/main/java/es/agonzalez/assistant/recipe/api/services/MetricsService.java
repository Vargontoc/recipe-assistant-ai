package es.agonzalez.assistant.recipe.api.services;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Timer;
import org.springframework.stereotype.Service;

/**
 * Service for tracking application metrics and performance
 */
@Service
public class MetricsService {

    private final MeterRegistry meterRegistry;
    private final Timer searchTimer;
    private final Timer suggestionTimer;

    public MetricsService(MeterRegistry meterRegistry) {
        this.meterRegistry = meterRegistry;
        
        // Initialize timers
        this.searchTimer = Timer.builder("recipe.search.duration")
                .description("Time taken for recipe searches")
                .register(meterRegistry);
                
        this.suggestionTimer = Timer.builder("recipe.suggestion.duration")
                .description("Time taken for AI recipe suggestions")
                .register(meterRegistry);
    }

    /**
     * Record a recipe search event
     */
    public void recordSearch(String searchType) {
        Counter.builder("recipe.search.count")
                .description("Number of recipe searches performed")
                .tags("type", searchType)
                .register(meterRegistry)
                .increment();
    }

    /**
     * Record a recipe view event
     */
    public void recordRecipeView(String recipeId) {
        Counter.builder("recipe.view.count")
                .description("Number of recipe views")
                .tags("recipe", recipeId)
                .register(meterRegistry)
                .increment();
    }

    /**
     * Record a favorite action
     */
    public void recordFavorite(String action) {
        Counter.builder("recipe.favorite.count")
                .description("Number of recipes favorited")
                .tags("action", action)
                .register(meterRegistry)
                .increment();
    }

    /**
     * Record a rating submission
     */
    public void recordRating(int rating) {
        Counter.builder("recipe.rating.count")
                .description("Number of recipe ratings submitted")
                .tags("stars", String.valueOf(rating))
                .register(meterRegistry)
                .increment();
    }

    /**
     * Get search timer for measuring search duration
     */
    public Timer.Sample startSearchTimer() {
        return Timer.start(meterRegistry);
    }

    /**
     * Record search duration
     */
    public void recordSearchDuration(Timer.Sample sample) {
        sample.stop(searchTimer);
    }

    /**
     * Get suggestion timer for measuring AI suggestion duration
     */
    public Timer.Sample startSuggestionTimer() {
        return Timer.start(meterRegistry);
    }

    /**
     * Record suggestion duration
     */
    public void recordSuggestionDuration(Timer.Sample sample) {
        sample.stop(suggestionTimer);
    }

    /**
     * Record custom metric
     */
    public void recordCustomMetric(String name, String description, double value) {
        meterRegistry.gauge(name, value);
    }

    /**
     * Increment custom counter
     */
    public void incrementCustomCounter(String name, String description, String... tags) {
        Counter.builder(name)
                .description(description)
                .tags(tags)
                .register(meterRegistry)
                .increment();
    }
}
