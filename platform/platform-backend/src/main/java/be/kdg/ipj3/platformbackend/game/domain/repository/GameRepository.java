package be.kdg.ipj3.platformbackend.game.domain.repository;

import be.kdg.ipj3.platformbackend.game.domain.Game;
import be.kdg.ipj3.platformbackend.game.domain.Genre;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface GameRepository {
    void save(Game game);
    List<Game> findAll();
    Optional<Game> findById(UUID id);
    Optional<Genre> findGenre(String name);
}
