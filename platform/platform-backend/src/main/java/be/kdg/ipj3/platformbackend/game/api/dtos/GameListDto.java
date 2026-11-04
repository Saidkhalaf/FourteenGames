package be.kdg.ipj3.platformbackend.game.api.dtos;

import be.kdg.ipj3.platformbackend.achievement.api.AchievementDto;
import be.kdg.ipj3.platformbackend.game.domain.Game;

import java.util.List;
import java.util.UUID;

//A dto for a limited version of a game to put in a list, not a list of game dtos
public record GameListDto(UUID id, String name, double price, String icon, String genre, List<AchievementDto> achievements) {
    public static GameListDto from(final Game game){
        return new GameListDto(
                game.getId().id(),
                game.getName(),
                game.getPrice(),
                game.getIcon(),
                game.getGenre().getName(),
                game.getAchievements().stream().map(AchievementDto::from).toList()
        );
    }
}
