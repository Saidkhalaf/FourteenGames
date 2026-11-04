package be.kdg.ipj3.platformbackend.game.infrastructure;

import be.kdg.ipj3.platformbackend.game.domain.Game;
import be.kdg.ipj3.platformbackend.game.domain.Genre;
import be.kdg.ipj3.platformbackend.game.domain.repository.GameRepository;
import be.kdg.ipj3.platformbackend.game.infrastructure.jpa.entity.JpaGameEntity;
import be.kdg.ipj3.platformbackend.game.infrastructure.jpa.repository.JpaGameRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
@Slf4j
public class DbGameRepository implements GameRepository {

    private final JpaGameRepository jpaGameRepository;

    public DbGameRepository(JpaGameRepository jpaGameRepository) {
        this.jpaGameRepository = jpaGameRepository;
    }

    @Override
    public void save(Game game) {
        jpaGameRepository.save(JpaGameEntity.fromDomain(game));
    }

    @Override
    public List<Game> findAll() {
       return jpaGameRepository.findAllWithAchievements().stream().map(JpaGameEntity::toDomain).toList();
    }

    @Override
    public Optional<Game> findById(UUID id) {
        return jpaGameRepository.findByIdWithAchievements(id).map(JpaGameEntity::toDomain);
    }

    @Override
    public Optional<Genre> findGenre(String name) {
        return jpaGameRepository.findGenreByName(name);
    }


}
