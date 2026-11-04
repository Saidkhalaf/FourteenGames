package be.kdg.ipj3.platformbackend.lobby.infrastructure;

import be.kdg.ipj3.platformbackend.game.domain.Game;
import be.kdg.ipj3.platformbackend.lobby.domain.GameSessionId;
import be.kdg.ipj3.platformbackend.lobby.domain.Lobby;
import be.kdg.ipj3.platformbackend.lobby.domain.LobbyId;
import be.kdg.ipj3.platformbackend.lobby.domain.repository.LobbyRepository;
import be.kdg.ipj3.platformbackend.lobby.infrastructure.jpa.entity.JpaLobbyEntity;
import be.kdg.ipj3.platformbackend.lobby.infrastructure.jpa.repository.JpaLobbyRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@Slf4j
public class DbLobbyRepository implements LobbyRepository {

    private final JpaLobbyRepository jpaLobbyRepository;

    public DbLobbyRepository(JpaLobbyRepository jpaLobbyRepository) {
        this.jpaLobbyRepository = jpaLobbyRepository;
    }

    @Override
    public List<Lobby> findAllLobbies() {
        return jpaLobbyRepository.findAll().stream().map(JpaLobbyEntity::toDomain).toList();
    }

    @Override
    public Lobby createNewLobby(Lobby newLobby, Game game) {
        JpaLobbyEntity createdLobby=jpaLobbyRepository.save(JpaLobbyEntity.fromDomain(newLobby, game));
        return createdLobby.toDomain();
    }

    @Override
    public Optional<Lobby> findLobbyById(LobbyId lobbyId) {
        return jpaLobbyRepository.findById(lobbyId.id()).map(JpaLobbyEntity::toDomain);
    }

    @Override
    public void save(Lobby lobby, Game game) {
        jpaLobbyRepository.save(JpaLobbyEntity.fromDomain(lobby, game));
    }

    @Override
    public void remove(Lobby lobby, Game game) {
        jpaLobbyRepository.delete(JpaLobbyEntity.fromDomain(lobby, game));
    }

    @Override
    public Optional<Lobby> findLobbyByGameSessionId(GameSessionId gameSessionId) {
        return jpaLobbyRepository.findJpaLobbyEntityByCurrentGameSessionId(gameSessionId.id()).map(JpaLobbyEntity::toDomain);
    }

}
