package ie.ramos.modak.exception;

import org.springframework.stereotype.Component;

import static org.springframework.http.HttpStatus.*;

@Component
public class ExceptionFactory {

    public ApiException buildNoMappingFound(Exception e) {
        return new ApiException("no_mapping_found", "No mapping found", e.getMessage(), NOT_FOUND, e);
    }

    public ApiException buildTooManyRequest(String id, String title, String description) {
        return new ApiException(id, title, description, TOO_MANY_REQUESTS);
    }

    public ApiException buildBadRequest(String description, Exception cause) {
        return new ApiException("bad_request", "Bad Request", description, BAD_REQUEST, cause);
    }

    public ApiException buildBadRequest(String id, String title, String description) {
        return new ApiException(id, title, description, BAD_REQUEST);
    }

    public ApiException buildNotFound(String id, String title, String description) {
        return new ApiException(id, title, description, BAD_REQUEST);
    }


    public ApiException buildInternalServerError(Exception e) {
        return new ApiException("internal_server_error", "An error occurred", e.getMessage(), INTERNAL_SERVER_ERROR, e);
    }

}
