package be.kdg.gobackend.analytics.infrastructure.messages;

import be.kdg.gobackend.game.domain.GameState;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

public record SessionStartedMessage(
        String event_type,
        String timestamp,
        UUID player_id,
        UUID session_id,
        UUID game_id,
        String game_name,
        int session_duration_seconds
) implements EventMessage {
    public static SessionStartedMessage of(GameState gameState) {
        return new SessionStartedMessage(
                "session_started",
                LocalDateTime.now().atOffset(ZoneOffset.UTC).format(DateTimeFormatter.ISO_INSTANT),
                gameState.getPlayer1().id(),
                gameState.getId().id(),
                gameState.getId().id(),
                "go",
                0
        );
    }
}
