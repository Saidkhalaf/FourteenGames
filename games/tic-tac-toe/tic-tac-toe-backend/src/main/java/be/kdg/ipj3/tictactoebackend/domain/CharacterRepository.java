package be.kdg.ipj3.tictactoebackend.domain;

import java.util.Optional;

public interface CharacterRepository {
    public Optional <GameCharacter> get(CharacterId id);
    void save(Player player);
}
