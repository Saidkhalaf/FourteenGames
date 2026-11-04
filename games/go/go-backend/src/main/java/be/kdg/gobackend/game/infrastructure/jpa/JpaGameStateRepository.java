package be.kdg.gobackend.game.infrastructure.jpa;

import be.kdg.gobackend.game.domain.Stone;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface JpaGameStateRepository extends JpaRepository<JpaGameStateEntity, UUID> {
    Optional<JpaGameStateEntity> findFirstByPlayer1OrPlayer2AndWinner(UUID player1, UUID player2, Stone winner);
    Optional<JpaGameStateEntity> findByPlayer1AndIsAiGameAndWinner(UUID player1, boolean isAiGame, Stone winner);
}
