package be.kdg.ipj3.platformbackend.chatbot.infrastructure.jpa.entity;

import be.kdg.ipj3.platformbackend.chatbot.domain.ChatMessage;
import be.kdg.ipj3.platformbackend.chatbot.domain.ChatMessageId;
import jakarta.persistence.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.parameters.P;

import java.time.LocalDateTime;
import java.util.UUID;

@Slf4j
@Entity
@Table(name = "messages")
public class JpaChatMessageEntity {
    @Id
    @Column
    private UUID id;

    @Column
    private UUID sender;

    @Column
    private String text;

    @Column
    private LocalDateTime sentTime;

    @ManyToOne
    @JoinColumn(name = "conversation_id")
    private JpaConversationEntity conversation;

    public JpaChatMessageEntity() {
    }

    public static JpaChatMessageEntity fromDomain(ChatMessage domain, JpaConversationEntity jpaConversation){
        JpaChatMessageEntity entity = new JpaChatMessageEntity();

        entity.id = domain.getId().id();
        entity.sender = domain.getSender();
        entity.text = domain.getText();
        entity.sentTime = domain.getSentTme();
        entity.conversation = jpaConversation;

        return entity;
    }

    public ChatMessage toDomain(){
        return new ChatMessage(
                new ChatMessageId(this.id),
                this.sender,
                this.text,
                this.sentTime
        );
    }
}
