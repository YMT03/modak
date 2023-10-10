package ie.ramos.modak.service;

import ie.ramos.modak.domain.Notification;
import ie.ramos.modak.domain.Type;
import ie.ramos.modak.exception.ApiException;
import ie.ramos.modak.exception.ExceptionFactory;
import ie.ramos.modak.ratelimit.RateLimitService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static ie.ramos.modak.domain.Type.MARKETING;
import static ie.ramos.modak.domain.Type.NEWS;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;
import static org.springframework.http.HttpStatus.TOO_MANY_REQUESTS;

@ExtendWith(MockitoExtension.class)
public class NotificationServiceTest {

    @InjectMocks
    private NotificationService service;

    @Mock
    private RateLimitService rateLimitServiceMock;

    @Mock
    private ExceptionFactory exceptionFactoryMock;

    @Test
    public void testSendOK() {
        var notification = buildNotification(NEWS, "1");

        when(rateLimitServiceMock.isExceeded("news-1", NEWS.getValue()))
                .thenReturn(false);

        service.send(notification);

        verify(rateLimitServiceMock).isExceeded("news-1", NEWS.getValue());
        verify(exceptionFactoryMock, never()).buildTooManyRequest(any(), any(), any());
    }

    @Test
    public void testSendExceeded() {
        var id = "marketing_notifications_exceeded";
        var title = "Too Many Requests";
        var description = "Notifications of type MARKETING have been exceeded for user 1";

        var notification = buildNotification(MARKETING, "1");

        when(rateLimitServiceMock.isExceeded("marketing-1", MARKETING.getValue()))
                .thenReturn(true);

        when(exceptionFactoryMock.buildTooManyRequest(id, title, description))
                .thenReturn(new ApiException(id, title, description, TOO_MANY_REQUESTS));


        var exception = assertThrows(ApiException.class, ()-> service.send(notification));
        assertThat(exception)
                .hasFieldOrPropertyWithValue("id", id)
                .hasFieldOrPropertyWithValue("title", title)
                .hasFieldOrPropertyWithValue("description", description)
                .hasFieldOrPropertyWithValue("httpStatus", TOO_MANY_REQUESTS);

        verify(rateLimitServiceMock).isExceeded("marketing-1", MARKETING.getValue());
        verify(exceptionFactoryMock).buildTooManyRequest(id, title, description);
    }

    private Notification buildNotification(Type type, String userId) {
        return Notification.builder()
                .type(type)
                .userId(userId)
                .build();
    }
}
