package be.kdg.ipj3.tictactoebackend.infrastructure.gameState.ai.dtos;

public record AiAnswerDto(
        int best_move,
        int row,
        int col
) {
}
