package ie.ramos.modak.mapper;

import ie.ramos.modak.dto.ErrorDTO;
import ie.ramos.modak.exception.ApiException;
import org.springframework.stereotype.Component;

@Component
public class ErrorMapper {

    public ErrorDTO mapToDTO(ApiException apiException) {
        return ErrorDTO.builder()
                .id(apiException.getId())
                .title(apiException.getTitle())
                .description(apiException.getDescription())
                .httpStatus(apiException.getHttpStatus().value())
                .date(apiException.getDate())
                .build();
    }

}
