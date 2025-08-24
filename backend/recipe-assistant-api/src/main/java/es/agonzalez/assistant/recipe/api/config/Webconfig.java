package es.agonzalez.assistant.recipe.api.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class Webconfig implements  WebMvcConfigurer{
    
    @Primary  
    public WebMvcConfigurer corsConfigurar() {
        return new WebMvcConfigurer() {
            @SuppressWarnings("null")
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                
                registry.addMapping("/api/**")
                        .allowedOrigins("*") // Allow all origins
                        .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS", "PATCH") // Allow specific HTTP methods
                        .allowedHeaders("*").allowCredentials(false); // Allow all headers
            }
            
            
        };
    }
}
