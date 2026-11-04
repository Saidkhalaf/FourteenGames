package be.kdg.ipj3.tictactoebackend.domain.achievement;

import be.kdg.ipj3.tictactoebackend.domain.CharacterId;

import java.time.LocalDateTime;

public class AchievementCompletion {
    private final AchievementId achievementId;
    private final CharacterId characterId;
    private LocalDateTime completionTime;

    public AchievementCompletion(LocalDateTime completionTime, CharacterId characterId, AchievementId achievementId) {
        this.completionTime = completionTime;
        this.characterId = characterId;
        this.achievementId = achievementId;
    }
}
