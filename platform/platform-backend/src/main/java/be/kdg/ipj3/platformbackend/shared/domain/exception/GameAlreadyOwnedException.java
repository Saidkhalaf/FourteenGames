package be.kdg.ipj3.platformbackend.shared.domain.exception;

import java.util.UUID;

public class GameAlreadyOwnedException extends RuntimeException {
    public GameAlreadyOwnedException(UUID gameId) {
        super("Game " + gameId + " already owned");
    }
}