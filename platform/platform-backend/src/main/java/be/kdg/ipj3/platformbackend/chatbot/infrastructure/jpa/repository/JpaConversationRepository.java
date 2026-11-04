package be.kdg.ipj3.platformbackend.chatbot.infrastructure.jpa.repository;

import be.kdg.ipj3.platformbackend.chatbot.infrastructure.jpa.entity.JpaConversationEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.UUID;

public interface JpaConversationRepository extends JpaRepository<JpaConversationEntity, UUID> {
    void removeById(UUID id);

    @Query("""
                SELECT COUNT(c) > 0
                FROM JpaConversationEntity c
                WHERE c.userId = :id
            """)
    boolean userHasActiveConversation(UUID id);

}
