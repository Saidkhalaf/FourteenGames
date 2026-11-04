package be.kdg.gobackend.game.infrastructure.ai;

import be.kdg.gobackend.game.domain.repository.AiCatalog;
import be.kdg.gobackend.game.infrastructure.ai.dtos.AiAnswerDto;
import be.kdg.gobackend.game.infrastructure.ai.dtos.AiEndGameDto;
import be.kdg.gobackend.game.infrastructure.ai.dtos.AiRequestBodyDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClient;

import java.util.Optional;

@Slf4j
@Component
public class ExternalAiCatalog implements AiCatalog {

    private final RestClient restClient;

    public ExternalAiCatalog(@Qualifier("aiCatalogApi") RestClient restClient) {
        this.restClient = restClient;
    }

    @Override public Optional<AiAnswerDto> askForMove(AiRequestBodyDto aiRequestBodyDto) {
        log.info("Asking the Ai to make a move: {}", aiRequestBodyDto);
        try {
            final var responseEntity = restClient
                    .post()
                    .uri("/ai-move")
                    .body(aiRequestBodyDto)
                    .retrieve()
                    .toEntity(AiAnswerDto.class);

            log.info("Response status: {}", responseEntity.getStatusCode());

            final var response = responseEntity.getBody();

            if (response == null) {
                log.error("No ai response");
            }

            return Optional.ofNullable(response);
        } catch (final HttpStatusCodeException e) {
            log.error("Error while asking AI for a move: {}", e.getMessage());
            return Optional.empty();
        } catch (final ResourceAccessException e) {
            log.error("AI service is unreachable: {}", e.getMessage());
            return Optional.empty();
        }
    }

    @Override
    public void SendSummaryToAI(AiEndGameDto aiEndGameDto) {
        log.info("Sending summary to AI: {}", aiEndGameDto);
        try {
            final var response = restClient
                    .post()
                    .uri("/end-game")
                    .body(aiEndGameDto)
                    .retrieve()
                    .toBodilessEntity();

            log.info("Response status: {}", response.getStatusCode());
        } catch (final HttpStatusCodeException e) {
            log.warn("Summary was not sent succesfully: {}", e.getMessage());
        } catch (final ResourceAccessException e) {
            log.warn("AI service is unreachable: {}", e.getMessage());
        }
    }

}
