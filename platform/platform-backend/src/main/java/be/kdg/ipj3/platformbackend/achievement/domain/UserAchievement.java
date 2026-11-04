package be.kdg.ipj3.platformbackend.achievement.domain;

import java.time.LocalDateTime;

public record UserAchievement(UserAchievementId id, LocalDateTime dateAchieved) {
}
