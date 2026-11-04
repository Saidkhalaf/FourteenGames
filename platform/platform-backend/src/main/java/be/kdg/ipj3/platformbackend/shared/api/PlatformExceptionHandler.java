package be.kdg.ipj3.platformbackend.shared.api;

import be.kdg.ipj3.platformbackend.shared.domain.exception.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class PlatformExceptionHandler {

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<String> handleGameNotFound(NotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
    }

    @ExceptionHandler(ConflictException.class)
    public static ResponseEntity<String> handleFriendConflict(ConflictException ex) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(ex.getMessage());
    }
    @ExceptionHandler(ForbiddenException.class)
    public static ResponseEntity<String> handleForbiddenConflict(ForbiddenException ex){
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(ex.getMessage());
    }
    @ExceptionHandler(BadRequestException.class)
    public static ResponseEntity<String> handleBadRequest(BadRequestException ex){
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
    }
}