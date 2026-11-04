package be.kdg.ipj3.platformbackend.chatbot.api.dtos;

import java.time.LocalDateTime;

public record ReceivedChatMessageDto(String text, LocalDateTime sentTime, String gameName, String currentPageUrl) {
}
