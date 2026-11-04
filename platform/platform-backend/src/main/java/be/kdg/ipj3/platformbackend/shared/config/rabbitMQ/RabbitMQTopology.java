package be.kdg.ipj3.platformbackend.shared.config.rabbitMQ;

import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQTopology {

    private final RabbitMQProperties properties;

    public RabbitMQTopology(RabbitMQProperties properties) {
        this.properties = properties;
    }

    @Bean
    TopicExchange xivExchange() {
        return new TopicExchange(properties.getExchangeName());
    }

    //REGISTER GAME
    @Bean
    Queue registerGameQueue(){
        return QueueBuilder.nonDurable(properties.getRegisterGameQueue()).build();
    }

    @Bean
    Binding registerGameBinging(){
        return BindingBuilder.bind(registerGameQueue()).to(xivExchange()).with(properties.getRegisterGameBinding());
    }

    //GAIN ACHIEVEMENT
    @Bean
    Queue unlockAchievementQueue(){
        return QueueBuilder.nonDurable(properties.getUnlockAchievementQueue()).build();
    }

    @Bean
    Binding unlockAchievementBinding(){
        return BindingBuilder.bind(unlockAchievementQueue()).to(xivExchange()).with(properties.getUnlockAchievementBinding());
    }

    // ========== Analytics Topology ==========

    @Bean
    TopicExchange analyticsExchange() {
        return new TopicExchange(properties.getAnalyticsExchange(), true, false);
    }

    @Bean
    Queue analyticsQueue() {
        return QueueBuilder.durable("analytics.queue").build();
    }

    @Bean
    Binding analyticsBinding() {
        // Bind with wildcard to catch all analytics events
        return BindingBuilder.bind(analyticsQueue()).to(analyticsExchange()).with("#");
    }
}
