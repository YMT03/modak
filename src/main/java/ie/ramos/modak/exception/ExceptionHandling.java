package ie.ramos.modak.exception;


import ie.ramos.modak.dto.ErrorDTO;
import ie.ramos.modak.mapper.ErrorMapper;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.NoHandlerFoundException;

import static org.springframework.http.HttpStatusCode.valueOf;

@RestControllerAdvice
@AllArgsConstructor
@Slf4j
public class ExceptionHandling {

    private final ExceptionFactory exceptionFactory;
    private final ErrorMapper errorMapper;

    @ExceptionHandler(NoHandlerFoundException.class)
    public ResponseEntity<ErrorDTO> handle(NoHandlerFoundException e) {
        return handleResponse(exceptionFactory.buildNoMappingFound(e));
    }

    @ExceptionHandler(ApiException.class)
    public ResponseEntity<ErrorDTO> handle(ApiException e) {
        return handleResponse(e);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorDTO> handle(HttpMessageNotReadableException e) {
        return handleResponse(exceptionFactory.buildBadRequest(e.getMessage(), e));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorDTO> handle(Exception e) {
        return handleResponse(getApiException(e));
    }

    private ResponseEntity<ErrorDTO> handleResponse(ApiException apiException) {
        log.error(apiException.getId(), apiException);
        return new ResponseEntity<>(errorMapper.mapToDTO(apiException), valueOf(apiException.getHttpStatus().value()));
    }

    private ApiException getApiException(Exception e) {
        return e.getCause() instanceof ApiException ae? ae : exceptionFactory.buildInternalServerError(e);
    }

}
