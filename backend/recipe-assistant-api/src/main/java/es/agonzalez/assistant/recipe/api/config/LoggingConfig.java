package es.agonzalez.assistant.recipe.api.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.filter.CommonsRequestLoggingFilter;

@Configuration
public class LoggingConfig {
    @Bean
    public CommonsRequestLoggingFilter log()
    {
        var log = new CommonsRequestLoggingFilter();
        log.setIncludeClientInfo(true);
        log.setIncludeQueryString(true);
        log.setIncludePayload(true);
        log.setMaxPayloadLength(1024);
        log.setIncludeHeaders(true);
        return log;
    }
}
