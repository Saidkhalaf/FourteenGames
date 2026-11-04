package be.kdg.ipj3.platformbackend.user.api.dtos;

import java.util.UUID;

public record FavoriteGameDto(UUID userid, UUID gameId) {
}
