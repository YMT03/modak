package ie.ramos.modak.mapper;

import ie.ramos.modak.domain.Notification;
import ie.ramos.modak.domain.Type;
import ie.ramos.modak.dto.NotificationDTO;
import org.springframework.stereotype.Component;

@Component
public class NotificationMapper {

    public Notification mapToBO(NotificationDTO notificationDTO) {
        return Notification.builder()
                .userId(notificationDTO.getUserId())
                .message(notificationDTO.getMessage())
                .type(Type.valueOf(notificationDTO.getType().toUpperCase()))
                .build();
    }

}
