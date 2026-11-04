package be.kdg.gobackend.game.domain.repository;


import be.kdg.gobackend.game.infrastructure.ai.dtos.AiAnswerDto;
import be.kdg.gobackend.game.infrastructure.ai.dtos.AiEndGameDto;
import be.kdg.gobackend.game.infrastructure.ai.dtos.AiRequestBodyDto;

import java.util.Optional;

public interface AiCatalog {
    Optional<AiAnswerDto> askForMove(AiRequestBodyDto aiRequestBodyDto);
    void SendSummaryToAI(AiEndGameDto aiEndGameDto);
}
