package be.kdg.ipj3.tictactoebackend.infrastructure.user;

import be.kdg.ipj3.tictactoebackend.domain.CharacterId;
import be.kdg.ipj3.tictactoebackend.domain.CharacterRepository;
import be.kdg.ipj3.tictactoebackend.domain.GameCharacter;
import be.kdg.ipj3.tictactoebackend.domain.Player;
import be.kdg.ipj3.tictactoebackend.infrastructure.user.jpa.JpaGameCharacterEntity;
import be.kdg.ipj3.tictactoebackend.infrastructure.user.jpa.JpaGameCharacterRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Slf4j
@Repository
public class DbCharacterRepository implements CharacterRepository {

    private final JpaGameCharacterRepository jpaGameCharacterRepository;

    public DbCharacterRepository(JpaGameCharacterRepository jpaGameCharacterRepository) {
        this.jpaGameCharacterRepository = jpaGameCharacterRepository;
    }

    @Override public Optional<GameCharacter> get(CharacterId id) {
        log.info("Getting character from database");
        return jpaGameCharacterRepository.findById(id.id()).map(JpaGameCharacterEntity::toDomain);
    }

    @Override public void save(Player player) {
        log.info("Saving player to database");
        jpaGameCharacterRepository.save(JpaGameCharacterEntity.fromDomain(player));
    }
}
