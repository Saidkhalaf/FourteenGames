package be.kdg.gobackend.analytics.infrastructure;

import be.kdg.gobackend.analytics.infrastructure.messages.*;
import be.kdg.gobackend.shared.config.rabbitMQ.RabbitMQProperties;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.AmqpException;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class AnalyticsMessagePublisher {

    private final RabbitTemplate rabbitTemplate;
    private final RabbitMQProperties properties;

    public void publishAchievementUnlockedMessage(AchievementUnlockedMessage message) {
        publishMessage(message, properties.getAnalyticsAchievementUnlockedBinding());
    }

    // abandoning a game as a feature not yet implemented but planned for the future.
    public void publishGameAbandonedMessage(GameAbandonedMessage message) {
        publishMessage(message, properties.getAnalyticsGameAbandonedBinding());
    }

    public void publishGameEndedMessage(GameEndedMessage message) {
        publishMessage(message, properties.getAnalyticsGameEndedBinding());
    }

    public void publishGameStartedMessage(GameStartedMessage message) {
        publishMessage(message, properties.getAnalyticsGameStartedBinding());
    }

    public void publishSessionStartedMessage(SessionStartedMessage message) {
        publishMessage(message, properties.getAnalyticsSessionStartedBinding());
    }

    public void publishWinnerDeclaredMessage(WinnerDeclaredMessage message) {
        publishMessage(message, properties.getAnalyticsWinnerDeclaredBinding());
    }

    private void publishMessage(EventMessage message, String routingKey) {
        try {
            rabbitTemplate.convertAndSend(
                    properties.getAnalyticsExchange(),
                    routingKey,
                    message
            );
            log.info("Published message: {} to routing key: {} | {}", message.event_type(), routingKey, message);
        } catch (AmqpException e) {
            log.warn("Failed to publish message: {} - RabbitMQ connection error: {} | {}",
                    message.event_type(), e.getMessage(), message);
        }
    }
}

