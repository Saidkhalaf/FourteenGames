package be.kdg.ipj3.platformbackend.chatbot.domain;

import be.kdg.ipj3.platformbackend.shared.domain.exception.NotFoundException;
import lombok.extern.slf4j.Slf4j;

import java.util.UUID;

@Slf4j
public record ConversationId(UUID id) {
    public ConversationId() {
        this(UUID.randomUUID());
    }

    public NotFoundException notFound() {
        log.error("Conversation with id {} not found", id);
        return new NotFoundException("Conversation [" + id + "] not found");
    }
}
