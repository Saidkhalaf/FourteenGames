package be.kdg.ipj3.platformbackend.user.api.dtos;

import be.kdg.ipj3.platformbackend.user.domain.PlatformUser;
import java.util.UUID;

public record FriendDto(UUID userId, String userName, String biography) {
    public static FriendDto from(final PlatformUser friendUser) {
        return new FriendDto(
                friendUser.getUserId().id(),
                friendUser.getUserName(),
                friendUser.getBiography()
        );
    }
}