package ie.ramos.modak.ratelimit;

import lombok.AllArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;

import static java.lang.Double.NEGATIVE_INFINITY;
import static java.lang.System.currentTimeMillis;
import static java.util.concurrent.TimeUnit.MILLISECONDS;

@AllArgsConstructor
@Service
public class RateLimitService {

    private final RedisTemplate<String, String> redisTemplate;
    private final RateLimitRuleStrategy strategy;

    public boolean isExceeded(String key, String ruleKey) {
        RateLimitRule rule = strategy.getRule(ruleKey);
        long currentTime = currentTimeMillis();
        removeOldValuesFromWindow(key, currentTime, rule.getWindow());
        return isExceeded(key, currentTime,  redisTemplate.opsForZSet().zCard(key), rule.getMax(), rule.getWindow());
    }


    private void removeOldValuesFromWindow(String key, long currentTime, Duration window) {
        long windowStart = currentTime - window.toMillis();
        redisTemplate.opsForZSet().removeRangeByScore(key, NEGATIVE_INFINITY, windowStart);
    }

    private boolean isExceeded(String key, long currentTime, Long currentCalls, long max, Duration window) {
        if (currentCalls == null|| currentCalls < max) {
            redisTemplate.opsForZSet().add(key, String.valueOf(currentTime), currentTime);
            redisTemplate.expire(key, window.toMillis(), MILLISECONDS);
            return false;
        }

        return true;
    }

}
