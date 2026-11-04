package be.kdg.ipj3.platformbackend.chatbot.api;

import be.kdg.ipj3.platformbackend.chatbot.api.dtos.ConversationDto;
import be.kdg.ipj3.platformbackend.chatbot.api.dtos.ReceivedChatMessageDto;
import be.kdg.ipj3.platformbackend.chatbot.application.ConversationService;
import be.kdg.ipj3.platformbackend.chatbot.domain.Conversation;
import be.kdg.ipj3.platformbackend.chatbot.domain.ConversationId;
import be.kdg.ipj3.platformbackend.shared.domain.UserId;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("/api/conversations")
public class ConversationController {
    private final ConversationService conversations;

    public ConversationController(ConversationService conversations) {
        this.conversations = conversations;
    }

    @GetMapping("/{id}")
    public ResponseEntity<ConversationDto> getConversation(@PathVariable final UUID id) {
        ConversationId conversationId = new ConversationId(id);
        Conversation conversation = conversations.find(conversationId);
        return ResponseEntity.ok(ConversationDto.from(conversation));
    }

    @PostMapping("/start")
    public ResponseEntity<ConversationDto> startConversationAsUser(@AuthenticationPrincipal Jwt token) {
        UserId userId = UserId.fromToken(token);
        Conversation conversation = conversations.startNew(userId);
        return ResponseEntity.ok(ConversationDto.from(conversation));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> endConversationAsUser(@AuthenticationPrincipal Jwt token, @PathVariable final UUID id) {
        ConversationId conversationId = new ConversationId(id);
        conversations.endConversation(conversationId);
        return ResponseEntity.ok("Conversation " + conversationId.id() + " successfully removed");
    }

    @PostMapping("/{id}/messages")
    public ResponseEntity<ConversationDto> sendMessageAsUser(@AuthenticationPrincipal Jwt token, @PathVariable final UUID id, @RequestBody ReceivedChatMessageDto receivedMessage) {
        UserId userId = UserId.fromToken(token);
        ConversationId conversationId = new ConversationId(id);
        Conversation conversation = conversations.sendMessage(conversationId, userId.id(), receivedMessage.text(), receivedMessage.sentTime(), receivedMessage.gameName(), receivedMessage.currentPageUrl());
        return ResponseEntity.ok(ConversationDto.from(conversation));
    }

    @PostMapping("/visitor/start")
    public ResponseEntity<ConversationDto> startConversationAsVisitor() {
        Conversation conversation = conversations.startNewWithoutUser();
        return ResponseEntity.ok(ConversationDto.from(conversation));
    }

    @DeleteMapping("/visitor/{id}")
    public ResponseEntity<String> endConversationAsVisitor(@PathVariable final UUID id) {
        ConversationId conversationId = new ConversationId(id);
        conversations.endConversation(conversationId);
        return ResponseEntity.ok("Conversation " + conversationId.id() + " successfully removed");
    }

    @PostMapping("/visitor/{id}/messages")
    public ResponseEntity<ConversationDto> sendMessageAsVisitor(@PathVariable final UUID id, @RequestBody ReceivedChatMessageDto receivedMessage) {
        ConversationId conversationId = new ConversationId(id);
        Conversation conversation = conversations.sendMessage(conversationId, null, receivedMessage.text(), receivedMessage.sentTime(), receivedMessage.gameName(), receivedMessage.currentPageUrl());
        return ResponseEntity.ok(ConversationDto.from(conversation));
    }
}
