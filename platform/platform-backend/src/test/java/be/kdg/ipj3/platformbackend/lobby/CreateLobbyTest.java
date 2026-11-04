package be.kdg.ipj3.platformbackend.lobby;

import be.kdg.ipj3.platformbackend.game.domain.Game;
import be.kdg.ipj3.platformbackend.game.domain.GameId;
import be.kdg.ipj3.platformbackend.game.domain.repository.GameRepository;
import be.kdg.ipj3.platformbackend.lobby.application.LobbyService;
import be.kdg.ipj3.platformbackend.lobby.domain.Lobby;
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

import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CreateLobbyTest {
    @Mock
    DbLobbyRepository lobbyRepository;
    @Mock
    GameRepository gameRepository;
    @Mock
    PlatformUserRepository platformUserRepository;

    @InjectMocks
    LobbyService lobbyService;

    @Nested
    class SuccessFlows {
        @Test
        void createNewLobby_shouldReturnLobby_whenDataIsValid() {
            // Arrange
            UserId userId = new UserId(UUID.randomUUID());
            GameId gameId = new GameId(UUID.randomUUID());
            int maxPlayerCount = 4;
            Game mockGame = new Game(gameId, "Test Game", maxPlayerCount,"Desc", 10.0, "img", "icon", "url", null, new ArrayList<>(), "url/start/ai", "/url/start", new HashMap<>());
            List<OwnedCopy> ownedCopyList = List.of(new OwnedCopy(new OwnedCopyId(UUID.randomUUID()), mockGame.getId(), false));
            PlatformUser mockUser = new PlatformUser(userId, "user1", "email", new ArrayList<>(), "", "", 100.0, ownedCopyList);
            Lobby expectedLobby = new Lobby(null, mockGame.getId(),null, new ArrayList<>(), null, mockGame.getMaxPlayerCount());
            when(gameRepository.findById(gameId.id())).thenReturn(Optional.of(mockGame));
            when(platformUserRepository.findUserById(userId)).thenReturn(Optional.of(mockUser));
            when(lobbyRepository.createNewLobby(any(Lobby.class), any(Game.class))).thenReturn(expectedLobby);
            // Act
            Lobby result = lobbyService.createNewLobby(userId, maxPlayerCount, gameId);
            // Assert
            assertThat(result).isNotNull();
            assertThat(result.getMaxPlayerCount()).isEqualTo(maxPlayerCount);
            verify(lobbyRepository).createNewLobby(any(Lobby.class), any(Game.class));
        }
    }

    @Nested
    class ErrorFlows {
        @Test
        void createNewLobby_shouldThrowException_whenGameNotFound() {
            // Arrange
            UserId userId = new UserId(UUID.randomUUID());
            GameId gameId = new GameId(UUID.randomUUID());
            when(gameRepository.findById(gameId.id())).thenReturn(Optional.empty());
            // Act
            // Assert
            assertThatThrownBy(() -> lobbyService.createNewLobby(userId, 4, gameId))
                    .isInstanceOf(RuntimeException.class);
        }

        @Test
        void createNewLobby_shouldThrowException_whenGameCopyIsNotOwned() {
            // Arrange
            UserId userId = new UserId(UUID.randomUUID());
            GameId gameId = new GameId(UUID.randomUUID());

            Game game = new Game(gameId, "Test", 4, "Desc",
                    10.0, "img", "icon", "url", null,
                    new ArrayList<>(), "url/start/ai", "/url/start", new HashMap<>());

            PlatformUser user = new PlatformUser(
                    userId, "user", "email",
                    new ArrayList<>(), "", "", 100.0, new ArrayList<>()
            );

            when(gameRepository.findById(gameId.id()))
                    .thenReturn(Optional.of(game));
            when(platformUserRepository.findUserById(userId))
                    .thenReturn(Optional.of(user));
            // Act
            // Assert
            assertThatThrownBy(() ->
                    lobbyService.createNewLobby(userId, 4, gameId)
            ).isInstanceOf(RuntimeException.class);
            verify(lobbyRepository, never()).createNewLobby(any(), any());
        }

        @Test
        void createNewLobby_shouldThrowException_whenUserNotFound() {
            // Arrange
            UserId userId = new UserId(UUID.randomUUID());
            GameId gameId = new GameId(UUID.randomUUID());
            Game mockGame = new Game(gameId, "Test Game", 2,"Desc", 10.0, "img", "icon", "url", null, new ArrayList<>(), "url/start/ai", "/url/start", new HashMap<>());
            when(gameRepository.findById(gameId.id())).thenReturn(Optional.of(mockGame));
            when(platformUserRepository.findUserById(userId)).thenReturn(Optional.empty());
            // Act
            // Assert
            assertThatThrownBy(() -> lobbyService.createNewLobby(userId, 4, gameId))
                    .isInstanceOf(RuntimeException.class);
        }
    }
}
