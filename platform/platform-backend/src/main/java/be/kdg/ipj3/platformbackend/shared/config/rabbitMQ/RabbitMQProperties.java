package be.kdg.ipj3.platformbackend.shared.config.rabbitMQ;

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
    private final String unlockAchievementQueue;
    private final String unlockAchievementBinding;

    private final String analyticsExchange;
    private final String analyticsPurchaseMadeBinding;
    private final String analyticsPaymentMadeBinding;
    private final String analyticsGamePageVisitBinding;
    private final String analyticsUserLoggedInBinding;
    private final String analyticsUserLoggedOutBinding;
    private final String analyticsUserRegisteredBinding;
    private final String analyticsFriendAddedBinding;
    private final String analyticsSystemErrorBinding;
}
