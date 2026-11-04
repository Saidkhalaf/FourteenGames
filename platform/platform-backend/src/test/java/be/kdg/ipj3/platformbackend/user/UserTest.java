package be.kdg.ipj3.platformbackend.user;

import be.kdg.ipj3.platformbackend.analytics.infrastructure.AnalyticsMessagePublisher;
import be.kdg.ipj3.platformbackend.analytics.messages.PaymentMadeMessage;
import be.kdg.ipj3.platformbackend.shared.domain.UserId;
import be.kdg.ipj3.platformbackend.user.api.dtos.UpdateUserProfileRequestDto;
import be.kdg.ipj3.platformbackend.user.application.UserService;
import be.kdg.ipj3.platformbackend.user.domain.repository.PlatformUserFriendRepository;
import be.kdg.ipj3.platformbackend.user.domain.repository.PlatformUserRepository;
import be.kdg.ipj3.platformbackend.user.domain.PlatformUser;
import org.hibernate.mapping.Any;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;

@ExtendWith(MockitoExtension.class)
public class UserTest {

    @Mock
    PlatformUserRepository platformUserRepository;

    @Mock
    AnalyticsMessagePublisher analyticsMessagePublisher;

    @InjectMocks
    UserService userService;

    @Nested
    class SuccesFlows {
        @Test
        void addUser_should_return_user_when_user_id_is_provided() {
            //Arrange
            UserId userId = new UserId(UUID.randomUUID());
            String userName1 = "TestUser1";
            PlatformUser mockUser = new PlatformUser(userId,userName1,"", new ArrayList<>(),"" ,"",0.0,new ArrayList<>());
            Mockito.when(platformUserRepository.createUser(any(PlatformUser.class)))
                    .thenReturn(mockUser);
            // Act
            PlatformUser result = userService.addUser(userId,userName1);
            // Assert
            assertThat(result).isNotNull();
            assertThat(result.getUserId()).isEqualTo(userId);
        }

        @Test
        void findOrCreateUserById_should_return_valid_user_for_provided_idandusername(){
            //Arrange
            UserId userId = new UserId(UUID.randomUUID());
            String userName1 = "TestUser1";
            PlatformUser mockUser = new PlatformUser(userId,userName1,"", new ArrayList<>(), "","",0.0,new ArrayList<>());
            Mockito.when(platformUserRepository.findUserById(userId))
                    .thenReturn(Optional.of(mockUser));
            //Act
            PlatformUser user = userService.findOrCreateUserById(userId, userName1);
            //Assert
            assertThat(user).isNotNull();
            assertThat(user.getUserId()).isEqualTo(userId);
            assertThat(user.getUserName()).isEqualTo(userName1);
        }

        @Test
        void findUserListByIdList_shouldreturn_ValidListOfUsers_ByProvided_Ids(){
            //Arrange
            UserId userId = new UserId(UUID.randomUUID());
            UserId testUser1Id = new UserId(UUID.randomUUID());
            UserId testUser2Id = new UserId(UUID.randomUUID());
            String userName1 = "TestUser1";
            PlatformUser mockUser = new PlatformUser(userId,userName1,"", new ArrayList<>(), "","",0.0,new ArrayList<>());
            PlatformUser testUser1 = new PlatformUser(testUser1Id, "test-user-1","",  new ArrayList<>(), "","",0.0,new ArrayList<>());
            PlatformUser testUser2 = new PlatformUser(testUser2Id, "test-user-2","", new ArrayList<>(),"","",0.0,new ArrayList<>());

            List<PlatformUser> list = List.of(mockUser,testUser1,testUser2);
            List<UserId> idList = List.of(userId, testUser1Id, testUser2Id);
            List<UUID> uuidList = List.of(userId.id(), testUser1Id.id(), testUser2Id.id());

            Mockito.when(platformUserRepository.findAllUsersByIds(uuidList))
                    .thenReturn(list);
            //Act
            List<PlatformUser> userList = userService.findUserListByIdList(idList);
            //Assert
            assertThat(userList).isNotNull();
            assertThat(userList.size()).isEqualTo(list.size());
            assertThat(userList.getFirst().getUserName()).isEqualTo(list.getFirst().getUserName());
            assertThat(userList.getFirst().getUserId().id()).isEqualTo(list.getFirst().getUserId().id());
        }


