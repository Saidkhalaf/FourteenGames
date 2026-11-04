package be.kdg.ipj3.tictactoebackend.infrastructure.gameState;

import be.kdg.ipj3.tictactoebackend.domain.*;
import be.kdg.ipj3.tictactoebackend.infrastructure.gameState.jpa.JpaGameStateEntity;
import be.kdg.ipj3.tictactoebackend.infrastructure.gameState.jpa.JpaGameStateRepository;
import be.kdg.ipj3.tictactoebackend.infrastructure.user.jpa.JpaGameCharacterRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Slf4j
@Repository
public class DbGameStateRepository implements GameStateRepository {

    private final JpaGameStateRepository gameStateRepository;
    private final JpaGameCharacterRepository userRepository;

    public DbGameStateRepository(JpaGameStateRepository gameStateRepository, JpaGameCharacterRepository userRepository) {
        this.gameStateRepository = gameStateRepository;
        this.userRepository = userRepository;
    }

    @Override
    public void save(GameState gameState) {
        log.info("Saving game state to database");
        final var player1Id = gameState.getPlayerOne();
        final var player2Id = gameState.getPlayerTwo();
        final var player1Jpa = userRepository.findById(player1Id.id()).orElseThrow(player1Id::notFound);
        final var player2Jpa = userRepository.findById(player2Id.id()).orElseThrow(player2Id::notFound);
        gameStateRepository.save(JpaGameStateEntity.fromDomain(gameState, player1Jpa, player2Jpa));
    }

    @Override public Optional<GameState> get(GameStateId stateId) {
        log.info("Getting game state from database");

        return gameStateRepository.findById(stateId.id()).map(JpaGameStateEntity::toDomain);
    }

    @Override
    public Optional<List<GameState>> getGameForPlayer(CharacterId playerId) {
        log.info("Getting game for player from database");
        final var playerJpa = userRepository.findById(playerId.id()).orElseThrow(playerId::notFound);
        return gameStateRepository.findByPlayerAndStatus(playerJpa, GameStatus.IN_PROGRESS)
                .map(list -> list.stream().map(JpaGameStateEntity::toDomain).toList());
    }
}
