package be.kdg.gobackend.registration.api.dto;

import java.util.UUID;

public record AchievementDto(UUID id, String name, String description) {
}
