package be.kdg.ipj3.tictactoebackend.infrastructure.user.jpa;

import be.kdg.ipj3.tictactoebackend.domain.GameCharacter;
import be.kdg.ipj3.tictactoebackend.domain.Npc;
import be.kdg.ipj3.tictactoebackend.domain.Player;
import be.kdg.ipj3.tictactoebackend.domain.CharacterId;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.UUID;

@AllArgsConstructor @Entity @Table(name = "game_character") @Getter public class JpaGameCharacterEntity {
    @Id @Column private UUID id;
    @Column private String name;
    @Column private String icon;
    @Column private String email;
    @Column private boolean isNpc;

    protected JpaGameCharacterEntity() {
    }

    public static JpaGameCharacterEntity fromDomain(GameCharacter character) {
        if (character instanceof Npc) {
            return new JpaGameCharacterEntity(
                    character.getId().id(),
                    character.getName(),
                    character.getIcon(),
                    null,
                    true
            );
        } else {
            return new JpaGameCharacterEntity(
                    character.getId().id(),
                    character.getName(),
                    character.getIcon(),
                    ((Player) character).getEmail(),
                    false
            );
        }
    }

    public GameCharacter toDomain() {
        if (this.isNpc) {
            return new Npc(
                    new CharacterId(id),
                    this.name,
                    this.icon
            );
        } else {
            return new Player(
                    new CharacterId(id),
                    this.name,
                    this.icon,
                    this.email
            );
        }
    }
}
