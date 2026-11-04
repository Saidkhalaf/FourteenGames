package be.kdg.gobackend.registration.infrastructure.messages;

import java.util.UUID;

public record AchievementMessageDto(UUID userId, UUID achievementId) {
}
