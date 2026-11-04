package be.kdg.ipj3.platformbackend.lobby;

import be.kdg.ipj3.platformbackend.lobby.application.LobbyService;
import be.kdg.ipj3.platformbackend.lobby.domain.GameSessionId;
import be.kdg.ipj3.platformbackend.lobby.domain.Lobby;
import be.kdg.ipj3.platformbackend.lobby.domain.LobbyId;
import be.kdg.ipj3.platformbackend.lobby.infrastructure.DbLobbyRepository;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class FindLobbyByGameIdTest {
    @Mock
    DbLobbyRepository lobbyRepository;

    @InjectMocks
    LobbyService lobbyService;

    @Nested
    class SuccessFlows {
        @Test
        void findLobbyByGameId_shouldReturnLobby_whenLobbyExists() {
            // Arrange
            GameSessionId gameSessionId = new GameSessionId(UUID.randomUUID());
            LobbyId lobbyId = new LobbyId(UUID.randomUUID());
            Lobby lobby = new Lobby(lobbyId, null, gameSessionId.id(), new ArrayList<>(), LocalDateTime.now(), 4);
            when(lobbyRepository.findLobbyByGameSessionId(gameSessionId)).thenReturn(Optional.of(lobby));
            // Act
            Lobby result = lobbyService.findLobbyByGameId(gameSessionId);
            // Assert
            assertThat(result).isNotNull();
            assertThat(result).isEqualTo(lobby);
            verify(lobbyRepository).findLobbyByGameSessionId(gameSessionId);
        }
    }

    @Nested
    class ErrorFlows {
        @Test
        void findLobbyByGameId_shouldThrowException_whenLobbyDoesNotExist() {
            // Arrange
            GameSessionId gameSessionId = new GameSessionId(UUID.randomUUID());
            when(lobbyRepository.findLobbyByGameSessionId(gameSessionId)).thenReturn(Optional.empty());
            // Act
            // Assert
            assertThatThrownBy(() -> lobbyService.findLobbyByGameId(gameSessionId))
                    .isInstanceOf(RuntimeException.class);
            verify(lobbyRepository).findLobbyByGameSessionId(gameSessionId);
        }
    }
}
