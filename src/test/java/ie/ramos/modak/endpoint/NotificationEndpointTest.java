package ie.ramos.modak.endpoint;


import ie.ramos.modak.domain.Notification;
import ie.ramos.modak.dto.NotificationDTO;
import ie.ramos.modak.endpoint.validator.NotificationValidator;
import ie.ramos.modak.mapper.NotificationMapper;
import ie.ramos.modak.service.NotificationService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class NotificationEndpointTest {

    @InjectMocks
    private NotificationEndpoint endpoint;

    @Mock
    private NotificationMapper mapperMock;

    @Mock
    private NotificationValidator validatorMock;

    @Mock
    private NotificationService serviceMock;


    @Test
    public void testSendOK() {
        var notificationDTO = new NotificationDTO();
        var notification = new Notification();

        when(mapperMock.mapToBO(notificationDTO))
                .thenReturn(notification);

        var result = endpoint.send(notificationDTO);
        assertThat(result).isEqualTo(notificationDTO);

        verify(validatorMock).validate(notificationDTO);
        verify(mapperMock).mapToBO(notificationDTO);
        verify(serviceMock).send(notification);
    }

}
