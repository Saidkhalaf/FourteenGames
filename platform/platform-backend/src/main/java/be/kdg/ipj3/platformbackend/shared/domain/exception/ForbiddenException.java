package be.kdg.ipj3.platformbackend.shared.domain.exception;

public class ForbiddenException extends RuntimeException {
    public ForbiddenException(final String message) {
        super(message);
    }
}
