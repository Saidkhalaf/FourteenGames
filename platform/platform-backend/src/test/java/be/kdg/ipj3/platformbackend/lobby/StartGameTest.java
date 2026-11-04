package be.kdg.ipj3.platformbackend.lobby;

import be.kdg.ipj3.platformbackend.game.domain.Game;
import be.kdg.ipj3.platformbackend.game.domain.GameId;
import be.kdg.ipj3.platformbackend.game.domain.repository.GameRepository;
import be.kdg.ipj3.platformbackend.lobby.application.LobbyService;
import be.kdg.ipj3.platformbackend.lobby.domain.Lobby;
import be.kdg.ipj3.platformbackend.lobby.domain.LobbyId;
import be.kdg.ipj3.platformbackend.lobby.domain.Player;
import be.kdg.ipj3.platformbackend.lobby.domain.catalog.GameApiCatalog;
import be.kdg.ipj3.platformbackend.lobby.infrastructure.DbLobbyRepository;
import be.kdg.ipj3.platformbackend.shared.domain.UserId;
import be.kdg.ipj3.platformbackend.shared.domain.exception.BadRequestException;
import be.kdg.ipj3.platformbackend.shared.domain.exception.ConflictException;
import be.kdg.ipj3.platformbackend.shared.domain.exception.ForbiddenException;
import be.kdg.ipj3.platformbackend.user.domain.PlatformUser;
import be.kdg.ipj3.platformbackend.user.domain.repository.PlatformUserRepository;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.oauth2.jwt.Jwt;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class StartGameTest {
    @Mock
    DbLobbyRepository lobbyRepository;
    @Mock
    GameRepository gameRepository;
    @Mock
    PlatformUserRepository platformUserRepository;
    @Mock
    GameApiCatalog gameApiCatalog;

    @InjectMocks
    LobbyService lobbyService;

    @Nested
    class SuccessFlows {
        @Test
        void startGame_shouldReturnGameSessionId_whenAllConditionsMet() {
            // Arrange
            UserId player1Id = new UserId(UUID.randomUUID());
            UserId player2Id = new UserId(UUID.randomUUID());
            LobbyId lobbyId = new LobbyId(UUID.randomUUID());
            GameId gameId = new GameId(UUID.randomUUID());
            UUID expectedSessionId = UUID.randomUUID();
            Map<String, Object> settings = new HashMap<>();
            Jwt mockJwt = mock(Jwt.class);
            when(mockJwt.getTokenValue()).thenReturn("mock-token");

            Lobby mockLobby = mock(Lobby.class);
            Game mockGame = mock(Game.class);
            PlatformUser mockUser1 = mock(PlatformUser.class);
            PlatformUser mockUser2 = mock(PlatformUser.class);

            when(lobbyRepository.findLobbyById(lobbyId)).thenReturn(Optional.of(mockLobby));
            when(mockLobby.getLobbyHost()).thenReturn(new Player(player1Id));
            when(mockLobby.isLobbyFull()).thenReturn(true);
            when(mockLobby.getGameId()).thenReturn(gameId);
            when(mockLobby.getId()).thenReturn(lobbyId); // Used in log

            when(platformUserRepository.findUserById(player1Id)).thenReturn(Optional.of(mockUser1));
            when(platformUserRepository.findUserById(player2Id)).thenReturn(Optional.of(mockUser2));
            when(mockUser1.getUserId()).thenReturn(player1Id);
            when(mockUser2.getUserId()).thenReturn(player2Id);

            when(gameRepository.findById(gameId.id())).thenReturn(Optional.of(mockGame));
            when(mockGame.getGameSettings()).thenReturn(settings);

            when(gameApiCatalog.startGameSession(eq(mockGame), anyString(), eq(player1Id.id()), eq(player2Id.id()), eq(settings)))
                    .thenReturn(expectedSessionId);

            // Act
            UUID result = lobbyService.startGame(player1Id, player2Id, lobbyId, mockJwt, settings);

            // Assert
            assertThat(result).isEqualTo(expectedSessionId);
            verify(mockLobby).setCurrentGameSession(expectedSessionId);
            verify(lobbyRepository).save(mockLobby, mockGame);
        }
    }

    @Nested
    class ErrorFlows {
        @Test
        void startGame_shouldThrowException_whenLobbyNotFound() {
            // Arrange
            UserId player1Id = new UserId(UUID.randomUUID());
            UserId player2Id = new UserId(UUID.randomUUID());
            LobbyId lobbyId = new LobbyId(UUID.randomUUID());
            Jwt mockJwt = mock(Jwt.class);
            Map<String, Object> settings = new HashMap<>();
            when(lobbyRepository.findLobbyById(lobbyId)).thenReturn(Optional.empty());

            // Act
            // Assert
            assertThatThrownBy(() -> lobbyService.startGame(player1Id, player2Id, lobbyId, mockJwt, settings))
                    .isInstanceOf(RuntimeException.class);
        }

        @Test
        void startGame_shouldThrowException_whenUserIsNotLobbyManager() {
            // Arrange
            UserId player1Id = new UserId(UUID.randomUUID()); // Requester
            UserId managerId = new UserId(UUID.randomUUID()); // Actual Manager
            UserId player2Id = new UserId(UUID.randomUUID());
            LobbyId lobbyId = new LobbyId(UUID.randomUUID());
            Jwt mockJwt = mock(Jwt.class);
            Map<String, Object> settings = new HashMap<>();

            Lobby mockLobby = mock(Lobby.class);
            when(lobbyRepository.findLobbyById(lobbyId)).thenReturn(Optional.of(mockLobby));
            when(mockLobby.getLobbyHost()).thenReturn(new Player(managerId));
            when(mockLobby.notLobbyHostForbidden()).thenReturn(new ForbiddenException("Forbidden"));

            // Act
            // Assert
            assertThatThrownBy(() -> lobbyService.startGame(player1Id, player2Id, lobbyId, mockJwt, settings))
                    .isInstanceOf(RuntimeException.class)
                    .hasMessage("Forbidden");
        }

        @Test
        void startGame_shouldThrowException_whenLobbyIsNotFull() {
            // Arrange
            UserId player1Id = new UserId(UUID.randomUUID());
            UserId player2Id = new UserId(UUID.randomUUID());
            LobbyId lobbyId = new LobbyId(UUID.randomUUID());
            Jwt mockJwt = mock(Jwt.class);
            Map<String, Object> settings = new HashMap<>();

            Lobby mockLobby = mock(Lobby.class);
            when(lobbyRepository.findLobbyById(lobbyId)).thenReturn(Optional.of(mockLobby));
            when(mockLobby.getLobbyHost()).thenReturn(new Player(player1Id));
            when(mockLobby.isLobbyFull()).thenReturn(false);
            when(mockLobby.notFullConflict()).thenReturn(new ConflictException("Not Full"));

            // Act
            // Assert
            assertThatThrownBy(() -> lobbyService.startGame(player1Id, player2Id, lobbyId, mockJwt, settings))
                    .isInstanceOf(RuntimeException.class)
                    .hasMessage("Not Full");
        }

        @Test
        void startGame_shouldThrowException_whenGameNotFound() {
            // Arrange
            UserId player1Id = new UserId(UUID.randomUUID());
            UserId player2Id = new UserId(UUID.randomUUID());
            LobbyId lobbyId = new LobbyId(UUID.randomUUID());
            GameId gameId = new GameId(UUID.randomUUID());
            Jwt mockJwt = mock(Jwt.class);
            Map<String, Object> settings = new HashMap<>();

            Lobby mockLobby = mock(Lobby.class);
            when(lobbyRepository.findLobbyById(lobbyId)).thenReturn(Optional.of(mockLobby));
            when(mockLobby.getLobbyHost()).thenReturn(new Player(player1Id));
            when(mockLobby.isLobbyFull()).thenReturn(true);
            when(mockLobby.getGameId()).thenReturn(gameId);

            when(platformUserRepository.findUserById(player1Id)).thenReturn(Optional.of(mock(PlatformUser.class)));
            when(platformUserRepository.findUserById(player2Id)).thenReturn(Optional.of(mock(PlatformUser.class)));
            when(gameRepository.findById(gameId.id())).thenReturn(Optional.empty());

            // Act
            // Assert
            assertThatThrownBy(() -> lobbyService.startGame(player1Id, player2Id, lobbyId, mockJwt, settings))
                    .isInstanceOf(RuntimeException.class);
        }

        @Test
        void startGame_shouldThrowException_whenSettingsMismatch() {
            // Arrange
            UserId player1Id = new UserId(UUID.randomUUID());
            UserId player2Id = new UserId(UUID.randomUUID());
            LobbyId lobbyId = new LobbyId(UUID.randomUUID());
            GameId gameId = new GameId(UUID.randomUUID());
            Jwt mockJwt = mock(Jwt.class);
            Map<String, Object> settings = new HashMap<>(); // Empty settings
            Map<String, Object> gameSettings = Map.of("difficulty", "hard"); // Game requires 1 setting

            Lobby mockLobby = mock(Lobby.class);
            Game mockGame = mock(Game.class);

            when(lobbyRepository.findLobbyById(lobbyId)).thenReturn(Optional.of(mockLobby));
            when(mockLobby.getLobbyHost()).thenReturn(new Player(player1Id));
            when(mockLobby.isLobbyFull()).thenReturn(true);
            when(mockLobby.getGameId()).thenReturn(gameId);
            when(mockLobby.notAllGameSettingsWereSet()).thenReturn(new BadRequestException("Settings Mismatch"));

            when(platformUserRepository.findUserById(player1Id)).thenReturn(Optional.of(mock(PlatformUser.class)));
            when(platformUserRepository.findUserById(player2Id)).thenReturn(Optional.of(mock(PlatformUser.class)));

            when(gameRepository.findById(gameId.id())).thenReturn(Optional.of(mockGame));
            when(mockGame.getGameSettings()).thenReturn(gameSettings);

            // Act
            // Assert
            assertThatThrownBy(() -> lobbyService.startGame(player1Id, player2Id, lobbyId, mockJwt, settings))
                    .isInstanceOf(RuntimeException.class)
                    .hasMessage("Settings Mismatch");
        }
    }
}
