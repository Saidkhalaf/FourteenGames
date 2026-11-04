package be.kdg.gobackend.analytics.infrastructure.messages;

import be.kdg.gobackend.game.domain.achievement.Achievement;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

public record AchievementUnlockedMessage(
        String event_type,
        String timestamp,
        UUID player_id,
        UUID achievement_id,
        String achievement_name,
        String achievement_category,
        String game_name
) implements EventMessage {
    public AchievementUnlockedMessage(UUID playerId, Achievement achievement) {
        this(
                "achievement_unlocked",
                LocalDateTime.now().atOffset(ZoneOffset.UTC).format(DateTimeFormatter.ISO_INSTANT),
                playerId,
                achievement.getId(),
                achievement.getTitle(),
                "skill",
                "go"
        );
    }
}
