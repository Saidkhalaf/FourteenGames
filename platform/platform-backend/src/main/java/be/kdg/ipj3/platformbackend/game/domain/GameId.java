package be.kdg.ipj3.platformbackend.game.domain;

import be.kdg.ipj3.platformbackend.shared.domain.exception.NotFoundException;
import lombok.extern.slf4j.Slf4j;

import java.util.UUID;

@Slf4j
public record GameId(UUID id) {
    public GameId() { this(UUID.randomUUID());}

    public NotFoundException notFound() {
        log.error("Game with id {} not found", id);
        return new NotFoundException("Game [" + id + "] not found");
    }
}
