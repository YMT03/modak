package ie.ramos.modak.ratelimit;

import ie.ramos.modak.exception.ExceptionFactory;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Optional;

@Component
@AllArgsConstructor
public class RateLimitRuleStrategy {

    private Map<String, RateLimitRule> rules;
    private ExceptionFactory exceptionFactory;

    public RateLimitRule getRule(String key) {
        return Optional.ofNullable(rules.get(key)).stream()
                .findFirst()
                .orElseThrow(()-> exceptionFactory.buildNotFound("rule_not_found", "Rate Limit rule not found", String.format("None rule found with key %s", key)));
    }

}
