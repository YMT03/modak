package ie.ramos.modak.ratelimit;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;

import static java.lang.Double.NEGATIVE_INFINITY;
import static java.time.Duration.ofDays;
import static java.util.concurrent.TimeUnit.MILLISECONDS;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class RateLimitServiceTest {

    @InjectMocks
    private RateLimitService service;
    @Mock
    private RedisTemplate<String, String> redisTemplateMock;
    @Mock
    private RateLimitRuleStrategy strategyMock;
    @Mock
    private ZSetOperations<String, String> zSetOperationsMock;


    @Test
    public void testIsExceeded_IsRuleExceeded_ReturnsTrue() {
        var key = "object-key";
        var ruleKey = "rule-key";
        RateLimitRule rule = new RateLimitRule(1L, ofDays(1));

        when(redisTemplateMock.opsForZSet())
                .thenReturn(zSetOperationsMock);

        when(strategyMock.getRule(ruleKey))
                .thenReturn(rule);

        when(zSetOperationsMock.zCard(key))
                .thenReturn(2L);

        Boolean result = service.isExceeded(key, ruleKey);

        assertThat(result).isTrue();

        verify(strategyMock).getRule(ruleKey);
        verify(zSetOperationsMock).removeRangeByScore(eq(key), eq(NEGATIVE_INFINITY), anyDouble());
        verify(zSetOperationsMock).zCard(key);
        verify(redisTemplateMock, times(2)).opsForZSet();
        verify(redisTemplateMock, never()).expire(any(), anyLong(), any());
    }

    @Test
    public void testIsExceeded_RuleIsNotExceeded_ReturnsFalse() {
        var key = "object-key";
        var ruleKey = "rule-key";
        RateLimitRule rule = new RateLimitRule(3L, ofDays(1));

        when(redisTemplateMock.opsForZSet())
                .thenReturn(zSetOperationsMock);

        when(strategyMock.getRule(ruleKey))
                .thenReturn(rule);

        when(zSetOperationsMock.zCard(key))
                .thenReturn(2L);

        Boolean result = service.isExceeded(key, ruleKey);

        assertThat(result).isFalse();

        verify(strategyMock).getRule(ruleKey);
        verify(zSetOperationsMock).removeRangeByScore(eq(key), eq(NEGATIVE_INFINITY), anyDouble());
        verify(zSetOperationsMock).zCard(key);
        verify(zSetOperationsMock).add(eq(key), any(), anyDouble());
        verify(redisTemplateMock, times(3)).opsForZSet();
        verify(redisTemplateMock).expire(key, rule.getWindow().toMillis(), MILLISECONDS);
    }

    @Test
    public void testIsExceeded_NullZCard_ReturnsFalse() {
        var key = "object-key";
        var ruleKey = "rule-key";
        RateLimitRule rule = new RateLimitRule(1L, ofDays(1));

        when(redisTemplateMock.opsForZSet())
                .thenReturn(zSetOperationsMock);

        when(strategyMock.getRule(ruleKey))
                .thenReturn(rule);

        Boolean result = service.isExceeded(key, ruleKey);

        assertThat(result).isFalse();

        verify(strategyMock).getRule(ruleKey);
        verify(zSetOperationsMock).removeRangeByScore(eq(key), eq(NEGATIVE_INFINITY), anyDouble());
        verify(zSetOperationsMock).zCard(key);
        verify(zSetOperationsMock).add(eq(key), any(), anyDouble());
        verify(redisTemplateMock, times(3)).opsForZSet();
        verify(redisTemplateMock).expire(key, rule.getWindow().toMillis(), MILLISECONDS);
    }
}
