package be.kdg.ipj3.platformbackend.lobby;

import be.kdg.ipj3.platformbackend.lobby.application.LobbyService;
import be.kdg.ipj3.platformbackend.lobby.domain.Lobby;
import be.kdg.ipj3.platformbackend.lobby.infrastructure.DbLobbyRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@ExtendWith(MockitoExtension.class)
public class FindAllLobbiesTest {
    @Mock
    DbLobbyRepository lobbyRepository;

    @InjectMocks
    LobbyService lobbyService;

    @Test
    void findAllLobbies_shouldReturnListOfLobbies() {
        // Arrange
        List<Lobby> expectedLobbies = List.of(new Lobby(null, null, null, null, LocalDateTime.now(),0));

        Mockito.when(lobbyRepository.findAllLobbies())
                .thenReturn(expectedLobbies);
        // Act
        List<Lobby> result = lobbyService.findAllLobbies();
        // Assert
        assertThat(result).isEqualTo(expectedLobbies);
        assertThat(result.size()).isEqualTo(1);
    }
}
