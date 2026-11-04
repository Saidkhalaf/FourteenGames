package be.kdg.ipj3.platformbackend.chatbot.api.dtos;

import be.kdg.ipj3.platformbackend.chatbot.domain.ChatMessage;

import java.time.LocalDateTime;
import java.util.UUID;

public record ChatMessageDto(UUID id, UUID sender, String text, LocalDateTime sentTime) {
    public static ChatMessageDto from(ChatMessage chatMessage) {
        return new ChatMessageDto(
                chatMessage.getId().id(),
                chatMessage.getSender(),
                chatMessage.getText(),
                chatMessage.getSentTme()
        );
    }
}
