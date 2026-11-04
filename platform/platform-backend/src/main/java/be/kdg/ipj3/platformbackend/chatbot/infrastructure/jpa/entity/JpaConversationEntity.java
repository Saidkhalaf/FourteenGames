package be.kdg.ipj3.platformbackend.chatbot.infrastructure.jpa.entity;

import be.kdg.ipj3.platformbackend.chatbot.domain.ChatMessage;
import be.kdg.ipj3.platformbackend.chatbot.domain.Conversation;
import be.kdg.ipj3.platformbackend.chatbot.domain.ConversationId;
import be.kdg.ipj3.platformbackend.shared.domain.UserId;
import jakarta.persistence.*;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@Entity
@Table(name = "conversations")
public class JpaConversationEntity {
    @Id
    @Column
    private UUID id;

    @JoinColumn(name = "user_id")
    private UUID userId;

    @OneToMany(mappedBy = "conversation", orphanRemoval = true, cascade = CascadeType.ALL)
    private List<JpaChatMessageEntity> messages;

    public JpaConversationEntity() {
    }

    public static JpaConversationEntity fromDomain(Conversation domain) {
        JpaConversationEntity entity = new JpaConversationEntity();

        entity.id = domain.getId().id();

        entity.userId = domain.getUser() != null
                ? domain.getUser().id()
                : null;
        entity.messages = domain.getMessages().stream().map(msg -> JpaChatMessageEntity.fromDomain(msg,entity)).toList();

        return entity;
    }

    public Conversation toDomain(){
        List<ChatMessage> domainMessages = messages.stream().map(JpaChatMessageEntity::toDomain).collect(Collectors.toList());

        return new Conversation(
                new ConversationId(this.id),
                new UserId(this.userId),
                domainMessages
        );
    }
}
