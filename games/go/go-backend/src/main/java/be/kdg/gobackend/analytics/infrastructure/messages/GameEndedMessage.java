package be.kdg.gobackend.analytics.infrastructure.messages;

import be.kdg.gobackend.game.domain.GameState;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.UUID;

public record GameEndedMessage(
        String event_type,
        String timestamp,
        UUID game_id,
        UUID player_id,
        UUID session_id,
        int session_duration,
        boolean completed,
        String ended_at
) implements EventMessage {
    public static GameEndedMessage of(GameState gameState) {
        return new GameEndedMessage(
                "game_ended",
                LocalDateTime.now().atOffset(ZoneOffset.UTC).format(DateTimeFormatter.ISO_INSTANT),
                gameState.getId().id(),
                gameState.getPlayer1().id(),
                gameState.getId().id(),
                (int) ChronoUnit.SECONDS.between(gameState.getCreatedAt(), java.time.LocalDateTime.now()),
                true,
                LocalDateTime.now().atOffset(ZoneOffset.UTC).format(DateTimeFormatter.ISO_INSTANT)
        );
    }
}
