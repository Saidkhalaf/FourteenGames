package be.kdg.ipj3.platformbackend.games;

import be.kdg.ipj3.platformbackend.game.api.dtos.FullGameDto;
import be.kdg.ipj3.platformbackend.game.domain.Game;
import be.kdg.ipj3.platformbackend.game.domain.repository.GameRepository;
import be.kdg.ipj3.platformbackend.game.infrastructure.rabbitMQ.messages.RegisterGameMessage;
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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@Testcontainers
@ActiveProfiles("test")
public class GameMessageHandlerIntegrationTests {


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
    private GameRepository gameRepository;

    @Nested
    class RegisterGameFlows {
        @Test
        void whenMessageSent_itIsConsumed() {
            //Arrange

            String url = "http://" + nginx.getHost() + ":" + nginx.getMappedPort(80);

            FullGameDto dto = new FullGameDto(
                    UUID.randomUUID(),
                    "Tic Tac Toe",
                    2,
                    "Game where you..",
                    20,
                    "testImg.png",
                    "testicon.png",
                    "Strategy",
                    url,
                    new ArrayList<>(),
                    url+"/start/ai",
                    url+"/start",
                    new HashMap<>()
                    );
            //Replicating the genre from "test_data.sql";

            RegisterGameMessage message = new RegisterGameMessage(dto);

            //Act
            //rabbitTemplate.convertAndSend("register-game-test-queue", message);
            rabbitTemplate.convertAndSend("xivgames_exchange", "game.register", message);

            //Assert
            Awaitility.await()
                            .atMost(Duration.ofSeconds(10))
                                    .untilAsserted(() -> {
                                        var games = gameRepository.findAll();

                                        assertEquals(2, games.size());
                                        Game saved = games.get(1);

                                        assertEquals(dto.id(), saved.getId().id());
                                        assertEquals(dto.name(), saved.getName());
                                        assertEquals(dto.url(), saved.getUrl());
                                        assertEquals(dto.genre(), saved.getGenre().getName());
                                    });

        }

        @Test
        void whenMalformedMessageIsSent_listenerDoesNotCrash() {
            //Arrange
            List<Game> gamesList = gameRepository.findAll();
            String badJson = "{ \"invalid\": \"data\" }";

            //Act
            rabbitTemplate.convertAndSend("xivgames_exchange", "game.register", badJson);

            //Assert
            Awaitility.await()
                    .atMost(Duration.ofSeconds(10))
                    .untilAsserted(() -> {
                        var games = gameRepository.findAll();
                        assertEquals(gamesList.size(), games.size());
                    });

        }
    }
}
