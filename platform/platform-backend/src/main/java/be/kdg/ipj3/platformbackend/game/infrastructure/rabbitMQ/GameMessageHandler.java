package be.kdg.ipj3.platformbackend.game.infrastructure.rabbitMQ;


import be.kdg.ipj3.platformbackend.game.application.GameService;

import be.kdg.ipj3.platformbackend.game.infrastructure.rabbitMQ.messages.RegisterGameMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class GameMessageHandler {
    private final GameService gameService;

    public GameMessageHandler(GameService gameService) {
        this.gameService = gameService;
    }

    @RabbitListener(queues = "${spring.rabbitmq.fourteengames.register-game-queue}")
    void onRegisterGame(RegisterGameMessage message){
        log.info("Register game message received for Game: {}", message.gameDto().id());
        try {
            gameService.registerGame(message.gameDto());
        } catch (IllegalStateException e) {
            log.error("Cannot register game {}: {}", message.gameDto().id(), e.getMessage());
        }
    }
}
