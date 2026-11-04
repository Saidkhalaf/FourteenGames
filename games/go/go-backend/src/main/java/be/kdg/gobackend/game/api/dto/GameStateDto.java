package be.kdg.gobackend.game.api.dto;

import be.kdg.gobackend.game.domain.GameState;

import java.util.UUID;

public record GameStateDto(
        UUID id,
        String[][] board,
        int size,
        int turnCount,
        String winner,
        double score,
        UUID player1Id,
        UUID player2Id,
        boolean isPlayer1AtTurn,
        boolean isLastTurnPassed,
        boolean isAiGame
) {
    public static GameStateDto from(GameState gameState) {
        return new GameStateDto(
                gameState.getId().id(),
                gameState.getBoardForDto(),
                gameState.getSize(),
                gameState.getTurnCount(),
                gameState.getWinner().toString(),
                gameState.getScore().getBlackMargin(),
                gameState.getPlayer1().id(),
                gameState.getPlayer2().id(),
                gameState.isPlayer1AtTurn(),
                gameState.isLastTurnPassed(),
                gameState.isAiGame()
        );
    }
}
