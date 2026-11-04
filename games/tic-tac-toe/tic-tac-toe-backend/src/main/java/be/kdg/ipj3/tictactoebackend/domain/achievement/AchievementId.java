package be.kdg.ipj3.tictactoebackend.domain.achievement;

import java.util.UUID;

public record AchievementId(UUID id) {
    public AchievementId() {
        this(UUID.randomUUID());
    }
}
