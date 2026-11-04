package be.kdg.ipj3.platformbackend.lobby.infrastructure.jpa.repository;

import be.kdg.ipj3.platformbackend.game.infrastructure.jpa.entity.JpaGameEntity;
import be.kdg.ipj3.platformbackend.lobby.infrastructure.jpa.entity.JpaLobbyEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;
import java.util.Optional;

public interface JpaLobbyRepository extends JpaRepository<JpaLobbyEntity, UUID> {
    Optional<JpaLobbyEntity> findJpaLobbyEntityByCurrentGameSessionId(UUID currentGameSessionId);

    UUID game(JpaGameEntity game);
}
