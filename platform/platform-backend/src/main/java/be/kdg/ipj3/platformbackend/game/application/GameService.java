package be.kdg.ipj3.platformbackend.game.application;

import be.kdg.ipj3.platformbackend.achievement.domain.Achievement;
import be.kdg.ipj3.platformbackend.achievement.domain.AchievementId;
import be.kdg.ipj3.platformbackend.game.api.dtos.FullGameDto;
import be.kdg.ipj3.platformbackend.game.domain.Game;
import be.kdg.ipj3.platformbackend.game.domain.GameId;
import be.kdg.ipj3.platformbackend.game.domain.Genre;
import be.kdg.ipj3.platformbackend.game.domain.repository.GameRepository;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
@Transactional
public class GameService {
    private final GameRepository gameRepository;

    public GameService(GameRepository games) {
        this.gameRepository = games;
    }

    public List<Game> findAll() {
        log.info("Returning all Games");
        return gameRepository.findAll();
    }

    public Game find(GameId gameId) {
        log.info("Finding game {}", gameId.id());
        return gameRepository.findById(gameId.id()).orElseThrow(gameId::notFound);
    }

    private Genre findGenre(String name) {
        log.info("Finding genre {}", name);
        return gameRepository.findGenre(name).orElseThrow(Genre::notFound);
    }

    public Game registerGame(FullGameDto gameDto) {
        log.info("Registering Game: {}, id: {}", gameDto.name(), gameDto.id());
        final var game = new Game(
                new GameId(gameDto.id()),
                gameDto.name(),
                gameDto.maxPlayerCount(),
                gameDto.description(),
                gameDto.price(),
                gameDto.image(),
                gameDto.icon(),
                gameDto.url(),
                findGenre(gameDto.genre()),
                gameDto.achievements().stream()
                        .map(dto -> new Achievement(
                                new AchievementId(dto.id()), dto.name(), dto.description()))
                        .toList(),
                gameDto.aiStartGameEndpoint(),
                gameDto.startGameEndpoint(),
                gameDto.configurableSettings()
        );
        gameRepository.save(game);
        return game;
    }
}
