package be.kdg.gobackend.game.domain.achievement;

import java.util.UUID;

public interface AchievementPublisher {
  void unlock(UUID playerId, Achievement achievement);
}