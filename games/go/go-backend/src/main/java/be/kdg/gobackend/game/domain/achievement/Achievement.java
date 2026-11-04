package be.kdg.gobackend.game.domain.achievement;

import lombok.Getter;

import java.util.UUID;

@Getter
public enum Achievement {

    // Map your JSON data here
    LETS_GO(
            UUID.fromString("f47ac10b-58cc-4372-a567-0e02b2c3d479"),
            "Let's Go",
            "Start a go game for the first time"
    ),
    GO_HOME(
            UUID.fromString("9b2c7e4a-3f1d-4a82-9c3b-2b8a6d4f1e57"),
            "Go Home",
            "Lose a game of go"
    ),
    LETS_GOOO(
            UUID.fromString("9dcd4a33-ef51-4d55-9dce-aaf28573be0e"),
            "LET'S GOOO!!!",
            "Win a game of go"
    ),
    GO_AHEAD(
            UUID.fromString("55b70408-17b5-499a-820e-c2254dea694b"),
            "Go ahead",
            "Pass your turn"
    ),
    GO_BIG_OR_GO_HOME(
            UUID.fromString("c6f78e0c-b0f4-443b-b678-57abc3f84b9e"),
            "Go big or go home",
            "Start a game on 19x19 board"
    );

    private final UUID id;
    private final String title;
    private final String description;

    Achievement(UUID id, String title, String description) {
        this.id = id;
        this.title = title;
        this.description = description;
    }
}