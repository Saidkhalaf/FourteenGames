package be.kdg.gobackend.analytics.infrastructure.messages;

import be.kdg.gobackend.game.domain.GameState;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

public record GameStartedMessage(
        String event_type,
        String timestamp,
        UUID game_id,
        UUID player_id,
        UUID session_id,
        String game_name,
        int player_count,
        String started_at
) implements EventMessage {
    public static GameStartedMessage of(GameState gameState) {
        return new GameStartedMessage(
                "game_started",
                LocalDateTime.now().atOffset(ZoneOffset.UTC).format(DateTimeFormatter.ISO_INSTANT),
                gameState.getId().id(),
                gameState.getPlayer1().id(),
                gameState.getId().id(),
                "go",
                (gameState.isAiGame()) ? 1 : 2,
                LocalDateTime.now().atOffset(ZoneOffset.UTC).format(DateTimeFormatter.ISO_INSTANT)
        );
    }
}
