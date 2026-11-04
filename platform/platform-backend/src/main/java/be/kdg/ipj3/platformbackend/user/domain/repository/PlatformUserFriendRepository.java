package be.kdg.ipj3.platformbackend.user.domain.repository;

import be.kdg.ipj3.platformbackend.shared.domain.UserId;
import be.kdg.ipj3.platformbackend.user.domain.PlatformFriendRequest;

import java.util.List;
import java.util.UUID;


public interface PlatformUserFriendRepository {
    PlatformFriendRequest save(PlatformFriendRequest user);
    PlatformFriendRequest findFriendRequestBetween(UUID friendId, UserId userId);
    void remove(UserId userId, UserId friendId, boolean isConfirmed);
    List<PlatformFriendRequest> findAllFriendRequestsForUser(UserId userId);
    void validateIfFriendRelationExists(UserId userId, UserId friendId);
    List<UUID> getUniqueFriendIdsForUser(UUID userId);
    List<PlatformFriendRequest> findAllFriendsForUser(UserId id);
}
