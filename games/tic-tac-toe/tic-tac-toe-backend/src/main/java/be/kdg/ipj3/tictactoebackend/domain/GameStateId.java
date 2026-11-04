package be.kdg.ipj3.tictactoebackend.domain;

import lombok.extern.slf4j.Slf4j;

import java.util.UUID;

@Slf4j
public record GameStateId(UUID id) {
    public static GameStateId create() {
        return new GameStateId(UUID.randomUUID());
    }

    public NotFoundException notFound() {
        log.error("Match with id {} not found", id);
        return new NotFoundException("Match [" + id + "] not found");
    }
}
