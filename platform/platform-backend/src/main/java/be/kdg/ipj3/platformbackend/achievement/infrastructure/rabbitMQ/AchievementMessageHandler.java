package be.kdg.ipj3.platformbackend.achievement.infrastructure.rabbitMQ;

import be.kdg.ipj3.platformbackend.game.infrastructure.rabbitMQ.messages.AchievementMessageDto;
import be.kdg.ipj3.platformbackend.user.application.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class AchievementMessageHandler {
    private final UserService userService;

    public AchievementMessageHandler(UserService userService) {
        this.userService = userService;
    }

    @RabbitListener(queues = "${spring.rabbitmq.fourteengames.unlock-achievement-queue}")
    void onAchievementUnlock(AchievementMessageDto dto){
        log.info("Unlock achievement {} message received for user: {}",dto.achievementId(), dto.userId());
        userService.unlockAchievement(dto);
    }
}
