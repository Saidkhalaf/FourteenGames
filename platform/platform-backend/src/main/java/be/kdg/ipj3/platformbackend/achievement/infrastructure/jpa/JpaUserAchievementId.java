package be.kdg.ipj3.platformbackend.achievement.infrastructure.jpa;

import be.kdg.ipj3.platformbackend.achievement.domain.UserAchievementId;
import jakarta.persistence.Embeddable;
import lombok.Getter;

import java.util.UUID;

@Embeddable
@Getter
public class JpaUserAchievementId {
    private UUID achievementId;
    private UUID userId;

    public JpaUserAchievementId() {
    }

    public JpaUserAchievementId(UUID achievementId, UUID userId) {
        this.achievementId = achievementId;
        this.userId = userId;
    }

    public static JpaUserAchievementId fromDomain(UserAchievementId userAchievementId) {
        return new JpaUserAchievementId(
                userAchievementId.achievementId().id(),
                userAchievementId.userId().id()
        );
    }

}
