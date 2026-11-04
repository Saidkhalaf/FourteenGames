package be.kdg.ipj3.tictactoebackend.domain;

import java.util.List;
import java.util.Optional;

public interface GameStateRepository {
    void save(GameState gameState);

    Optional<GameState> get(GameStateId stateId);

    Optional<List<GameState>> getGameForPlayer(CharacterId playerId);
}
