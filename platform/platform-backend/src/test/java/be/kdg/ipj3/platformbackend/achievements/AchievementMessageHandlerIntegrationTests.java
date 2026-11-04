package be.kdg.ipj3.platformbackend.achievements;

import be.kdg.ipj3.platformbackend.game.infrastructure.rabbitMQ.messages.AchievementMessageDto;
import be.kdg.ipj3.platformbackend.shared.domain.UserId;
import be.kdg.ipj3.platformbackend.user.domain.PlatformUser;
import be.kdg.ipj3.platformbackend.user.domain.repository.PlatformUserRepository;
import org.awaitility.Awaitility;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.RabbitMQContainer;
import org.testcontainers.containers.wait.strategy.HttpWaitStrategy;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.nginx.NginxContainer;
import org.testcontainers.utility.DockerImageName;

import java.time.Duration;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@Testcontainers
@ActiveProfiles("test")
public class AchievementMessageHandlerIntegrationTests {
    private static final DockerImageName NGINX_IMAGE =
            DockerImageName.parse("nginx:1.27-alpine");

    @Container
    static NginxContainer nginx = new NginxContainer(NGINX_IMAGE)
            .waitingFor(new HttpWaitStrategy()
                    .forPath("/")
                    .forStatusCode(200)
                    .withStartupTimeout(Duration.ofSeconds(30)));

    @Container
    static RabbitMQContainer rabbit = new RabbitMQContainer("rabbitmq:3.12-management")
            .withExposedPorts(5672);

    @DynamicPropertySource
    static void configureRabbit(DynamicPropertyRegistry registry) {
        registry.add("spring.rabbitmq.host", rabbit::getHost);
        registry.add("spring.rabbitmq.port", rabbit::getAmqpPort);
        registry.add("spring.rabbitmq.username", rabbit::getAdminUsername);
        registry.add("spring.rabbitmq.password", rabbit::getAdminPassword);
        registry.add("spring.rabbitmq.fourteengames.register-game-queue", () -> "register_game");
    }

    @DynamicPropertySource
    static void configureNginx(DynamicPropertyRegistry registry) {
        registry.add("urlchecker.base-url", () ->
                "http://" + nginx.getHost() + ":" + nginx.getMappedPort(80)
        );
    }


    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private PlatformUserRepository userRepository;

    @Nested
    class UnlockAchievementFlows {
        @Test
        void whenMessageSent_itIsConsumed() {
            //Arrange
            AchievementMessageDto dto = new AchievementMessageDto(
                    UUID.fromString("11111111-1111-1111-1234-111111111111"),
                    UUID.fromString("f47ac10b-58cc-4372-a567-0e02b2c3d479")
            );

            //Act
            rabbitTemplate.convertAndSend("xivgames_exchange", "achievement.unlock", dto);

            //Assert
            Awaitility.await()
                    .atMost(Duration.ofSeconds(10))
                    .untilAsserted(() -> {
                        UserId userId = new UserId(dto.userId());
                        PlatformUser user = userRepository.findUserById(userId).orElseThrow(userId::notFound);

                        assertEquals(1, user.getAchievements().size());
                        assertEquals(dto.achievementId(), user.getAchievements().getFirst().id().achievementId().id());
                    });

        }

        @Test
        void whenMalformedMessageIsSent_listenerDoesNotCrash() {
            //Arrange
            UserId userId = new UserId(UUID.fromString("11111111-1111-1111-1234-111111111111"));
            PlatformUser user = userRepository.findUserById(userId).orElseThrow(userId::notFound);

            String badJson = "{ \"invalid\": \"data\" }";

            //Act
            rabbitTemplate.convertAndSend("xivgames_exchange", "achievement.unlock", badJson);

            //Assert
            Awaitility.await()
                    .atMost(Duration.ofSeconds(10))
                    .untilAsserted(() -> {
                        PlatformUser postTestUser = userRepository.findUserById(userId).orElseThrow(userId::notFound);
                        assertEquals(user.getAchievements().size(), postTestUser.getAchievements().size());
                    });

        }
    }
}
