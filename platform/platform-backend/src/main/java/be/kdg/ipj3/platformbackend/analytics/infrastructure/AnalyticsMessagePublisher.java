package be.kdg.ipj3.platformbackend.analytics.infrastructure;

import be.kdg.ipj3.platformbackend.analytics.messages.*;
import be.kdg.ipj3.platformbackend.shared.config.rabbitMQ.RabbitMQProperties;
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

    public void publishUserLoggedInMessage(UserLoggedInMessage message) {
        publishMessage(message, properties.getAnalyticsUserLoggedInBinding());
    }

    // not possible with our current architecture. (handled by keycloak)
    public void publishUserLoggedOutMessage(UserLoggedOutMessage message) {
        publishMessage(message, properties.getAnalyticsUserLoggedOutBinding());
    }

    // not possible with our current architecture. (handled by keycloak)
    public void publishUserRegisteredMessage(UserRegisteredMessage message) {
        publishMessage(message, properties.getAnalyticsUserRegisteredBinding());
    }

    public void publishFriendAddedMessage(FriendAddedMessage message) {
        publishMessage(message, properties.getAnalyticsFriendAddedBinding());
    }

    // not possible with our current architecture.
    public void publishGamePageVisitMessage(GamePageVisitMessage message) {
        publishMessage(message, properties.getAnalyticsGamePageVisitBinding());
    }

    public void publishPurchaseMadeMessage(PurchaseMadeMessage message) {
        publishMessage(message, properties.getAnalyticsPurchaseMadeBinding());
    }

    public void publishPaymentMadeMessage(PaymentMadeMessage message) {
        publishMessage(message, properties.getAnalyticsPaymentMadeBinding());
    }

    // not possible with our current architecture.
    public void publishSystemErrorMessage(SystemErrorMessage message) {
        publishMessage(message, properties.getAnalyticsSystemErrorBinding());
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

