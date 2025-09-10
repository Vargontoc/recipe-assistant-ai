package es.agonzalez.assistant.recipe.api.health;

import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Component;

/**
 * Custom health indicator for cache status
 */
@Component
public class CacheHealthIndicator implements HealthIndicator {

    private final CacheManager cacheManager;

    public CacheHealthIndicator(CacheManager cacheManager) {
        this.cacheManager = cacheManager;
    }

    @Override
    public Health health() {
        try {
            // Check if cache manager is working
            if (cacheManager == null) {
                return Health.down()
                        .withDetail("cache", "Cache manager is not available")
                        .build();
            }

            // Get cache names and their status
            var cacheNames = cacheManager.getCacheNames();
            int activeCaches = 0;
            
            for (String cacheName : cacheNames) {
                var cache = cacheManager.getCache(cacheName);
                if (cache != null) {
                    activeCaches++;
                }
            }

            if (activeCaches > 0) {
                return Health.up()
                        .withDetail("cache.manager", cacheManager.getClass().getSimpleName())
                        .withDetail("cache.active", activeCaches)
                        .withDetail("cache.names", cacheNames)
                        .build();
            } else {
                return Health.down()
                        .withDetail("cache", "No active caches found")
                        .build();
            }

        } catch (Exception e) {
            return Health.down()
                    .withDetail("cache", "Cache health check failed")
                    .withDetail("error", e.getMessage())
                    .build();
        }
    }
}
