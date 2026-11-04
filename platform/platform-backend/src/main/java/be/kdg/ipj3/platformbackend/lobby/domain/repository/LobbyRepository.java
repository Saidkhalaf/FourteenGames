package be.kdg.ipj3.platformbackend.lobby.domain.repository;

import be.kdg.ipj3.platformbackend.game.domain.Game;
import be.kdg.ipj3.platformbackend.lobby.domain.GameSessionId;
import be.kdg.ipj3.platformbackend.lobby.domain.Lobby;
import be.kdg.ipj3.platformbackend.lobby.domain.LobbyId;

import java.util.List;
import java.util.Optional;

public interface LobbyRepository {
    List<Lobby> findAllLobbies();
    Lobby createNewLobby(Lobby newLobby, Game game);
    Optional<Lobby> findLobbyById(LobbyId lobbyId);
    void save(Lobby lobby, Game game);
    void remove(Lobby lobby, Game game);
    Optional<Lobby> findLobbyByGameSessionId(GameSessionId gameSessionId);
}
