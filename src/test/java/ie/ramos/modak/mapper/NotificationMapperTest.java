package ie.ramos.modak.mapper;

import ie.ramos.modak.domain.Notification;
import ie.ramos.modak.domain.Type;
import ie.ramos.modak.dto.NotificationDTO;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class NotificationMapperTest {

    private final NotificationMapper mapper = new NotificationMapper();


    @Test
    public void testMapToBO() {
        var notificationDTO = buildNotificationDTO("news", "1", "3");
        var expected = buildNotification(Type.NEWS, "1", "3");

        var retrieved = mapper.mapToBO(notificationDTO);

        assertThat(retrieved).isEqualTo(expected);
    }

    private Notification buildNotification(Type type, String userId, String message) {
        return Notification.builder()
                .type(type)
                .userId(userId)
                .message(message)
                .build();
    }

    private NotificationDTO buildNotificationDTO(String type, String userId, String message) {
        return NotificationDTO.builder()
                .type(type)
                .userId(userId)
                .message(message)
                .build();
    }
}
