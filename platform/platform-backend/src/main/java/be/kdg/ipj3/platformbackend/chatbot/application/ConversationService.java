package be.kdg.ipj3.platformbackend.chatbot.application;

import be.kdg.ipj3.platformbackend.chatbot.api.dtos.chatbot.ChatbotAnswerDto;
import be.kdg.ipj3.platformbackend.chatbot.api.dtos.chatbot.ChatbotRequestDto;
import be.kdg.ipj3.platformbackend.chatbot.api.dtos.chatbot.ContextDto;
import be.kdg.ipj3.platformbackend.chatbot.domain.Conversation;
import be.kdg.ipj3.platformbackend.chatbot.domain.ConversationId;
import be.kdg.ipj3.platformbackend.chatbot.domain.catalog.AiChatbotApiCatalog;
import be.kdg.ipj3.platformbackend.chatbot.domain.repository.ConversationRepository;
import be.kdg.ipj3.platformbackend.shared.domain.UserId;
import be.kdg.ipj3.platformbackend.shared.domain.exception.ActiveConversationExistsException;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

@Service
@Slf4j
@Transactional
public class ConversationService {
    private final ConversationRepository repository;
    private final AiChatbotApiCatalog chatbot;

    public ConversationService(ConversationRepository repository, AiChatbotApiCatalog chatbot) {
        this.repository = repository;
        this.chatbot = chatbot;
    }

    public Conversation find(ConversationId id){
        return repository.findById(id).orElseThrow(id::notFound);
    }

    public Conversation startNew(UserId userId){
        if (repository.hasActiveConversation(userId)) {
            throw new ActiveConversationExistsException(userId);
        }
        Conversation newConversation = Conversation.startNew(userId);
        log.info("New Conversation {} started for user {}", newConversation.getId().id(), userId.id());
        repository.save(newConversation);
        return newConversation;
    }

    public Conversation startNewWithoutUser(){
        Conversation newConversation = Conversation.startNewWithoutUser();
        log.info("New Conversation {} started without a user", newConversation.getId().id());
        repository.save(newConversation);
        return newConversation;
    }

    public Conversation sendMessage(ConversationId id, UUID sender, String text, LocalDateTime sendOn, String gameName, String currentPageUrl){
        Conversation conversation = find(id);
        conversation.sendMessage(sender,text,sendOn);
        repository.save(conversation);
        askChatbot(id,gameName,currentPageUrl);
        return conversation;
    }

    public void endConversation(ConversationId id){
        find(id);
        log.info("Conversation {} stopped", id.id());
        repository.remove(id);
    }

    private void askChatbot(ConversationId id, String gameName, String currentPageUrl){
        Conversation conversation = find(id);
        CompletableFuture.runAsync(() -> {
            try {
                ChatbotRequestDto dto = conversation.toChatbotRequest(
                        new ContextDto("14", gameName, currentPageUrl)
                );

                ChatbotAnswerDto answerDto = chatbot.askQuestion(dto);

                conversation.sendMessage(
                        UUID.fromString("00000000-0000-0000-0000-000000000001"),
                        answerDto.answer(),
                        LocalDateTime.now()
                );

                repository.save(conversation);

            } catch (Exception e) {
                log.warn("Chatbot async call failed", e);
            }
        });
    }
}
