package be.kdg.ipj3.platformbackend.game.domain;

import be.kdg.ipj3.platformbackend.achievement.domain.Achievement;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Map;

@Getter
@Slf4j
@AllArgsConstructor
public class Game {
    private final GameId id;
    private final String name;
    private final int maxPlayerCount;
    private final String description;
    private final double price;
    private final String image;
    private final String icon;
    private final String url;
    private final Genre genre;
    private List<Achievement> achievements;
    private String aiGameStartEndpoint;
    private String gameStartEndpoint;
    private Map<String, Object> gameSettings;
}
