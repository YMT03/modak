package ie.ramos.modak.endpoint.validator;

import ie.ramos.modak.domain.Type;
import ie.ramos.modak.dto.NotificationDTO;
import ie.ramos.modak.exception.ExceptionFactory;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import static java.util.Arrays.stream;
import static java.util.Objects.isNull;
import static org.apache.logging.log4j.util.Strings.isBlank;

@Component
@AllArgsConstructor
public class NotificationValidator {
    private final ExceptionFactory exceptionFactory;

    public void validate(NotificationDTO notificationDTO) {
        if (isNull(notificationDTO)) {
            throw exceptionFactory.buildBadRequest("empty_notification_body", "Empty notification body", "Notification body cannot be empty");
        }

        if (isBlank(notificationDTO.getUserId())) {
            throw exceptionFactory.buildBadRequest("empty_user_id", "Empty User ID", "User ID cannot be empty");
        }

        if (isBlank(notificationDTO.getType())) {
            throw exceptionFactory.buildBadRequest("empty_notification_type", "Empty notification type", "Notification type cannot be empty");
        }

        if (isBlank(notificationDTO.getMessage())) {
            throw exceptionFactory.buildBadRequest("empty_notification_message", "Empty notification message", "Notification message cannot be empty");
        }


        if (isNotValid(notificationDTO.getType())) {
            throw exceptionFactory.buildBadRequest("invalid_notification_type", "Invalid notification type", "Notification type value is not valid");
        }
    }

    private boolean isNotValid(String notificationType) {
        return stream(Type.values()).noneMatch(type -> type.getValue().equalsIgnoreCase(notificationType));
    }
}
