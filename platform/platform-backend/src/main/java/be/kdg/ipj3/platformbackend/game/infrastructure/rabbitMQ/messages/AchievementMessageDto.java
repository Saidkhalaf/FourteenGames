package be.kdg.ipj3.platformbackend.game.infrastructure.rabbitMQ.messages;

import java.util.UUID;

public record AchievementMessageDto(UUID userId, UUID achievementId) {
}
