package be.kdg.ipj3.platformbackend.analytics.messages;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;

public record UserRegisteredMessage(
        String event_type,
        String timestamp,
        String user_id,
        String registration_method
) implements EventMessage {
    public UserRegisteredMessage(String user_id, String registration_method) {
        this(
                "user_registered",
                LocalDateTime.now().atOffset(ZoneOffset.UTC).format(DateTimeFormatter.ISO_INSTANT),
                user_id,
                registration_method);
    }
}
