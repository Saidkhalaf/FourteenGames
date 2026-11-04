package be.kdg.ipj3.platformbackend.user.api.dtos;
import be.kdg.ipj3.platformbackend.user.domain.PlatformUser;

import java.util.UUID;

public record MinimalPlatformUserDto(UUID id, String userName, String biography,  String profilePictureUrl) {
    public static MinimalPlatformUserDto from(final PlatformUser user){
        return new MinimalPlatformUserDto(
                user.getUserId().id(),
                user.getUserName(),
                user.getBiography(),
                user.getProfilePictureUrl()
        );
    }
}
