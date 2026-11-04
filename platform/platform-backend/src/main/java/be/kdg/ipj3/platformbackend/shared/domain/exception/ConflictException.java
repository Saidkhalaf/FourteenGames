package be.kdg.ipj3.platformbackend.shared.domain.exception;

public class ConflictException extends RuntimeException {
    public ConflictException(final String message) {
        super(message);
    }
}
