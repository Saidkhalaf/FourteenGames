package be.kdg.ipj3.platformbackend.analytics.messages;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

public record SystemErrorMessage(
        String event_type,
        String timestamp,
        UUID user_id,
        String error_code,
        String error_message,
        String affected_feature,
        String severity
) implements EventMessage {
    public SystemErrorMessage(String error_code, String error_message, String severity, UUID user_id, String affected_feature) {
        this(
                "system_error",
                LocalDateTime.now().atOffset(ZoneOffset.UTC).format(DateTimeFormatter.ISO_INSTANT),
                user_id,
                error_code,
                error_message,
                affected_feature,
                severity
        );
    }
}
