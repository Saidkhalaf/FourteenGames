package be.kdg.ipj3.tictactoebackend.domain;

import lombok.Getter;

@Getter
public abstract class GameCharacter {
    private final CharacterId id;
    private final String name;
    private final String icon;

    protected GameCharacter(CharacterId id, String name, String icon) {
        this.id = id;
        this.name = name;
        this.icon = icon;
    }
}
