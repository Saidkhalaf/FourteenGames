package be.kdg.ipj3.platformbackend.shared.domain.exception;

public class BadRequestException extends RuntimeException {
    public BadRequestException(final String message) {
        super(message);
    }
}
