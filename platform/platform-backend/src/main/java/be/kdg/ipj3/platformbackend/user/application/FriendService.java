package be.kdg.ipj3.platformbackend.user.application;

import be.kdg.ipj3.platformbackend.analytics.infrastructure.AnalyticsMessagePublisher;
import be.kdg.ipj3.platformbackend.analytics.messages.FriendAddedMessage;
import be.kdg.ipj3.platformbackend.shared.domain.UserId;
import be.kdg.ipj3.platformbackend.shared.domain.exception.NotFoundException;
import be.kdg.ipj3.platformbackend.user.domain.repository.PlatformUserFriendRepository;
import be.kdg.ipj3.platformbackend.user.domain.repository.PlatformUserRepository;
import be.kdg.ipj3.platformbackend.user.domain.PlatformUser;
import be.kdg.ipj3.platformbackend.user.domain.PlatformFriendRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Slf4j
@Transactional
@RequiredArgsConstructor
public class FriendService {

    private final PlatformUserRepository platformUserRepository;
    private final PlatformUserFriendRepository platformUserFriendRepository;
    private final AnalyticsMessagePublisher analyticsMessagePublisher;

    public List<PlatformUser> findAllFriendsOfUserWithId(UserId userId) {
        List<PlatformFriendRequest> friendRequests = platformUserFriendRepository.findAllFriendsForUser(userId);

        List<UUID> friendIds = friendRequests.stream()
                .map(fr -> !fr.getSender().id().equals(userId.id())
                        ? fr.getSender().id()
                        : fr.getReceiver().id())
                .collect(Collectors.toList());

        return platformUserRepository.findByIdIn(friendIds);
    }

    public PlatformFriendRequest addFriendRequest(UserId userId, UserId friendId) {
        log.info("Adding friend {} to user {}", friendId, userId);
        PlatformUser user = platformUserRepository.findUserById(userId).orElseThrow(userId::notFound);
        PlatformUser friend = platformUserRepository.findUserById(friendId).orElseThrow(userId::notFound);

        PlatformFriendRequest friendRelation = new PlatformFriendRequest(
                UUID.randomUUID(),
                user.getUserId(),
                friend.getUserId(),
                false,
                LocalDateTime.now(),
                null
        );
        try {
            platformUserFriendRepository.findFriendRequestBetween(friend.getUserId().id(), user.getUserId());
            acceptFriendRequest(userId, friendId.id());
            return null;
        } catch (NotFoundException e) {
            log.info("No existing request between {} and {}", userId, friendId);
        }
        platformUserFriendRepository.validateIfFriendRelationExists(userId, friendId);
        platformUserFriendRepository.save(friendRelation);
        return friendRelation;
    }

    public void removeFriendFromFriendList(UserId userId, UserId friendId) {
        log.info("Removing friend {} from user {}", userId, friendId);
        platformUserFriendRepository.remove(userId, friendId, true);
    }

    public PlatformFriendRequest acceptFriendRequest(UserId userId, UUID friendId) {
        log.info("Accepting friend request between {} to user {}", friendId, userId);
        PlatformFriendRequest friendRequest = platformUserFriendRepository.findFriendRequestBetween(friendId, userId);
        friendRequest.accept();
        analyticsMessagePublisher.publishFriendAddedMessage(FriendAddedMessage.of(friendRequest));
        return platformUserFriendRepository.save(friendRequest);
    }

    public void denyFriendRequest(UserId userId, UUID friendId) {
        log.info("Denying friend request between {} to user {}", friendId, userId);
        platformUserFriendRepository.remove(userId, new UserId(friendId), false);
    }

    public List<PlatformFriendRequest> findFriendRequestsForUser(UserId userId) {
        log.info("Finding friend requests for user {}", userId);
        return platformUserFriendRepository.findAllFriendRequestsForUser(userId);
    }

    public List<PlatformUser> getFriendRecommendations(UUID id, String nameQuery) {
        log.info("Finding friend recommendations for user");
        int size = 10;
        List<UUID> friendIds = platformUserFriendRepository.getUniqueFriendIdsForUser(id);
        friendIds.add(id);
        List<PlatformUser> recommendations = new ArrayList<>();
        if (nameQuery != null && !nameQuery.isEmpty()) {
            recommendations.addAll(platformUserRepository.findRecommendationsListOfSizeWithNameQuery(friendIds, nameQuery, size));
        }
        if (size > recommendations.size()) {
            recommendations.forEach(recommendation -> friendIds.add(recommendation.getUserId().id()));
            recommendations.addAll(platformUserRepository.findRecommendationsListOfSize(friendIds, size - recommendations.size()));
        }
        return recommendations;
    }
}
