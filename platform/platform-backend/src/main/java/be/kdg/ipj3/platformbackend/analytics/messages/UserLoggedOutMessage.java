package be.kdg.ipj3.platformbackend.analytics.messages;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

public record UserLoggedOutMessage(
        String event_type,
        String timestamp,
        UUID user_id,
        UUID session_id,
        int sessionDurationSeconds
) implements EventMessage {
    public static UserLoggedOutMessage of(UUID userId, UUID sessionId, int duration) {
        return new UserLoggedOutMessage(
                "user_logged_out",
                LocalDateTime.now().atOffset(ZoneOffset.UTC).format(DateTimeFormatter.ISO_INSTANT),
                userId,
                sessionId,
                duration
        );
    }
}