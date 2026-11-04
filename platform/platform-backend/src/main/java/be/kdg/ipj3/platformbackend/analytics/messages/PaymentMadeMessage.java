package be.kdg.ipj3.platformbackend.analytics.messages;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

public record PaymentMadeMessage(
        String event_type,
        String timestamp,
        UUID player_id,
        UUID transaction_id,
        String payment_method,
        double amount,
        String currency,
        String status
) implements EventMessage {
    public PaymentMadeMessage(UUID player_id, double amount) {
        this(
                "payment_made",
                LocalDateTime.now().atOffset(ZoneOffset.UTC).format(DateTimeFormatter.ISO_INSTANT),
                player_id,
                UUID.randomUUID(),
                "paypal",
                amount,
                "EUR",
                "completed");
    }
}
