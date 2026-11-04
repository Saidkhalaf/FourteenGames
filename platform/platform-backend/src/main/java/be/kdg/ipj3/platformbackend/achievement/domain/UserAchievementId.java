package be.kdg.ipj3.platformbackend.achievement.domain;

import be.kdg.ipj3.platformbackend.shared.domain.UserId;
import be.kdg.ipj3.platformbackend.shared.domain.exception.NotFoundException;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public record UserAchievementId(UserId userId, AchievementId achievementId) {

    public NotFoundException notFound() {
        log.error("No relation between user {} and achievement {}", userId.id(), achievementId.id());
        return new NotFoundException("No relation between user [" + userId.id() + "] and achievement [" + achievementId.id() + "]");
    }

}
