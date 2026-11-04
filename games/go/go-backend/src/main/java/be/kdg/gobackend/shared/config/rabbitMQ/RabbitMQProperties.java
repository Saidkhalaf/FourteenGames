package be.kdg.gobackend.shared.config.rabbitMQ;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@AllArgsConstructor
@ConfigurationProperties(prefix = "spring.rabbitmq.fourteengames")
public class RabbitMQProperties {
    private final String exchangeName;
    private final String registerGameQueue;
    private final String registerGameBinding;
    private final String unlockAchievementBinding;

    private final String analyticsExchange;
    private final String analyticsGameStartedBinding;
    private final String analyticsGameEndedBinding;
    private final String analyticsGameAbandonedBinding;
    private final String analyticsSessionStartedBinding;
    private final String analyticsWinnerDeclaredBinding;
    private final String analyticsAchievementUnlockedBinding;
}
