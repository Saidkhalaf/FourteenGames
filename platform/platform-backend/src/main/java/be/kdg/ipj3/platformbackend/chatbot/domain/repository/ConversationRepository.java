package be.kdg.ipj3.platformbackend.chatbot.domain.repository;

import be.kdg.ipj3.platformbackend.chatbot.domain.Conversation;
import be.kdg.ipj3.platformbackend.chatbot.domain.ConversationId;
import be.kdg.ipj3.platformbackend.shared.domain.UserId;

import java.util.Optional;

public interface ConversationRepository {
    void save (Conversation conversation);
    Optional<Conversation> findById (ConversationId id);
    void remove(ConversationId id);
    boolean hasActiveConversation(UserId userId);
}
