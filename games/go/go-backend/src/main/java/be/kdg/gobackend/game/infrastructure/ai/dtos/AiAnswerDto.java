package be.kdg.gobackend.game.infrastructure.ai.dtos;

public record AiAnswerDto(
        int best_move,
        int row,
        int col
) {
}
