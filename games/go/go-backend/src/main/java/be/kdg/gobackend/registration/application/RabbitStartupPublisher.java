package be.kdg.gobackend.registration.application;

import be.kdg.gobackend.game.domain.achievement.Achievement;
import be.kdg.gobackend.registration.api.dto.AchievementDto;
import be.kdg.gobackend.registration.api.dto.FullGameDto;
import be.kdg.gobackend.registration.infrastructure.UrlChecker;
import be.kdg.gobackend.registration.infrastructure.messages.RegisterGameMessage;
import be.kdg.gobackend.shared.config.RegistrationConfig;
import be.kdg.gobackend.shared.config.rabbitMQ.RabbitMQProperties;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.AmqpException;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.annotation.Profile;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.time.Duration;
import java.util.Arrays;
import java.util.Collections;
import java.util.Map;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.atomic.AtomicReference;

@Slf4j
@Component
@Profile("!test")
@RequiredArgsConstructor
public class RabbitStartupPublisher {

    private final RabbitTemplate rabbitTemplate;
    private final RabbitMQProperties rabbitMQProperties;
    private final RegistrationConfig registrationConfig;
    private final UrlChecker urlChecker;
    private final TaskScheduler taskScheduler;

    @EventListener(ApplicationReadyEvent.class)
    public void publishStartupEvent() {

        final var configurableSettings = loadConfigurableSettings();

        final var updatedDto = new FullGameDto(
                registrationConfig.getId(),
                registrationConfig.getName(),
                registrationConfig.getMaxPlayers(),
                registrationConfig.getAiStartGameEndpoint(),
                registrationConfig.getStartGameEndpoint(),
                registrationConfig.getDescription(),
                registrationConfig.getPrice(),
                registrationConfig.getImage(),
                registrationConfig.getIcon(),
                registrationConfig.getGenre(),
                registrationConfig.getExternalGameUrl(),
                Arrays.stream(Achievement.values())
                        .map(a -> new AchievementDto(
                                a.getId(),
                                a.getTitle(),
                                a.getDescription()))
                        .toList(),
                configurableSettings
        );

        final var futureRef = new AtomicReference<ScheduledFuture<?>>();

        final var future = taskScheduler.scheduleWithFixedDelay(() -> {
            try {
                if (!urlChecker.isUrlReachable(registrationConfig.getInternalGameUrl())) {
                    log.warn("Game not registered yet; url not reachable internally: {}, external is {}", registrationConfig.getInternalGameUrl(), registrationConfig.getExternalGameUrl());
                    return;
                }

                rabbitTemplate.convertAndSend(
                        rabbitMQProperties.getExchangeName(),
                        rabbitMQProperties.getRegisterGameBinding(),
                        new RegisterGameMessage(updatedDto)
                );
                log.info("Startup game message sent to RabbitMQ: {}", updatedDto);

                final var f = futureRef.get();
                if (f != null) f.cancel(false);

            } catch (AmqpException e) {
                // Don’t kill the scheduler thread; just log and let it retry on next tick.
                log.error("Error while trying to register startup game (will retry)", e);
            }
        }, Duration.ofSeconds(5));

        futureRef.set(future);
    }

    private Map<String, Object> loadConfigurableSettings() {
        try (InputStream is = getClass().getClassLoader().getResourceAsStream("configurableSettings.json")) {
            if (is == null) {
                log.error("configurableSettings.json not found in resources");
                return Collections.emptyMap();
            }

            final var objectMapper = new ObjectMapper();
            var rootNode = objectMapper.readTree(is);
            var settingsNode = rootNode.get("configurableSettings");
            if(settingsNode == null || settingsNode.isNull()){
                log.error("configurableSettings key not found in configurableSettings.json");
                return Collections.emptyMap();
            }

            return objectMapper.convertValue(
                    settingsNode,
                    new TypeReference<Map<String, Object>>() {}
            );
        } catch (IOException e) {
            log.error("Failed to read configurableSettings.json", e);
            return Collections.emptyMap();
        }
    }
}