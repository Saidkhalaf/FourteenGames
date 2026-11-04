package be.kdg.ipj3.platformbackend.user.domain;

import be.kdg.ipj3.platformbackend.shared.domain.UserId;
import lombok.Getter;
import java.time.LocalDateTime;
import java.util.UUID;

@Getter
public class PlatformFriendRequest {
    private final UUID id;
    private final UserId sender;
    private final UserId receiver;
    private boolean isConfirmed;
    private final LocalDateTime requestedAt;
    private LocalDateTime confirmedAt;

    public PlatformFriendRequest(UUID id, UserId sender, UserId receiver, Boolean isConfirmed, LocalDateTime requestedAt, LocalDateTime confirmedAt) {
        this.id = id;
        this.sender = sender;
        this.receiver = receiver;
        this.isConfirmed = isConfirmed;
        this.requestedAt = requestedAt;
        this.confirmedAt = confirmedAt;
    }

    public static PlatformFriendRequest fromDb(
            UUID id,
            UserId sender,
            UserId receiver,
            Boolean isConfirmed,
            LocalDateTime requestedAt,
            LocalDateTime confirmedAt
    ) {
        return new PlatformFriendRequest(id,sender, receiver, isConfirmed, requestedAt, confirmedAt);
    }

    public void accept() {
        this.isConfirmed = true;
        this.confirmedAt = LocalDateTime.now();
    }

    public UserId getRequestingUserId(UserId currentUserId) {
        UUID current = currentUserId.id();
        if (sender.id().equals(current)) {
            return receiver;
        }
        return sender;
    }

}
