package be.kdg.ipj3.platformbackend.achievement.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

@AllArgsConstructor
@Slf4j
@Getter
public class Achievement {
    private final AchievementId id;
    private final String name;
    private final String Description;

}
