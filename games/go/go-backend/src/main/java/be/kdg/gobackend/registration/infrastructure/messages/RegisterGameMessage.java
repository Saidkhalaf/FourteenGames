package be.kdg.gobackend.registration.infrastructure.messages;

import be.kdg.gobackend.registration.api.dto.FullGameDto;

public record RegisterGameMessage(FullGameDto gameDto) {
}
