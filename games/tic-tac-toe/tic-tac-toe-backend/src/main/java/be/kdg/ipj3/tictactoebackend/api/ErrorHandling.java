package be.kdg.ipj3.tictactoebackend.api;

import be.kdg.ipj3.tictactoebackend.domain.NotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@Slf4j
@ControllerAdvice
public class ErrorHandling {
    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ErrorResponse> notFoundHandler(final NotFoundException ex) {
        final var errorResponse = new ErrorResponse(ex.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
    }

    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<ErrorResponse> illegalStateHandler(final IllegalStateException ex) {
        final var errorResponse = new ErrorResponse(ex.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorResponse> illegalStateHandler(final IllegalArgumentException ex) {
        final var errorResponse = new ErrorResponse(ex.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }

    public record ErrorResponse(String message) {
        public ErrorResponse {
            log.error(message);
        }
    }
}
