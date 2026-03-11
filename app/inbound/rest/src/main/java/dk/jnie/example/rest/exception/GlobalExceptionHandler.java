package dk.jnie.example.rest.exception;

import io.micronaut.http.HttpRequest;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.HttpStatus;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Error;
import io.micronaut.http.annotation.Produces;
import io.micronaut.serde.annotation.Serdeable;
import jakarta.validation.ConstraintViolationException;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Instant;

/**
 * Global exception handler for the REST API.
 * Handles exceptions thrown from controllers and returns appropriate error responses.
 */
@Controller
public class GlobalExceptionHandler {

    private static final Logger LOG = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    /**
     * Handles generic exceptions and returns a 500 Internal Server Error response.
     */
    @Error(global = true, exception = Exception.class)
    @Produces
    public HttpResponse<ErrorResponse> handleGenericException(HttpRequest<?> request, Exception ex) {
        LOG.error("Unhandled exception: {}", ex.getMessage(), ex);
        ErrorResponse error = new ErrorResponse(
                HttpStatus.INTERNAL_SERVER_ERROR.getCode(),
                "Internal Server Error",
                "An unexpected error occurred"
        );
        return HttpResponse.serverError(error);
    }

    /**
     * Handles IllegalArgumentException and returns a 400 Bad Request response.
     */
    @Error(exception = IllegalArgumentException.class)
    @Produces
    public HttpResponse<ErrorResponse> handleIllegalArgumentException(HttpRequest<?> request, IllegalArgumentException ex) {
        ErrorResponse error = new ErrorResponse(
                HttpStatus.BAD_REQUEST.getCode(),
                "Bad Request",
                ex.getMessage()
        );
        return HttpResponse.badRequest(error);
    }

    /**
     * Handles validation errors from @Valid annotations and returns a 400 Bad Request response.
     */
    @Error(exception = ConstraintViolationException.class)
    @Produces
    public HttpResponse<ErrorResponse> handleValidationException(HttpRequest<?> request, ConstraintViolationException ex) {
        ErrorResponse error = new ErrorResponse(
                HttpStatus.BAD_REQUEST.getCode(),
                "Validation Failed",
                ex.getMessage()
        );
        return HttpResponse.badRequest(error);
    }

    /**
     * Data class for error responses.
     */
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Serdeable
    public static class ErrorResponse {
        private int status;
        private String error;
        private String message;
        private Instant timestamp;

        public ErrorResponse(int status, String error, String message) {
            this.status = status;
            this.error = error;
            this.message = message;
            this.timestamp = Instant.now();
        }
    }
}