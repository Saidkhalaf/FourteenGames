package be.kdg.ipj3.platformbackend.lobby.domain;

import be.kdg.ipj3.platformbackend.shared.domain.exception.NotFoundException;
import lombok.extern.slf4j.Slf4j;

import java.util.UUID;

@Slf4j
public record LobbyId(UUID id) {
    public LobbyId() {this(UUID.randomUUID());}

    public NotFoundException notFound() {
        log.error("Lobby with id {} not found", id);
        return new NotFoundException("Lobby [" + id + "] not found");
    }
}