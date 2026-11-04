package be.kdg.ipj3.platformbackend.achievement.domain;

import be.kdg.ipj3.platformbackend.shared.domain.exception.NotFoundException;
import lombok.extern.slf4j.Slf4j;

import java.util.UUID;

@Slf4j
public record AchievementId(UUID id) {
    public AchievementId() {this(UUID.randomUUID());}

    public NotFoundException notFound() {
        log.error("Achievement with id {} not found", id);
        return new NotFoundException("Achievement [" + id + "] not found");
    }
}
