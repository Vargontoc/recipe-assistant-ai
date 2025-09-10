package es.agonzalez.assistant.recipe.api.controllers;

import es.agonzalez.assistant.recipe.api.services.MetricsService;
import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * Controller for application performance and health monitoring
 */
@RestController
@RequestMapping("/api/performance")
public class PerformanceController {

    @Autowired
    private MetricsService metricsService;
    
    @Autowired
    private CacheManager cacheManager;
    
    @Autowired
    private MeterRegistry meterRegistry;

    /**
     * Get application performance statistics
     */
    @GetMapping("/stats")
    public ResponseEntity<Map<String, Object>> getPerformanceStats() {
        Map<String, Object> stats = new HashMap<>();
        
        // Cache information
        Map<String, Object> cacheInfo = new HashMap<>();
        var cacheNames = cacheManager.getCacheNames();
        cacheInfo.put("available_caches", cacheNames);
        cacheInfo.put("cache_count", cacheNames.size());
        stats.put("cache", cacheInfo);
        
        // JVM information
        Map<String, Object> jvmInfo = new HashMap<>();
        Runtime runtime = Runtime.getRuntime();
        jvmInfo.put("total_memory_mb", runtime.totalMemory() / (1024 * 1024));
        jvmInfo.put("free_memory_mb", runtime.freeMemory() / (1024 * 1024));
        jvmInfo.put("used_memory_mb", (runtime.totalMemory() - runtime.freeMemory()) / (1024 * 1024));
        jvmInfo.put("max_memory_mb", runtime.maxMemory() / (1024 * 1024));
        jvmInfo.put("available_processors", runtime.availableProcessors());
        stats.put("jvm", jvmInfo);
        
        // Application information
        Map<String, Object> appInfo = new HashMap<>();
        appInfo.put("status", "running");
        appInfo.put("uptime_ms", System.currentTimeMillis());
        stats.put("application", appInfo);
        
        return ResponseEntity.ok(stats);
    }

    /**
     * Get cache statistics
     */
    @GetMapping("/cache")
    public ResponseEntity<Map<String, Object>> getCacheStats() {
        Map<String, Object> cacheStats = new HashMap<>();
        
        for (String cacheName : cacheManager.getCacheNames()) {
            var cache = cacheManager.getCache(cacheName);
            if (cache != null) {
                Map<String, Object> cacheDetail = new HashMap<>();
                cacheDetail.put("name", cacheName);
                cacheDetail.put("native_cache_type", cache.getNativeCache().getClass().getSimpleName());
                cacheStats.put(cacheName, cacheDetail);
            }
        }
        
        return ResponseEntity.ok(cacheStats);
    }

    /**
     * Record a custom performance metric
     */
    @GetMapping("/record-metric")
    public ResponseEntity<String> recordCustomMetric() {
        metricsService.recordCustomMetric("demo.metric", "Demo metric for testing", 1.0);
        return ResponseEntity.ok("Metric recorded successfully");
    }

    /**
     * Simple health check
     */
    @GetMapping("/health")
    public ResponseEntity<Map<String, String>> healthCheck() {
        Map<String, String> health = new HashMap<>();
        health.put("status", "UP");
        health.put("timestamp", String.valueOf(System.currentTimeMillis()));
        return ResponseEntity.ok(health);
    }
}
