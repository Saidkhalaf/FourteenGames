package be.kdg.ipj3.platformbackend.user.api.dtos;
import be.kdg.ipj3.platformbackend.user.domain.PlatformUser;
import java.util.List;
import java.util.stream.Collectors;

public record FriendRequestListDto(List<FriendDto> friends) {
    public static FriendRequestListDto from(final List<PlatformUser> friends) {
        List<FriendDto> friendDtos = friends
                .stream()
                .map(f -> new FriendDto(
                        f.getUserId().id(),
                        f.getUserName(),
                        f.getBiography()
                ))
                .collect(Collectors.toList());

        return new FriendRequestListDto(
                friendDtos
        );
    }
}