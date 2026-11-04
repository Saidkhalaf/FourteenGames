package be.kdg.ipj3.platformbackend.lobby.infrastructure.game;

import be.kdg.ipj3.platformbackend.game.domain.Game;
import be.kdg.ipj3.platformbackend.lobby.api.dtos.request.StartGameRequest;
import be.kdg.ipj3.platformbackend.lobby.domain.catalog.GameApiCatalog;
import be.kdg.ipj3.platformbackend.shared.domain.UserId;
import com.fasterxml.jackson.databind.JsonNode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestClient;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Slf4j
@Component
public class ExternalGameApiCatalog implements GameApiCatalog {
    private final RestClient restClient;

    public ExternalGameApiCatalog(RestClient restClient) {
        this.restClient = restClient;
    }

    @Override
    public UUID startGameSession(Game game, String authToken, UUID player1Id, UUID player2Id, Map<String, Object> settings) {
        log.info("Requesting a new game session");
        try{
            JsonNode responseBody = restClient.post()
                    .uri(game.getGameStartEndpoint())
                    .header(HttpHeaders.AUTHORIZATION, "Bearer " + authToken)
                    .body(new StartGameRequest(player1Id, player2Id, settings))
                    .retrieve()
                    .body(JsonNode.class);

            if (responseBody == null || !responseBody.has("id")) {
                throw new IllegalStateException(
                        "Game started but no 'id' field returned from " + game.getGameStartEndpoint()
                );
            }

            return UUID.fromString(responseBody.get("id").asText());

        }catch(HttpStatusCodeException e){
            log.warn("Http error while asking {} to start a new game session.", game.getName());
        }
        return null;
    }

    @Override
    public void startTrainingGameSession(Game game, String tokenValue, UserId userId) {
        log.info("Requesting a new training game session");
        if (game.getAiGameStartEndpoint() == null) return;
        try{
            restClient.post()
                    .uri(game.getAiGameStartEndpoint())
                    .header(HttpHeaders.AUTHORIZATION, "Bearer " + tokenValue)
                    .body(new StartGameRequest(userId.id(), null, new HashMap<>()))
                    .retrieve()
                    .body(JsonNode.class);
        }catch(HttpStatusCodeException e){
            log.warn("Http error {} while asking {} to start a new game session.", e.getStatusCode(), game.getName());
        }
    }
}
