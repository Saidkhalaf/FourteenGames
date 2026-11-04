package be.kdg.ipj3.platformbackend.lobby.api.dtos;

import be.kdg.ipj3.platformbackend.lobby.domain.Lobby;
import be.kdg.ipj3.platformbackend.lobby.domain.Player;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public record LobbyDto(UUID id,UUID gameId,UUID currentGameSessionId,UUID lobbyHostId, List<PlayerDto> players, LocalDateTime creationDate, int maxPlayers) {
    public static LobbyDto from(final Lobby lobby) {
        Player host = lobby.getLobbyHost();
        return new LobbyDto(
                lobby.getId().id(),
                lobby.getGameId().id(),
                lobby.getCurrentGameSessionId(),
                host != null ? host.userId().id(): null,
                lobby.getPlayers()
                        .stream()
                        .map(PlayerDto::from)
                        .toList(),
                lobby.getCreationDate(),
                lobby.getMaxPlayerCount()
        );
    }
}