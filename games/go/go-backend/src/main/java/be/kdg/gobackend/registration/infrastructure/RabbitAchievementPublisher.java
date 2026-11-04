package be.kdg.gobackend.registration.infrastructure;

import be.kdg.gobackend.analytics.infrastructure.AnalyticsMessagePublisher;
import be.kdg.gobackend.analytics.infrastructure.messages.AchievementUnlockedMessage;
import be.kdg.gobackend.game.domain.achievement.Achievement;
import be.kdg.gobackend.game.domain.achievement.AchievementPublisher;
import be.kdg.gobackend.registration.infrastructure.messages.AchievementMessageDto;
import be.kdg.gobackend.shared.config.rabbitMQ.RabbitMQProperties;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class RabbitAchievementPublisher implements AchievementPublisher {

    private final RabbitTemplate rabbitTemplate;
    private final RabbitMQProperties properties;
    private final AnalyticsMessagePublisher analyticsMessagePublisher;

    // The method now takes the Enum, not a String ID
    @Override
    public void unlock(UUID playerId, Achievement achievement) {
        log.info("Sending achievement unlocked message for player {} and achievement {} : {} ({})", playerId, achievement.getTitle(), achievement.getDescription(), achievement.getId());
        rabbitTemplate.convertAndSend(
                properties.getExchangeName(),
                properties.getUnlockAchievementBinding(),
                new AchievementMessageDto(
                    playerId,
                    achievement.getId()
                )
        );

        analyticsMessagePublisher.publishAchievementUnlockedMessage(
                new AchievementUnlockedMessage(
                        playerId,
                        achievement
                )
        );
    }
}