package be.kdg.ipj3.platformbackend.analytics.messages;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

public record UserLoggedInMessage(
        String event_type,
        String timestamp,
        UUID user_id,
        UUID session_id,
        String login_method,
        String device_type
) implements EventMessage {
    public static UserLoggedInMessage of(UUID userId, UUID sessionId, String loginMethod, String deviceType) {
        return new UserLoggedInMessage(
                "user_logged_in",
                LocalDateTime.now().atOffset(ZoneOffset.UTC).format(DateTimeFormatter.ISO_INSTANT),
                userId,
                sessionId,
                loginMethod,
                deviceType
        );
    }
}
