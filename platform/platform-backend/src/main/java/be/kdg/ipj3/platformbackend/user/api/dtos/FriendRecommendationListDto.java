package be.kdg.ipj3.platformbackend.user.api.dtos;

import be.kdg.ipj3.platformbackend.user.domain.PlatformUser;

import java.util.List;
import java.util.stream.Collectors;

public record FriendRecommendationListDto(List<FriendDto> recommendations) {
    public static FriendRecommendationListDto from(final List<PlatformUser> recommendations) {
        List<FriendDto> friendDtos = recommendations
                .stream()
                .map(f -> new FriendDto(
                        f.getUserId().id(),
                        f.getUserName(),
                        f.getBiography()
                ))
                .collect(Collectors.toList());

        return new FriendRecommendationListDto(
                friendDtos
        );
    }
}