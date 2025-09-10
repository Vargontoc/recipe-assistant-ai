package es.agonzalez.assistant.recipe.api.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.lang.NonNull;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Web configuration for interceptors and performance optimizations
 */
@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Autowired
    private RateLimitInterceptor rateLimitInterceptor;

    @Override
    public void addInterceptors(@NonNull InterceptorRegistry registry) {
        // Apply rate limiting to all API endpoints
        registry.addInterceptor(rateLimitInterceptor)
                .addPathPatterns("/api/**")
                .excludePathPatterns(
                        "/api/actuator/**",  // Exclude health checks
                        "/api/health/**",    // Exclude health endpoints
                        "/api/swagger-ui/**", // Exclude Swagger UI
                        "/api/v3/api-docs/**" // Exclude OpenAPI docs
                );
    }
}
