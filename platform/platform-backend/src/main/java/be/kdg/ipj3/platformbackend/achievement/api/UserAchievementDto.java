package be.kdg.ipj3.platformbackend.achievement.api;

import be.kdg.ipj3.platformbackend.achievement.domain.UserAchievement;

import java.time.LocalDateTime;
import java.util.UUID;

public record UserAchievementDto(UUID userId, UUID achievementId, LocalDateTime dateAchieved) {
    public static UserAchievementDto from(UserAchievement userAchievement){
        return new UserAchievementDto(
                userAchievement.id().userId().id(),
                userAchievement.id().achievementId().id(),
                userAchievement.dateAchieved()
        );
    }
}
