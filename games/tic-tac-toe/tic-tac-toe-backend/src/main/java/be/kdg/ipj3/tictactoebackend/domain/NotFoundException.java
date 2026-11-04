package be.kdg.ipj3.tictactoebackend.domain;

public class NotFoundException extends RuntimeException {
    public NotFoundException(final String message) {
        super(message);
    }
}
