package es.agonzalez.assistant.recipe.api.config;

import java.time.Duration;
import java.util.Arrays;

import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.caffeine.CaffeineCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import com.github.benmanes.caffeine.cache.Caffeine;

/**
 * Cache configuration for improving application performance
 */
@Configuration
@EnableCaching
public class CacheConfig {

    /**
     * Configure Caffeine cache manager with different cache configurations
     */
    @Bean
    @Primary
    public CacheManager cacheManager() {
        CaffeineCacheManager cacheManager = new CaffeineCacheManager();
        
        // Default cache configuration
        cacheManager.setCaffeine(Caffeine.newBuilder()
                .initialCapacity(100)
                .maximumSize(1000)
                .expireAfterWrite(Duration.ofMinutes(30))
                .recordStats());
        
        // Cache names that will be used
        cacheManager.setCacheNames(Arrays.asList(
                "recipes",
                "search-filters", 
                "recipe-stats",
                "user-favorites",
                "search-results"
        ));
        
        return cacheManager;
    }

    /**
     * Specific cache configuration for search results (shorter expiration)
     */
    @Bean("searchResultsCacheManager")
    public CacheManager searchResultsCacheManager() {
        CaffeineCacheManager cacheManager = new CaffeineCacheManager("search-results");
        cacheManager.setCaffeine(Caffeine.newBuilder()
                .initialCapacity(50)
                .maximumSize(500)
                .expireAfterWrite(Duration.ofMinutes(10)) // Shorter expiration for search results
                .recordStats());
        return cacheManager;
    }

    /**
     * Long-term cache for static data like filters
     */
    @Bean("staticDataCacheManager")
    public CacheManager staticDataCacheManager() {
        CaffeineCacheManager cacheManager = new CaffeineCacheManager("search-filters");
        cacheManager.setCaffeine(Caffeine.newBuilder()
                .initialCapacity(10)
                .maximumSize(50)
                .expireAfterWrite(Duration.ofHours(2)) // Longer expiration for static data
                .recordStats());
        return cacheManager;
    }
}
