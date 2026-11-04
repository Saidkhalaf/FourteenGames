package be.kdg.ipj3.platformbackend.shared.domain.exception;

public class InsufficientCreditsException extends RuntimeException {
    public InsufficientCreditsException(double credits, double price) {
        super("Not enough credits. Required: " + price + ", available: " + credits);
    }
}
