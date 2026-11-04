package be.kdg.ipj3.platformbackend.game.infrastructure.rabbitMQ.messages;


import be.kdg.ipj3.platformbackend.game.api.dtos.FullGameDto;

public record RegisterGameMessage(FullGameDto gameDto) {
}
