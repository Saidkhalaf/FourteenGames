package be.kdg.gobackend.analytics.infrastructure.messages;

import be.kdg.gobackend.game.domain.GameState;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.UUID;

public record GameAbandonedMessage(
        String event_type,
        String timestamp,
        UUID game_id,
        String game_name,
        UUID player_id,
        UUID session_id,
        int session_duration_seconds,
        boolean completed,
        String reason,
        String abandoned_at
) implements EventMessage {
    public static GameAbandonedMessage of(GameState gameState, String reason) {
        return new GameAbandonedMessage(
                "game_abandoned",
                LocalDateTime.now().atOffset(ZoneOffset.UTC).format(DateTimeFormatter.ISO_INSTANT),
                gameState.getId().id(),
                "go",
                gameState.getPlayer1().id(),
                gameState.getId().id(),
                (int) gameState.getCreatedAt().atOffset(ZoneOffset.UTC).until(LocalDateTime.now(), ChronoUnit.SECONDS),
                false,
                reason,
                LocalDateTime.now().atOffset(ZoneOffset.UTC).format(DateTimeFormatter.ISO_INSTANT)
        );
    }
}
