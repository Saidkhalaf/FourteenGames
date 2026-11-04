package be.kdg.ipj3.platformbackend.chatbot.api.dtos;

import be.kdg.ipj3.platformbackend.chatbot.domain.Conversation;

import java.util.List;
import java.util.UUID;

public record ConversationDto(UUID id, UUID userId, List<ChatMessageDto> messages) {
    public static ConversationDto from(Conversation conversation){
        UUID userId = conversation.getUser() != null
                ? conversation.getUser().id()
                : null;
        return new ConversationDto(
                conversation.getId().id(),
                userId,
                conversation.getMessages().stream().map(ChatMessageDto::from).toList()
        );
    }
}