        @Nested
        class UpdateFlows {

            @Test
            void updateUsersBiograhpy_shouldreturn_ValidPlatformUser_WithCorrectUpdatedField() {
                //Arrange
                UserId userId = new UserId(UUID.randomUUID());
                String userName1 = "TestUser1";
                PlatformUser mockUser = new PlatformUser(userId, userName1, "",  new ArrayList<>(), "", "",0.0,new ArrayList<>());
                UpdateUserProfileRequestDto payload = new UpdateUserProfileRequestDto("test biography", "", "");
                Mockito.when(platformUserRepository.findUserById(userId))
                        .thenReturn(Optional.of(mockUser));
                Mockito.doNothing().when(platformUserRepository).save(any(PlatformUser.class));

                // Act
                PlatformUser result =
                        userService.updateUserProfile(userId, payload);

                // Assert
                assertEquals("test biography", result.getBiography());
                assertEquals("", result.getProfilePictureUrl());
                assertEquals("", result.getBannerUrl());
                Mockito.verify(platformUserRepository).findUserById(userId);
            }

            @Test
            void updateUsersProfilePicture_shouldreturn_ValidPlatformUser_WithCorrectUpdatedField() {
                //Arrange
                UserId userId = new UserId(UUID.randomUUID());
                String userName1 = "TestUser1";
                PlatformUser mockUser = new PlatformUser(userId, userName1, "", new ArrayList<>(), "", "",0.0,new ArrayList<>());
                UpdateUserProfileRequestDto payload = new UpdateUserProfileRequestDto("", "website.com/testimg.jpg", "");
                Mockito.when(platformUserRepository.findUserById(userId))
                        .thenReturn(Optional.of(mockUser));
                Mockito.doNothing().when(platformUserRepository).save(any(PlatformUser.class));

                // Act
                PlatformUser result =
                        userService.updateUserProfile(userId, payload);

                // Assert
                assertEquals("", result.getBiography());
                assertEquals("website.com/testimg.jpg", result.getProfilePictureUrl());
                assertEquals("", result.getBannerUrl());
                Mockito.verify(platformUserRepository).findUserById(userId);
            }

            @Test
            void updateUsersBannerImageUrl_shouldreturn_ValidPlatformUser_WithCorrectUpdatedField() {
                //Arrange
                UserId userId = new UserId(UUID.randomUUID());
                String userName1 = "TestUser1";
                PlatformUser mockUser = new PlatformUser(userId, userName1, "", new ArrayList<>(), "", "",0.0,new ArrayList<>());
                UpdateUserProfileRequestDto payload = new UpdateUserProfileRequestDto("", "", "website.com/testimg.jpg");
                Mockito.when(platformUserRepository.findUserById(userId))
                        .thenReturn(Optional.of(mockUser));
                Mockito.doNothing().when(platformUserRepository).save(any(PlatformUser.class));

                // Act
                PlatformUser result =
                        userService.updateUserProfile(userId, payload);

                // Assert
                assertEquals("", result.getBiography());
                assertEquals("", result.getProfilePictureUrl());
                assertEquals("website.com/testimg.jpg", result.getBannerUrl());
                Mockito.verify(platformUserRepository).findUserById(userId);
            }

            @Test
            void addCredit_shouldAddAmountGivenToUsersCredit(){
                //Arrange
                double amount = 50.0;
                UserId userId = new UserId(UUID.randomUUID());
                PlatformUser mockUser = new PlatformUser(userId, "userName1", "", new ArrayList<>(), "", "",0.0,new ArrayList<>());

                Mockito.when(platformUserRepository.findUserById(userId))
                        .thenReturn(Optional.of(mockUser));

                //Act
                PlatformUser result = userService.addCredit(userId, amount);

                //Assert
                assertEquals(50, result.getCredits());
                Mockito.verify(platformUserRepository).save(mockUser);
                Mockito.verify(analyticsMessagePublisher).publishPaymentMadeMessage(any(PaymentMadeMessage.class));
            }

        }


    }

}
