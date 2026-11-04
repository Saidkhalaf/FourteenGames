package be.kdg.ipj3.platformbackend.lobby;

import be.kdg.ipj3.platformbackend.game.domain.Game;
import be.kdg.ipj3.platformbackend.game.domain.GameId;
import be.kdg.ipj3.platformbackend.game.domain.Genre;
import be.kdg.ipj3.platformbackend.game.domain.repository.GameRepository;
import be.kdg.ipj3.platformbackend.lobby.application.LobbyService;
import be.kdg.ipj3.platformbackend.lobby.domain.Lobby;
import be.kdg.ipj3.platformbackend.lobby.domain.LobbyId;
import be.kdg.ipj3.platformbackend.lobby.infrastructure.DbLobbyRepository;
import be.kdg.ipj3.platformbackend.shared.domain.UserId;
import be.kdg.ipj3.platformbackend.user.domain.OwnedCopy;
import be.kdg.ipj3.platformbackend.user.domain.OwnedCopyId;
import be.kdg.ipj3.platformbackend.user.domain.PlatformUser;
import be.kdg.ipj3.platformbackend.user.domain.repository.PlatformUserRepository;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.*;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class AddPlayerToLobbyTest {
    @Mock
    DbLobbyRepository lobbyRepository;

    @Mock
    PlatformUserRepository platformUserRepository;

    @Mock
    GameRepository gameRepository;

    @InjectMocks
    LobbyService lobbyService;

    @Nested
    class SuccessFlows {
        @Test
        void addPlayerToLobby_shouldAddUser_whenLobbyIsOpen() {
            // Arrange
            LobbyId lobbyId = new LobbyId(UUID.randomUUID());
            UserId userId = new UserId(UUID.randomUUID());
            Game game = new Game(new GameId(UUID.randomUUID()),"myGame",2,"MyDescription",5.00,"","","localhost:8080",new Genre("strategy",""),new ArrayList<>(),"localhost:8080/start/ai","localhost:8080/start", new HashMap<>());
            Lobby lobby = new Lobby(lobbyId, game.getId(),null, new ArrayList<>(), LocalDateTime.now(), game.getMaxPlayerCount());
            List<OwnedCopy> ownedCopyList = List.of(
                new OwnedCopy(new OwnedCopyId(UUID.randomUUID()),game.getId(), false )
            );
            PlatformUser user = new PlatformUser(userId, "u", "", new ArrayList<>(), "", "", 0.0,ownedCopyList);
            when(lobbyRepository.findLobbyById(lobbyId)).thenReturn(Optional.of(lobby));
            when(gameRepository.findById(game.getId().id())).thenReturn(Optional.of(game));
            when(platformUserRepository.findUserById(userId)).thenReturn(Optional.of(user));
            // Act
            Lobby result = lobbyService.addPlayerToLobby(lobbyId, userId);
            // Assert
            assertThat(result.getPlayers().size()).isEqualTo(1);
            verify(lobbyRepository).save(lobby, game);
        }
    }

    @Nested
    class ErrorFlows {
        @Test
        void addPlayerToLobby_shouldThrowException_whenLobbyIsFull() {
            // Arrange
            LobbyId lobbyId = new LobbyId(UUID.randomUUID());
            UserId userId = new UserId(UUID.randomUUID());
            Game game = new Game(new GameId(UUID.randomUUID()),"myGame",1,"MyDescription",5.00,"","","localhost:8080",new Genre("strategy",""),new ArrayList<>(),"localhost:8080/start/ai","localhost:8080/start", new HashMap<>());
            Lobby lobby = new Lobby(lobbyId, game.getId(),null, new ArrayList<>(), LocalDateTime.now(), game.getMaxPlayerCount());
            PlatformUser existingUser = new PlatformUser(new UserId(UUID.randomUUID()), "existing", "", null, "", "", 0.0, null);
            lobby.addPlayer(existingUser.getUserId());
            PlatformUser newUser = new PlatformUser(userId, "new", "", null, "", "", 0.0, null);
            when(lobbyRepository.findLobbyById(lobbyId)).thenReturn(Optional.of(lobby));
            when(gameRepository.findById(game.getId().id())).thenReturn(Optional.of(game));
            when(platformUserRepository.findUserById(userId)).thenReturn(Optional.of(newUser));
            // Act
            // Assert
            assertThatThrownBy(() -> lobbyService.addPlayerToLobby(lobbyId, userId))
                    .isInstanceOf(RuntimeException.class);
        }

        @Test
        void addPlayerToLobby_shouldThrowException_whenUserDoesNotOwnACopyOfTheGame() {
            // Arrange
            LobbyId lobbyId = new LobbyId(UUID.randomUUID());
            UserId userId = new UserId(UUID.randomUUID());
            Game game = new Game(new GameId(UUID.randomUUID()),"myGame",2,"MyDescription",5.00,"","","localhost:8080",new Genre("strategy",""),new ArrayList<>(),"localhost:8080/start/ai","localhost:8080/start", new HashMap<>());
            Lobby lobby = new Lobby(lobbyId, game.getId(),null, new ArrayList<>(), LocalDateTime.now(), game.getMaxPlayerCount());
            PlatformUser user = new PlatformUser(userId, "u", "", new ArrayList<>(), "", "", 0.0, new ArrayList<>());
            when(lobbyRepository.findLobbyById(lobbyId)).thenReturn(Optional.of(lobby));
            when(gameRepository.findById(game.getId().id())).thenReturn(Optional.of(game));
            when(platformUserRepository.findUserById(userId)).thenReturn(Optional.of(user));
            // Act
            // Assert
            assertThatThrownBy(() -> lobbyService.addPlayerToLobby(lobbyId, userId))
                    .isInstanceOf(RuntimeException.class);
        }

        @Test
        void addPlayerToLobby_shouldThrowException_whenPlayerAlreadyInLobby() {
            // Arrange
            LobbyId lobbyId = new LobbyId(UUID.randomUUID());
            UserId userId = new UserId(UUID.randomUUID());
            Game game = new Game(new GameId(UUID.randomUUID()),"myGame",2,"MyDescription",5.00,"","","localhost:8080",new Genre("strategy",""),new ArrayList<>(),"localhost:8080/start/ai","localhost:8080/start", new HashMap<>());
            Lobby lobby = new Lobby(lobbyId, game.getId(),null, new ArrayList<>(), LocalDateTime.now(), game.getMaxPlayerCount());
            PlatformUser user = new PlatformUser(userId, "u", "", new ArrayList<>(), "", "", 0.0, new ArrayList<>());
            lobby.addPlayer(user.getUserId());
            when(lobbyRepository.findLobbyById(lobbyId)).thenReturn(Optional.of(lobby));
            when(gameRepository.findById(game.getId().id())).thenReturn(Optional.of(game));
            when(platformUserRepository.findUserById(userId)).thenReturn(Optional.of(user));

            // Act
            // Assert
            assertThatThrownBy(() -> lobbyService.addPlayerToLobby(lobbyId, userId))
                    .isInstanceOf(RuntimeException.class);
        }
    }
}
