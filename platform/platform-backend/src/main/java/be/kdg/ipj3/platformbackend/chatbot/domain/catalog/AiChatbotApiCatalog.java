package be.kdg.ipj3.platformbackend.chatbot.domain.catalog;

import be.kdg.ipj3.platformbackend.chatbot.api.dtos.chatbot.ChatbotAnswerDto;
import be.kdg.ipj3.platformbackend.chatbot.api.dtos.chatbot.ChatbotRequestDto;

public interface AiChatbotApiCatalog {
    ChatbotAnswerDto askQuestion(ChatbotRequestDto body);
}
