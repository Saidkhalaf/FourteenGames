package be.kdg.ipj3.platformbackend.user.api.dtos;
import be.kdg.ipj3.platformbackend.achievement.api.UserAchievementDto;
import be.kdg.ipj3.platformbackend.user.domain.PlatformUser;

import java.util.List;
import java.util.UUID;

public record PlatformUserDto(UUID id, String userName, String biography, List<UserAchievementDto> achievements, String profilePictureUrl, String bannerUrl,
                              double credits, List<OwnedCopyDto> ownedCopies) {
    public static PlatformUserDto from(final PlatformUser user){
        return new PlatformUserDto(
                user.getUserId().id(),
                user.getUserName(),
                user.getBiography(),
                user.getAchievements().stream().map(UserAchievementDto::from).toList(),
                user.getProfilePictureUrl(),
                user.getBannerUrl(),
                user.getCredits(),
                user.getOwnedGames().stream().map(OwnedCopyDto::from).toList()
        );
    }
}
