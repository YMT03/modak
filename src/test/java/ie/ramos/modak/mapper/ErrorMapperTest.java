package ie.ramos.modak.mapper;


import ie.ramos.modak.dto.ErrorDTO;
import ie.ramos.modak.exception.ApiException;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import java.time.OffsetDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;

public class ErrorMapperTest {

    private final ErrorMapper errorMapper = new ErrorMapper();

    @Test
    public void testMapToDTO_OK() {
        var errorId = "some_error_id";
        var title = "Some Title";
        var description = "Some description";
        var apiException = buildApiException(errorId, title, description, INTERNAL_SERVER_ERROR, new RuntimeException());

        var expected = buildErrorDTO(errorId, title, description, 500, apiException.getDate());

        var retrieved = errorMapper.mapToDTO(apiException);

        assertThat(retrieved).isEqualTo(expected);
    }

    private ErrorDTO buildErrorDTO(String id, String title, String description, int status, OffsetDateTime date) {
        return ErrorDTO.builder()
                .id(id)
                .title(title)
                .description(description)
                .httpStatus(status)
                .date(date)
                .build();
    }

    private ApiException buildApiException(String id, String title, String description, HttpStatus status, Throwable cause) {
        return new ApiException(id, title, description, status, cause);
    }
}
