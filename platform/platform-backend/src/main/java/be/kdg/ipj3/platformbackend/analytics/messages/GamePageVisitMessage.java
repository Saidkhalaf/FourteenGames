package be.kdg.ipj3.platformbackend.analytics.messages;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

public record GamePageVisitMessage(
        String event_type,
        String timestamp,
        UUID user_id,
        UUID game_id,
        UUID game_name,
        String referrer,
        String session_id
) implements EventMessage {
    public GamePageVisitMessage(UUID user_id, UUID game_id, UUID game_name, String referrer, String session_id) {
        this(
                "gamePage_visit",
                LocalDateTime.now().atOffset(ZoneOffset.UTC).format(DateTimeFormatter.ISO_INSTANT),
                user_id,
                game_id,
                game_name,
                referrer,
                session_id
        );
    }
}
