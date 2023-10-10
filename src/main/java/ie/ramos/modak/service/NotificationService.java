package ie.ramos.modak.service;

import ie.ramos.modak.domain.Notification;
import ie.ramos.modak.exception.ApiException;
import ie.ramos.modak.exception.ExceptionFactory;
import ie.ramos.modak.ratelimit.RateLimitService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;


@Service
@AllArgsConstructor
@Slf4j
public class NotificationService {

    private final RateLimitService rateLimitService;
    private final ExceptionFactory exceptionFactory;

    public void send(Notification notification) {
        if (rateLimitService.isExceeded(buildKey(notification), notification.getType().getValue())) {
            throw buildTooManyRequestsException(notification);
        }

        log.info("Message has been sent successfully");
    }

    private ApiException buildTooManyRequestsException(Notification notification) {
        return exceptionFactory.buildTooManyRequest(
                String.format("%s_notifications_exceeded", notification.getType().getValue()),
                "Too Many Requests",
                String.format("Notifications of type %s have been exceeded for user %s", notification.getType(), notification.getUserId())
        );
    }

    private String buildKey(Notification notification) {
        return String.format("%s-%s", notification.getType().getValue(), notification.getUserId());
    }

}
