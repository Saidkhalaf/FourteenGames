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
import be.kdg.ipj3.platformbackend.user.domain.PlatformUser;
import be.kdg.ipj3.platformbackend.user.domain.repository.PlatformUserRepository;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class LeaveLobbyTest {
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
        void leaveLobby_shouldRemovePlayerAndSave_whenOtherPlayersRemain() {
            // Arrange
            LobbyId lobbyId = new LobbyId(UUID.randomUUID());
            UserId userId1 = new UserId(UUID.randomUUID());
            UserId userId2 = new UserId(UUID.randomUUID());
            GameId gameId = new GameId(UUID.randomUUID());
            Game game = new Game(gameId, "Test Game", 2,"Desc", 10.0, "img", "icon", "url", null, new ArrayList<>(), "url/start/ai", "/url/start", new HashMap<>());
            Lobby lobby = new Lobby(lobbyId, game.getId(),null, new ArrayList<>(), LocalDateTime.now(), 5);
            PlatformUser user1 = new PlatformUser(userId1, "u1", "", new ArrayList<>(), "", "", 0.0, new ArrayList<>());
            PlatformUser user2 = new PlatformUser(userId2, "u2", "", new ArrayList<>(), "", "", 0.0, new ArrayList<>());
            lobby.addPlayer(user1.getUserId());
            lobby.addPlayer(user2.getUserId());
            when(lobbyRepository.findLobbyById(lobbyId)).thenReturn(Optional.of(lobby));
            when(gameRepository.findById(game.getId().id())).thenReturn(Optional.of(game));
            when(platformUserRepository.findUserById(userId1)).thenReturn(Optional.of(user1));
            // Act
            lobbyService.leaveLobby(userId1, lobbyId);
            // Assert
            assertThat(lobby.getPlayers().size()).isEqualTo(1);
            verify(lobbyRepository).save(lobby, game);
            verify(lobbyRepository, never()).remove(any(), any());
        }

        @Test
        void leaveLobby_shouldRemoveLobby_whenLastPlayerLeaves() {
            // Arrange
            LobbyId lobbyId = new LobbyId(UUID.randomUUID());
            UserId userId = new UserId(UUID.randomUUID());
            GameId gameId = new GameId(UUID.randomUUID());
            Game game = new Game(gameId, "Test Game", 2,"Desc", 10.0, "img", "icon", "url", null, new ArrayList<>(), "url/start/ai", "/url/start", new HashMap<>());
            Lobby lobby = new Lobby(lobbyId, game.getId(),null, new ArrayList<>(), LocalDateTime.now(), 5);
            PlatformUser user = new PlatformUser(userId, "u1", "", new ArrayList<>(), "", "", 0.0, new ArrayList<>());
            lobby.addPlayer(user.getUserId());
            when(lobbyRepository.findLobbyById(lobbyId)).thenReturn(Optional.of(lobby));
            when(gameRepository.findById(game.getId().id())).thenReturn(Optional.of(game));
            when(platformUserRepository.findUserById(userId)).thenReturn(Optional.of(user));
            // Act
            lobbyService.leaveLobby(userId, lobbyId);
            // Assert
            assertThat(lobby.getPlayers().isEmpty()).isTrue();
            verify(lobbyRepository).remove(lobby, game);
            verify(lobbyRepository, never()).save(lobby, game);
        }
    }

    @Nested
    class ErrorFlows {
        @Test
        void leaveLobby_shouldThrowException_whenUserNotInLobby() {
            // Arrange
            LobbyId lobbyId = new LobbyId(UUID.randomUUID());
            UserId userId = new UserId(UUID.randomUUID());
            UserId otherId = new UserId(UUID.randomUUID());
            Game game = new Game(new GameId(UUID.randomUUID()),"myGame",2,"MyDescription",5.00,"","","",new Genre("strategy",""),new ArrayList<>(), "","",new HashMap<>());
            Lobby lobby = new Lobby(lobbyId, game.getId(),null, new ArrayList<>(), LocalDateTime.now(), 5);
            PlatformUser otherUser = new PlatformUser(otherId, "u2", "", new ArrayList<>(), "", "", 0.0, new ArrayList<>());
            lobby.addPlayer(otherUser.getUserId());
            when(lobbyRepository.findLobbyById(lobbyId)).thenReturn(Optional.of(lobby));
            when(gameRepository.findById(game.getId().id())).thenReturn(Optional.of(game));
            PlatformUser requestingUser = new PlatformUser(userId, "u1", "", null, "", "", 0.0, null);
            when(platformUserRepository.findUserById(userId)).thenReturn(Optional.of(requestingUser));
            // Act
            // Assert
            assertThatThrownBy(() -> lobbyService.leaveLobby(userId, lobbyId))
                    .isInstanceOf(RuntimeException.class);
        }
    }
}
