package be.kdg.gobackend.game.domain.repository;

import be.kdg.gobackend.game.domain.GameState;
import be.kdg.gobackend.game.domain.GameStateId;
import be.kdg.gobackend.game.domain.PlayerId;

import java.util.Optional;

public interface GameStateRepository {
    void save(GameState gameState);
    Optional<GameState> get(GameStateId id);
    Optional<GameState> getPlayingGameForPlayer(PlayerId playerId);
    Optional<GameState> getOngoingAiGameForPlayer(PlayerId playerId);
    void removeGame(GameState notFinishedAiGame);
}
