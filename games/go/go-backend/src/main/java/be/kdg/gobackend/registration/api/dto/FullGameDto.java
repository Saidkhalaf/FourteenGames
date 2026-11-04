package be.kdg.gobackend.registration.api.dto;


import java.util.List;
import java.util.Map;
import java.util.UUID;

public record FullGameDto(
        UUID id,
        String name,
        int maxPlayerCount,
        String aiStartGameEndpoint,
        String startGameEndpoint,
        String description,
        double price,
        String image,
        String icon,
        String genre,
        String url,
        List<AchievementDto> achievements,
        Map<String, Object> configurableSettings
) {}
