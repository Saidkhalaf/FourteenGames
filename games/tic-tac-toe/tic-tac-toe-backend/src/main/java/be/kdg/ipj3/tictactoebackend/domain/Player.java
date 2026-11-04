package be.kdg.ipj3.tictactoebackend.domain;

import lombok.Getter;

@Getter
public class Player extends GameCharacter {
    private final String email;

    public Player(CharacterId id) {
        super(id, "no-name", "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcR99-ZMZeEtYlFVdT-HN3Hz0f_i64Zf76D67g&s");
        this.email = "no-email";
    }

    public Player(CharacterId id, String name, String icon, String email) {
        super(id, name, icon);
        this.email = email;
    }
}
