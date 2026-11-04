package be.kdg.ipj3.platformbackend.user;

import be.kdg.ipj3.platformbackend.analytics.infrastructure.AnalyticsMessagePublisher;
import be.kdg.ipj3.platformbackend.analytics.messages.FriendAddedMessage;
import be.kdg.ipj3.platformbackend.shared.domain.UserId;
import be.kdg.ipj3.platformbackend.shared.domain.exception.NotFoundException;
import be.kdg.ipj3.platformbackend.user.application.FriendService;
import be.kdg.ipj3.platformbackend.user.domain.repository.PlatformUserFriendRepository;
import be.kdg.ipj3.platformbackend.user.domain.repository.PlatformUserRepository;
import be.kdg.ipj3.platformbackend.user.domain.PlatformUser;
import be.kdg.ipj3.platformbackend.user.domain.PlatformFriendRequest;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.UUID;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.hibernate.validator.internal.util.Contracts.assertTrue;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class FriendRequestTest {

    @Mock
    PlatformUserRepository platformUserRepository;

    @Mock
    PlatformUserFriendRepository platformUserFriendRepository;

    @Mock
    AnalyticsMessagePublisher analyticsMessagePublisher;

    @InjectMocks
    FriendService friendService;

    @Nested
    class SuccesFlows {

        @Test
        void addFriendToFriendList_should_addNewFriend_whenNoExistingRequest() {
            //Arrange
            UserId userId = new UserId(UUID.fromString("11111111-1111-1111-1111-111111111111"));
            String userName1 = "TestUser1";
            UserId friend1Id = new UserId(UUID.fromString("11111111-1111-1111-1111-111111111112"));
            String userName2 = "TestUser2";

            PlatformUser user = new PlatformUser(userId, userName1, "", new ArrayList<>(),"","",0.0,new ArrayList<>());
            PlatformUser friend = new PlatformUser(friend1Id, userName2, "", new ArrayList<>(),"","",0.0,new ArrayList<>());
            Mockito.when(platformUserRepository.findUserById(userId)).thenReturn(Optional.of(user));
            Mockito.when(platformUserRepository.findUserById(friend1Id)).thenReturn(Optional.of(friend));
            Mockito.when(platformUserFriendRepository.findFriendRequestBetween(friend1Id.id(), userId))
                    .thenThrow(new NotFoundException("No request found"));

            //Act
            var friendRelation = friendService.addFriendRequest(userId, friend1Id);
            //Assert
            Mockito.verify(platformUserRepository, times(1)).findUserById(userId);
            Mockito.verify(platformUserFriendRepository, times(1)).findFriendRequestBetween(friend1Id.id(), userId);
            Mockito.verify(platformUserFriendRepository, times(1)).save(friendRelation);
            assertThat(friendRelation.getSender() == userId);
            assertThat(friendRelation.getReceiver() == friend1Id);
        }

        @Test
        void addFriendToFriendList_should_acceptExistingRequest_whenRequestExists() {
            // Arrange
            UserId userId = new UserId(UUID.fromString("11111111-1111-1111-1111-111111111111"));
            UserId friendId = new UserId(UUID.fromString("11111111-1111-1111-1111-111111111112"));
            PlatformUser user = new PlatformUser(userId, "TestUser1", "", new ArrayList<>(),"","",0.0,new ArrayList<>());
            PlatformUser friend = new PlatformUser(friendId, "TestUser2", "", new ArrayList<>(),"","",0.0,new ArrayList<>());
            PlatformFriendRequest existingRequest = new PlatformFriendRequest(
                    UUID.randomUUID(), friend.getUserId(), user.getUserId(), false, LocalDateTime.now(), null
            );
            Mockito.when(platformUserRepository.findUserById(userId)).thenReturn(Optional.of(user));
            Mockito.when(platformUserRepository.findUserById(friendId)).thenReturn(Optional.of(friend));
            Mockito.when(platformUserFriendRepository.findFriendRequestBetween(friendId.id(), userId))
                    .thenReturn(existingRequest);

            // Act
            friendService.addFriendRequest(userId, friendId);

            // Assert
            Mockito.verify(platformUserFriendRepository, times(2)).findFriendRequestBetween(friendId.id(), userId);
            Mockito.verify(platformUserFriendRepository, never()).validateIfFriendRelationExists(userId, friendId);
        }

        @Test
        void removeFriendFromFriendlist_should_removeplatform_user_friend() {
            //Arrange
            UserId userId = new UserId(UUID.fromString("11111111-1111-1111-1111-111111111111"));
            UserId friend1Id = new UserId(UUID.fromString("11111111-1111-1111-1111-111111111112"));
            //Act
            friendService.removeFriendFromFriendList(userId, friend1Id);
            //Assert
            Mockito.verify(platformUserFriendRepository, times(1)).remove(userId, friend1Id, true);
        }

        @Test
        void acceptFriendRequest_should_update_the_state_of_an_existing_friend_request() {
            // Arrange
            UserId userId = new UserId(UUID.fromString("11111111-1111-1111-1111-111111111111"));
            UUID friendId = UUID.fromString("11111111-1111-1111-1111-111111111112");
            PlatformUser user = new PlatformUser(userId, "TestUser1", "", new ArrayList<>(),"","",0.0,new ArrayList<>());
            PlatformUser friend = new PlatformUser(new UserId(friendId), "TestUser2", "", new ArrayList<>(),"","",0.0,new ArrayList<>());
            LocalDateTime now = LocalDateTime.now();

            PlatformFriendRequest existingRequest = new PlatformFriendRequest(
                    UUID.randomUUID(),
                    user.getUserId(),
                    friend.getUserId(),
                    false,
                    now,
                    null
            );

            Mockito.when(platformUserFriendRepository.findFriendRequestBetween(friendId, userId))
                    .thenReturn(existingRequest);

            // Act
            friendService.acceptFriendRequest(userId, friendId);

            // Assert
            Mockito.verify(platformUserFriendRepository, times(1)).findFriendRequestBetween(friendId, userId);
            assertTrue(existingRequest.isConfirmed(), "The friend request should be marked as accepted");
            Mockito.verify(platformUserFriendRepository, times(1)).save(existingRequest);
            Mockito.verify(analyticsMessagePublisher).publishFriendAddedMessage(any(FriendAddedMessage.class));
        }

    }

}
