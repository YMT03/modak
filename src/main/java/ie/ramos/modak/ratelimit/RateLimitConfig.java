package ie.ramos.modak.ratelimit;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class RateLimitConfig {

    @Bean
    @ConfigurationProperties(prefix = "app.notifications.rate-limit-rules")
    public Map<String, RateLimitRule> rateLimitRules() {
        return new HashMap<>();
    }

}
