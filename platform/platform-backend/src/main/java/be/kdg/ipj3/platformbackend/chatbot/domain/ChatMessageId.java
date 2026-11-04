package be.kdg.ipj3.platformbackend.chatbot.domain;

import be.kdg.ipj3.platformbackend.shared.domain.exception.NotFoundException;
import lombok.extern.slf4j.Slf4j;

import java.util.UUID;

@Slf4j
public record ChatMessageId(UUID id) {
    public ChatMessageId() {
        this(UUID.randomUUID());
    }

    public NotFoundException notFound() {
        log.error("Message with id {} not found", id);
        return new NotFoundException("Message [" + id + "] not found");
    }
}
