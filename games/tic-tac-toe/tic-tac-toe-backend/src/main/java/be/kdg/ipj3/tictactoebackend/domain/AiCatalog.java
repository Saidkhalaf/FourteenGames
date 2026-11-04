package be.kdg.ipj3.tictactoebackend.domain;

import be.kdg.ipj3.tictactoebackend.infrastructure.gameState.ai.dtos.AiAnswerDto;
import be.kdg.ipj3.tictactoebackend.infrastructure.gameState.ai.dtos.GameStateDtoAI;

import java.util.Optional;

public interface AiCatalog {
    Optional<AiAnswerDto> askForMove(GameStateDtoAI gameStateDto);
}
