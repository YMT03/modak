package ie.ramos.modak.ratelimit;

import ie.ramos.modak.exception.ApiException;
import ie.ramos.modak.exception.ExceptionFactory;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Duration;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.http.HttpStatus.NOT_FOUND;


@ExtendWith(MockitoExtension.class)
public class RateLimitStrategyTest {

    @InjectMocks
    private RateLimitRuleStrategy strategy;

    @Mock
    private ExceptionFactory exceptionFactoryMock;

    @Mock
    private Map<String, RateLimitRule> rulesMock;

    @Test
    public void testGetRuleOK() {
        var key = "rule-key";
        var rule = buildRateLimitRule(1L, Duration.ofDays(1));

        when(rulesMock.get(key))
                .thenReturn(rule);

        var retrieved = strategy.getRule(key);

        assertThat(retrieved).isEqualTo(rule);

        verify(rulesMock).get(key);
    }

    @Test
    public void testGetRule_NotFound_Throws() {
        var key = "rule-key";
        var id = "rule_not_found";
        var title = "Rate Limit rule not found";
        var description = "None rule found with key rule-key";

        when(exceptionFactoryMock.buildNotFound(id, title, description))
                .thenReturn(new ApiException(id, title, description, NOT_FOUND));

        assertThatThrownBy(()-> strategy.getRule(key))
                .isExactlyInstanceOf(ApiException.class)
                .hasFieldOrPropertyWithValue("id", id)
                .hasFieldOrPropertyWithValue("title", title)
                .hasFieldOrPropertyWithValue("description", description)
                .hasFieldOrPropertyWithValue("httpStatus", NOT_FOUND);

        verify(rulesMock).get(key);
        verify(exceptionFactoryMock).buildNotFound(id, title, description);
    }



    private RateLimitRule buildRateLimitRule(Long max, Duration window) {
        return new RateLimitRule(max, window);
    }
}
