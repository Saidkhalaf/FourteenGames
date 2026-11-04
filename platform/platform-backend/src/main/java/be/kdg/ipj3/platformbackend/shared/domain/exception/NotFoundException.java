package be.kdg.ipj3.platformbackend.shared.domain.exception;

public class NotFoundException extends RuntimeException {
    public NotFoundException(final String message) {
        super(message);
    }
}
