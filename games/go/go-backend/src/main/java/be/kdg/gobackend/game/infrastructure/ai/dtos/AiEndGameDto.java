package be.kdg.gobackend.game.infrastructure.ai.dtos;

import be.kdg.gobackend.game.domain.GameState;
import be.kdg.gobackend.game.domain.Stone;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;

public record AiEndGameDto(
        String timestamp,
        String gameId,
        int totalTurns,
        String[][] finalBoardState,
        FinalScore finalScore,
        Winner winner,
        String gameOutcome,
        GameInfo gameInfo
) {
    private record FinalScore(
            PlayerScore white,
            PlayerScore Black
    ) {}
    private record PlayerScore(
            int territory,
            int captures,
            int total
    ) {}
    
    private record Winner(
            String player,
            double komi,
            int margin
    
    ) {}
    
    private record GameInfo(
            String boardSize,
            double komi
    ) {}

    public static AiEndGameDto fromDomain(GameState gameState){
        return new AiEndGameDto(
                LocalDateTime.now().atOffset(ZoneOffset.UTC).format(DateTimeFormatter.ISO_INSTANT),
                gameState.getId().id().toString(),
                gameState.getTurnCount(),
                gameState.getBoardForDto(),
                new FinalScore(
                        new PlayerScore(
                                0,
                                0,
                                0
                        ),
                        new PlayerScore(
                                0,
                                0,
                                0
                        )
                ),
                new Winner(
                        gameState.getWinner().toString(),
                        GameState.KOMI,
                        0
                ),
                (gameState.getWinner() == Stone.WHITE) ? "AI_WIN" : "AI_LOSS" ,
                new GameInfo(
                        gameState.getBoard().getSize() + "x" + gameState.getBoard().getSize(),
                        GameState.KOMI
                )
        );
    }
}
