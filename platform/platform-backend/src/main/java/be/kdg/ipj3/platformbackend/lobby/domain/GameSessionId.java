package be.kdg.ipj3.platformbackend.lobby.domain;

import be.kdg.ipj3.platformbackend.shared.domain.exception.NotFoundException;
import lombok.extern.slf4j.Slf4j;

import java.util.UUID;

@Slf4j
public record GameSessionId(UUID id) {
    public GameSessionId() {this(UUID.randomUUID());}

    public NotFoundException notFound() {
        log.error("No lobby found that has active game session with id: {}", id);
        return new NotFoundException("No lobby found that has active game session with id ["+id + "]");
    }
}