package be.kdg.ipj3.platformbackend.chatbot.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;
import java.util.UUID;

@Slf4j
@AllArgsConstructor
@Getter
public class ChatMessage {
    private final ChatMessageId id;
    private final UUID sender;
    private final String text;
    private final LocalDateTime sentTme;
}
