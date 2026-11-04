package be.kdg.ipj3.platformbackend.analytics.messages;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

public record PurchaseMadeMessage(
        String event_type,
        String timestamp,
        UUID player_id,
        UUID game_id,
        String game_name,
        String product_type,
        double amount,
        String currency,
        UUID transaction_id
) implements EventMessage {
    public PurchaseMadeMessage(UUID playerId, UUID gameId, String gameName, double amount) {
        this(
                "purchase_made",
                LocalDateTime.now().atOffset(ZoneOffset.UTC).format(DateTimeFormatter.ISO_INSTANT),
                playerId,
                gameId,
                gameName,
                "game",
                amount,
                "EUR",
                UUID.randomUUID());
    }
}
