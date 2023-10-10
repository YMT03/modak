package ie.ramos.modak.ratelimit;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.Duration;

@AllArgsConstructor
@Getter
public class RateLimitRule {
    private long max;
    private Duration window;
}
