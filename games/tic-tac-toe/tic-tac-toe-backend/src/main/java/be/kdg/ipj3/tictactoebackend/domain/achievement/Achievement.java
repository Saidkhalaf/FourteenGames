package be.kdg.ipj3.tictactoebackend.domain.achievement;

public class Achievement {
    private final AchievementId id;
    private String name;
    private int value;

    public Achievement(AchievementId id, String name, int value) {
        this.id = id;
        this.name = name;
        this.value = value;
    }
}
