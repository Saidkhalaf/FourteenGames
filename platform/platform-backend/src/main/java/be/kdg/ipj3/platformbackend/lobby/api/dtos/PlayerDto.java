package be.kdg.ipj3.platformbackend.lobby.api.dtos;

import be.kdg.ipj3.platformbackend.lobby.domain.Player;

import java.util.UUID;

public record PlayerDto(UUID userId) {
    public static PlayerDto from(final Player player){
        return new PlayerDto(player.userId().id());
    }
}