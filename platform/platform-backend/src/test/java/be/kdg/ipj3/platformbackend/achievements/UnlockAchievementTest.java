package be.kdg.ipj3.platformbackend.achievements;

import be.kdg.ipj3.platformbackend.game.infrastructure.rabbitMQ.messages.AchievementMessageDto;
import be.kdg.ipj3.platformbackend.achievement.domain.AchievementId;
import be.kdg.ipj3.platformbackend.shared.domain.UserId;
import be.kdg.ipj3.platformbackend.user.application.UserService;
import be.kdg.ipj3.platformbackend.user.domain.PlatformUser;
import be.kdg.ipj3.platformbackend.user.domain.repository.PlatformUserRepository;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@ExtendWith(MockitoExtension.class)
public class UnlockAchievementTest {

    @Mock
    PlatformUserRepository platformUserRepository;

    @InjectMocks
    UserService userService;

    @Nested
    class SuccessFlows {
        @Test
        void unlockAchievement_adds_new_achievement_to_user() {
            //Arrange
            UserId userId = new UserId(UUID.fromString("11111111-1111-1111-1111-111111111111"));
            String userName = "TestUser";

            PlatformUser user = new PlatformUser(
                    userId,
                    userName,
                    "Lives for the test, test is life",
                    new ArrayList<>(),
                    "",
                    "",
                    0.0,
                    new ArrayList<>()
            );

            AchievementId achievementId = new AchievementId(UUID.fromString("11111111-1111-1111-1111-111111111123"));

            AchievementMessageDto dto = new AchievementMessageDto(userId.id(), achievementId.id());

            Mockito.when(platformUserRepository.findUserById(userId)).thenReturn(Optional.of(user));
            //Act
            user = userService.unlockAchievement(dto);

            //Assert
            assertThat(user.getAchievements().size()).isEqualTo(1);
            assertThat(user.getAchievements().getFirst().id().achievementId()).isEqualTo(achievementId);
        }
    }

    @Nested
    class ErrorFlows {
        @Test
        void unlockAchievement_adds_no_achievement_already_earned_to_user() {
            //Arrange
            UserId userId = new UserId(UUID.fromString("11111111-1111-1111-1111-111111111111"));
            String userName = "TestUser";

            PlatformUser user = new PlatformUser(
                    userId,
                    userName,
                    "Lives for the test, test is life",
                    new ArrayList<>(),
                    "",
                    "",
                    0.0,
                    new ArrayList<>()
            );

            AchievementId achievementId = new AchievementId(UUID.fromString("11111111-1111-1111-1111-111111111123"));

            AchievementMessageDto dto = new AchievementMessageDto(userId.id(), achievementId.id());

            Mockito.when(platformUserRepository.findUserById(userId)).thenReturn(Optional.of(user));
            //Act
            PlatformUser postTestUser1 = userService.unlockAchievement(dto);
            PlatformUser postTestUser2 = userService.unlockAchievement(dto);

            //Assert
            assertThat(postTestUser1.getAchievements().size()).isEqualTo(postTestUser2.getAchievements().size());
        }

    }
}
