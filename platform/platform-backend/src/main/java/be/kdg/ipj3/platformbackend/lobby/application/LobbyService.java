package be.kdg.ipj3.platformbackend.lobby.application;

import be.kdg.ipj3.platformbackend.game.domain.Game;
import be.kdg.ipj3.platformbackend.game.domain.GameId;
import be.kdg.ipj3.platformbackend.game.domain.repository.GameRepository;
import be.kdg.ipj3.platformbackend.lobby.domain.GameSessionId;
import be.kdg.ipj3.platformbackend.lobby.domain.Lobby;
import be.kdg.ipj3.platformbackend.lobby.domain.LobbyId;
import be.kdg.ipj3.platformbackend.lobby.domain.Player;
import be.kdg.ipj3.platformbackend.lobby.domain.catalog.GameApiCatalog;
import be.kdg.ipj3.platformbackend.lobby.infrastructure.DbLobbyRepository;
import be.kdg.ipj3.platformbackend.shared.domain.UserId;
import be.kdg.ipj3.platformbackend.user.domain.PlatformUser;
import be.kdg.ipj3.platformbackend.user.domain.repository.PlatformUserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
@Slf4j
@Transactional
public class LobbyService {
    private final DbLobbyRepository lobbyRepository;
    private final GameRepository gameRepository;
    private final PlatformUserRepository platformUserRepository;
    private final GameApiCatalog gameApiCatalog;

    public LobbyService(DbLobbyRepository lobbyRepository, GameRepository gameRepository, PlatformUserRepository platformUserRepository, GameApiCatalog gameApiCatalog) {
        this.lobbyRepository = lobbyRepository;
        this.gameRepository = gameRepository;
        this.platformUserRepository = platformUserRepository;
        this.gameApiCatalog = gameApiCatalog;
    }

    public List<Lobby> findAllLobbies() {
        return lobbyRepository.findAllLobbies();
    }


    public Lobby createNewLobby(UserId userId, int maxPlayerCount, GameId gameId) {
        Game game = gameRepository.findById(gameId.id()).orElseThrow(gameId::notFound);
        Lobby newLobby = new Lobby(new LobbyId(UUID.randomUUID()), gameId, null, new ArrayList<>(), LocalDateTime.now(), maxPlayerCount);
        PlatformUser platformUser = platformUserRepository.findUserById(userId).orElseThrow(userId::notFound);
        platformUser.validateGameOwnerShip(game.getId());
        newLobby.addPlayer(platformUser.getUserId());
        return lobbyRepository.createNewLobby(newLobby, game);
    }

    public void leaveLobby(UserId userId, LobbyId lobbyId) {
        Lobby lobby = lobbyRepository.findLobbyById(lobbyId).orElseThrow(lobbyId::notFound);
        Game game = gameRepository.findById(lobby.getGameId().id()).orElseThrow(lobby.getGameId()::notFound);
        PlatformUser platformUser = platformUserRepository.findUserById(userId).orElseThrow(userId::notFound);
        Player player = lobby.getPlayers()
                .stream()
                .filter(p -> p.userId().id().equals(userId.id()))
                .findFirst()
                .orElseThrow(() -> new Player(platformUser.getUserId()).notFound());
        lobby.removePlayer(player);
        if (lobby.getPlayers().isEmpty()) {
            removeLobby(lobby, game);
        } else {
            lobbyRepository.save(lobby, game);
        }
    }

    private void removeLobby(Lobby lobby, Game game) {
        lobbyRepository.remove(lobby, game);
    }

    public Lobby addPlayerToLobby(LobbyId lobbyId, UserId userId) {
        Lobby lobby = lobbyRepository.findLobbyById(lobbyId).orElseThrow(lobbyId::notFound);
        Game game = gameRepository.findById(lobby.getGameId().id()).orElseThrow(lobby.getGameId()::notFound);
        PlatformUser platformUser = platformUserRepository.findUserById(userId).orElseThrow(userId::notFound);
        platformUser.validateGameOwnerShip(game.getId());
        lobby.getPlayers()
                .stream()
                .filter(p -> p.userId().id().equals(userId.id()))
                .findAny()
                .ifPresent(p -> {
                    throw new Player(platformUser.getUserId()).conflict();
                });
        if (lobby.isLobbyFull()) {
            throw lobby.fullConflict();
        }
        platformUserRepository.findUserById(userId).orElseThrow(userId::notFound);
        lobby.addPlayer(platformUser.getUserId());
        lobbyRepository.save(lobby, game);
        return lobby;
    }

    public Lobby findLobby(LobbyId lobbyId) {
        return lobbyRepository.findLobbyById(lobbyId).orElseThrow(lobbyId::notFound);
    }

    public UUID startGame(UserId player1Id, UserId player2Id, LobbyId lobbyId, Jwt token, Map<String, Object> settings) {
        Lobby lobby = lobbyRepository.findLobbyById(lobbyId)
                .orElseThrow(lobbyId::notFound);
        if (!lobby.getLobbyHost().userId().id().equals(player1Id.id())) {
            throw lobby.notLobbyHostForbidden();
        }
        if (!lobby.isLobbyFull()) {
            throw lobby.notFullConflict();
        }
        PlatformUser user1 = platformUserRepository.findUserById(player1Id).orElseThrow(player1Id::notFound);
        PlatformUser user2 = platformUserRepository.findUserById(player2Id).orElseThrow(player2Id::notFound);
        Game game = gameRepository.findById(lobby.getGameId().id())
                .orElseThrow(lobby.getGameId()::notFound);
        if(game.getGameSettings().size() != settings.size()){
            throw lobby.notAllGameSettingsWereSet();
        }
        UUID gameSessionId = gameApiCatalog.startGameSession(
                game,
                token.getTokenValue(),
                user1.getUserId().id(),
                user2.getUserId().id(),
                settings
        );
        lobby.setCurrentGameSession(gameSessionId);
        lobbyRepository.save(lobby, game);
        log.info("Started game {} for game {} in lobby {}", gameSessionId, game.getName(), lobby.getId());
        return gameSessionId;
    }

    public Lobby findLobbyByGameId(GameSessionId gameSessionId) {
        return lobbyRepository.findLobbyByGameSessionId(gameSessionId).orElseThrow(gameSessionId::notFound);
    }

    public void startTrainingGame(GameId gameId, Jwt token) {
        final var game = gameRepository.findById(gameId.id())
                .orElseThrow(gameId::notFound);

        gameApiCatalog.startTrainingGameSession(
                game,
                token.getTokenValue(),
                UserId.fromToken(token)
        );
    }
}
