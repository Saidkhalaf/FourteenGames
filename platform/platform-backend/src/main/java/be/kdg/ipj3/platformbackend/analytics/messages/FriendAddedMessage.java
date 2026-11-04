package be.kdg.ipj3.platformbackend.analytics.messages;

import be.kdg.ipj3.platformbackend.user.domain.PlatformFriendRequest;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

public record FriendAddedMessage(
        String event_type,
        String timestamp,
        UUID user_id,
        UUID friend_id,
        String connection_type
) implements EventMessage {
    public static FriendAddedMessage of(PlatformFriendRequest friendRequest) {
        return new FriendAddedMessage(
                "friend_added",
                LocalDateTime.now().atOffset(ZoneOffset.UTC).format(DateTimeFormatter.ISO_INSTANT),
                friendRequest.getSender().id(),
                friendRequest.getReceiver().id(),
                (friendRequest.isConfirmed()) ? "confirmed" : "pending"
        );
    }
}
