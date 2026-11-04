package be.kdg.ipj3.platformbackend.chatbot.api.dtos.chatbot;

import java.util.List;
import java.util.UUID;

public record ChatbotRequestDto(String question, UUID playerId, ContextDto context, List<HistoryDto> history) {
}
