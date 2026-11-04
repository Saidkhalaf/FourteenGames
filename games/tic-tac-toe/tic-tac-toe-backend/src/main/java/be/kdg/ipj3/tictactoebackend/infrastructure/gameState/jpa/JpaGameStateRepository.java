package be.kdg.ipj3.tictactoebackend.infrastructure.gameState.jpa;

import be.kdg.ipj3.tictactoebackend.domain.GameStatus;
import be.kdg.ipj3.tictactoebackend.infrastructure.user.jpa.JpaGameCharacterEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface JpaGameStateRepository extends JpaRepository<JpaGameStateEntity, UUID> {

    @Query("SELECT g FROM JpaGameStateEntity g WHERE (g.player1 = :player OR g.player2 = :player) AND g.status = :status")
    Optional<List<JpaGameStateEntity>> findByPlayerAndStatus(@Param("player") JpaGameCharacterEntity player, @Param("status") GameStatus status);
}
