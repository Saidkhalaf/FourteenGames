package be.kdg.ipj3.platformbackend.lobby.domain.catalog;

import be.kdg.ipj3.platformbackend.game.domain.Game;
import be.kdg.ipj3.platformbackend.shared.domain.UserId;

import java.util.Map;
import java.util.UUID;

public interface GameApiCatalog {
    UUID startGameSession(Game game, String authToken, UUID player1Id, UUID player2Id, Map<String, Object> settings);
    void startTrainingGameSession(Game game, String tokenValue, UserId userId);
}
