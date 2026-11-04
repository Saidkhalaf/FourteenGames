package be.kdg.ipj3.platformbackend.user.api.dtos;
import be.kdg.ipj3.platformbackend.user.domain.PlatformUser;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public record FriendListDto(UUID id, List<FriendDto> friends) {
    public static FriendListDto from(final PlatformUser user, List<PlatformUser> friends) {
        List<FriendDto> friendDtos = friends.stream()
                .map(f -> new FriendDto(
                        f.getUserId().id(),
                        f.getUserName(),
                        f.getBiography()

                ))
                .collect(Collectors.toList());

        return new FriendListDto(
                user.getUserId().id(),
                friendDtos
        );
    }
}