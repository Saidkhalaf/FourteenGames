package be.kdg.ipj3.tictactoebackend.application;

import be.kdg.ipj3.tictactoebackend.domain.*;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service @Transactional @Slf4j public class PlayerService {

    private final GameStateRepository gameStateRepository;
    private final CharacterRepository characterRepository;

    public PlayerService(GameStateRepository gameStateRepository, CharacterRepository characterRepository) {
        this.gameStateRepository = gameStateRepository;
        this.characterRepository = characterRepository;
    }

    public GameCharacter getOrCreatePlayer(CharacterId characterId) {
        log.info("Getting player {}", characterId.id());
        return characterRepository.get(characterId).orElseGet(() -> {
            log.info("Creating player {}", characterId.id());
            final var player = new Player(characterId);
            characterRepository.save(player);
            return player;
        });
    }
}
