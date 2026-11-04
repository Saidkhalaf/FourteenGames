package be.kdg.ipj3.platformbackend.achievement.api;

import be.kdg.ipj3.platformbackend.achievement.domain.Achievement;

import java.util.UUID;

public record AchievementDto(UUID id, String name, String description) {
    public static AchievementDto from(Achievement achievement) {
        return new AchievementDto(
                achievement.getId().id(),
                achievement.getName(),
                achievement.getDescription()
        );
    }
}
