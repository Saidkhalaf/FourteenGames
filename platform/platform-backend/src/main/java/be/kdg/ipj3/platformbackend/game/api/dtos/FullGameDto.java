package be.kdg.ipj3.platformbackend.game.api.dtos;

import be.kdg.ipj3.platformbackend.achievement.api.AchievementDto;
import be.kdg.ipj3.platformbackend.game.domain.Game;

import java.util.List;
import java.util.Map;
import java.util.UUID;

public record FullGameDto(UUID id, String name,int maxPlayerCount, String description, double price, String image, String icon, String genre, String url, List<AchievementDto> achievements, String aiStartGameEndpoint, String startGameEndpoint, Map<String, Object> configurableSettings) {
    public static FullGameDto from(final Game game){
        return new FullGameDto(
                game.getId().id(),
                game.getName(),
                game.getMaxPlayerCount(),
                game.getDescription(),
                game.getPrice(),
                game.getImage(),
                game.getIcon(),
                game.getGenre().getName(),
                game.getUrl(),
                game.getAchievements().stream().map(AchievementDto::from).toList(),
                game.getAiGameStartEndpoint(),
                game.getGameStartEndpoint(),
                game.getGameSettings()
        );
    }
}
