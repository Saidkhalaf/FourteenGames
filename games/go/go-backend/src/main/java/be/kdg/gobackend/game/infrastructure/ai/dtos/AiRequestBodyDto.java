package be.kdg.gobackend.game.infrastructure.ai.dtos;

import be.kdg.gobackend.game.domain.GameState;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;

public record AiRequestBodyDto(
        String timestamp,
        String gameId,
        String requestTimestamp,
        int turnNumber,
        String[][] boardState,
        String currentPlayer,
        int iterations
) {
    public static AiRequestBodyDto from(GameState gameState) {
        return new AiRequestBodyDto(
                LocalDateTime.now().atOffset(ZoneOffset.UTC).format(DateTimeFormatter.ISO_INSTANT),
                gameState.getId().id().toString(),
                LocalDateTime.now().atOffset(ZoneOffset.UTC).format(DateTimeFormatter.ISO_INSTANT),
                gameState.getTurnCount(),
                gameState.getBoardForDto(),
                "BLACK",
                10
        );
    }
}

