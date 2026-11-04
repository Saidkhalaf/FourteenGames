package be.kdg.gobackend.shared.config;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.UUID;

@Getter
@AllArgsConstructor
@ConfigurationProperties(prefix = "spring.game.registration")
public class RegistrationConfig {
    private final String internalGameUrl;
    private final String externalGameUrl;

    private final UUID id;
    private final String name;
    private final String description;
    private final int maxPlayers;
    private final String aiStartGameEndpoint;
    private final String startGameEndpoint;
    private final double price;
    private final String image;
    private final String icon;
    private final String genre;
}