package ie.ramos.modak.endpoint;

import ie.ramos.modak.dto.NotificationDTO;
import ie.ramos.modak.endpoint.validator.NotificationValidator;
import ie.ramos.modak.mapper.NotificationMapper;
import ie.ramos.modak.service.NotificationService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.http.ResponseEntity.ok;

@AllArgsConstructor
@RestController
public class NotificationEndpoint {

    private final NotificationValidator validator;
    private final NotificationService service;
    private final NotificationMapper mapper;

    @PostMapping("/api/notifications")
    public ResponseEntity<String> send(@RequestBody NotificationDTO notificationDTO) {
        validator.validate(notificationDTO);
        service.send(mapper.mapToBO(notificationDTO));
        return ok("Notification has been sent successfully");
    }

}
