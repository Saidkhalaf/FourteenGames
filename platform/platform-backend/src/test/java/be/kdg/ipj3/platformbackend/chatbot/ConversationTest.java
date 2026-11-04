package be.kdg.ipj3.platformbackend.chatbot;

import be.kdg.ipj3.platformbackend.chatbot.application.ConversationService;
import be.kdg.ipj3.platformbackend.chatbot.domain.Conversation;
import be.kdg.ipj3.platformbackend.chatbot.domain.ConversationId;
import be.kdg.ipj3.platformbackend.chatbot.domain.repository.ConversationRepository;
import be.kdg.ipj3.platformbackend.shared.domain.UserId;
import be.kdg.ipj3.platformbackend.shared.domain.exception.ActiveConversationExistsException;
import be.kdg.ipj3.platformbackend.shared.domain.exception.NotFoundException;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;

@ExtendWith(MockitoExtension.class)
public class ConversationTest {

    @Mock
    ConversationRepository conversationRepository;

    @InjectMocks
    ConversationService conversationService;

    @Nested
    class SuccessFlows {
        @Test
        void startNew_should_create_new_conversation_when_no_active_conversations(){
            //Arrange
            UserId userId = new UserId(UUID.fromString("11111111-1111-1111-1111-111111111111"));
            //Act
            Conversation newConversation = conversationService.startNew(userId);
            //Assert
            assertThat(newConversation.getUser()).isEqualTo(userId);
            assertThat(newConversation.getMessages().size()).isEqualTo(0);
            Mockito.verify(conversationRepository,times(1)).save(newConversation);
        }

        @Test
        void startNewWithoutUser_should_create_new_conversation_when_no_active_conversations(){
            //Arrange
            //Act
            Conversation newConversation = conversationService.startNewWithoutUser();
            //Assert
            assertThat(newConversation.getUser()).isEqualTo(null);
            Mockito.verify(conversationRepository,times(1)).save(newConversation);
        }

        @Test
        void endConversation_should_remove_the_conversation(){
            //Arrange
            ConversationId conversationId = new ConversationId();
            UserId userId = new UserId(UUID.randomUUID());
            Conversation conversation = new Conversation(conversationId,userId,new ArrayList<>());

            Mockito.when(conversationRepository.findById(conversationId)).thenReturn(Optional.of(conversation));
            //Act
            conversationService.endConversation(conversationId);
            //Assert
            Mockito.verify(conversationRepository,times(1)).remove(conversationId);
        }

        @Test
        void sendMessage_should_add_message_and_save_conversation() {
            // Arrange
            ConversationId conversationId = new ConversationId();
            UserId userId = new UserId(UUID.randomUUID());
            Conversation conversation = Mockito.spy(new Conversation(conversationId, userId, new ArrayList<>()));

            UUID senderId = UUID.randomUUID();
            String text = "Hello!";
            LocalDateTime sendOn = LocalDateTime.now();
            String gameName = "Go";
            String currentPageUrl = "/game-page";

            Mockito.when(conversationRepository.findById(conversationId))
                    .thenReturn(Optional.of(conversation));

            // Act
            Conversation result = conversationService.sendMessage(
                    conversationId,
                    senderId,
                    text,
                    sendOn,
                    gameName,
                    currentPageUrl
            );

            // Assert
            Mockito.verify(conversation, times(1)).sendMessage(senderId, text, sendOn);
            Mockito.verify(conversationRepository, times(1)).save(conversation);
            assertThat(conversation.getId()).isEqualTo(result.getId());
            assertThat(result.getMessages().size()).isEqualTo(1);
        }
    }

    @Nested
    class ErrorFlows {
        @Test
        void find_should_throw_when_conversation_not_found() {
            // Arrange
            ConversationId conversationId = new ConversationId();
            Mockito.when(conversationRepository.findById(conversationId))
                    .thenReturn(Optional.empty());

            // Act & Assert
            assertThatThrownBy(() -> conversationService.find(conversationId))
                    .isInstanceOf(NotFoundException.class);
        }
        @Test
        void startNew_should_throw_ActiveConversationExistsException_with_active_conversations(){
            //Arrange
            UserId userId = new UserId(UUID.fromString("11111111-1111-1111-1111-111111111111"));
            //Act
            conversationService.startNew(userId);
            Mockito.when(conversationRepository.hasActiveConversation(userId)).thenReturn(true);
            //Assert
            assertThatThrownBy(() -> conversationService.startNew(userId)).isInstanceOf(ActiveConversationExistsException.class);
        }

    }
}
