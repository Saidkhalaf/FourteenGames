package be.kdg.ipj3.tictactoebackend.infrastructure.gameState.ai.dtos;

import be.kdg.ipj3.tictactoebackend.domain.GameState;

public record GameStateDtoAI(
        String[][] boardState,
        String currentPlayer
) {
    public static GameStateDtoAI from(GameState gameState) {
        return new GameStateDtoAI(gameState.getGameStateForAI(),  "-1");
    }
}
