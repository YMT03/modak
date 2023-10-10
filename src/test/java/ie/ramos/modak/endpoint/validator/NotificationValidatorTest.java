package ie.ramos.modak.endpoint.validator;


import ie.ramos.modak.dto.NotificationDTO;
import ie.ramos.modak.exception.ApiException;
import ie.ramos.modak.exception.ExceptionFactory;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.http.HttpStatus.BAD_REQUEST;

@ExtendWith(MockitoExtension.class)
public class NotificationValidatorTest {

    @InjectMocks
    private NotificationValidator validator;

    @Mock
    private ExceptionFactory exceptionFactory;


    @Test
    public void testValidate_NullNotification_Throws() {
        assertThrown(null, "empty_notification_body", "Empty notification body", "Notification body cannot be empty");
    }

    @Test
    public void testValidate_NullUserId_Throws() {
        assertThrown(NotificationDTO.builder().build(), "empty_user_id", "Empty User ID", "User ID cannot be empty");
    }

    @Test
    public void testValidate_BlankUserId_Throws() {
        assertThrown(NotificationDTO.builder().userId("").build(), "empty_user_id", "Empty User ID", "User ID cannot be empty");
    }

    @Test
    public void testValidate_WhiteSpacesUserId_Throws() {
        assertThrown(NotificationDTO.builder().userId("   ").build(), "empty_user_id", "Empty User ID", "User ID cannot be empty");
    }

    @Test
    public void testValidate_NullMessage_Throws() {
        assertThrown(NotificationDTO.builder().userId("1").type("news").build(), "empty_notification_message", "Empty notification message", "Notification message cannot be empty");
    }

    @Test
    public void testValidate_BlankMessage_Throws() {
        assertThrown(NotificationDTO.builder().userId("1").type("news").message("").build(), "empty_notification_message", "Empty notification message", "Notification message cannot be empty");
    }

    @Test
    public void testValidate_WhiteSpacesMessage_Throws() {
        assertThrown(NotificationDTO.builder().userId("1").type("news").message("   ").build(), "empty_notification_message", "Empty notification message", "Notification message cannot be empty");
    }

    @Test
    public void testValidate_NullType_Throws() {
        assertThrown(NotificationDTO.builder().userId("1").build(), "empty_notification_type", "Empty notification type", "Notification type cannot be empty");
    }

    @Test
    public void testValidate_BlankType_Throws() {
        assertThrown(NotificationDTO.builder().userId("1").type("").build(), "empty_notification_type", "Empty notification type", "Notification type cannot be empty");
    }

    @Test
    public void testValidate_WhiteSpacesType_Throws() {
        assertThrown(NotificationDTO.builder().userId("1").type("   ").build(), "empty_notification_type", "Empty notification type", "Notification type cannot be empty");
    }

    @Test
    public void testValidate_InvalidType_Throws() {
        assertThrown(NotificationDTO.builder().userId("1").type("marketing").message("some msg").type("invalid").build(), "invalid_notification_type", "Invalid notification type", "Notification type value is not valid");
    }


    private void assertThrown(NotificationDTO notificationDTO, String id, String title, String description) {
        when(exceptionFactory.buildBadRequest(id, title, description))
                .thenReturn(new ApiException(id, title, description, BAD_REQUEST));

        assertThatThrownBy(()-> validator.validate(notificationDTO))
                .hasFieldOrPropertyWithValue("id", id)
                .hasFieldOrPropertyWithValue("title", title)
                .hasFieldOrPropertyWithValue("description", description)
                .hasFieldOrPropertyWithValue("httpStatus", BAD_REQUEST);

        verify(exceptionFactory).buildBadRequest(id, title, description);
    }


}
