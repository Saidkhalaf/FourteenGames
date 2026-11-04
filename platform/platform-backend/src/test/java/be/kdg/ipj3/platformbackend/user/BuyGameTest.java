package be.kdg.ipj3.platformbackend.user;

import be.kdg.ipj3.platformbackend.analytics.infrastructure.AnalyticsMessagePublisher;
import be.kdg.ipj3.platformbackend.analytics.messages.PurchaseMadeMessage;
import be.kdg.ipj3.platformbackend.game.domain.Game;
import be.kdg.ipj3.platformbackend.game.domain.GameId;
import be.kdg.ipj3.platformbackend.game.domain.Genre;
import be.kdg.ipj3.platformbackend.game.domain.repository.GameRepository;
import be.kdg.ipj3.platformbackend.shared.domain.UserId;
import be.kdg.ipj3.platformbackend.shared.domain.exception.GameAlreadyOwnedException;
import be.kdg.ipj3.platformbackend.shared.domain.exception.InsufficientCreditsException;
import be.kdg.ipj3.platformbackend.user.application.UserService;
import be.kdg.ipj3.platformbackend.user.domain.OwnedCopy;
import be.kdg.ipj3.platformbackend.user.domain.OwnedCopyId;
import be.kdg.ipj3.platformbackend.user.domain.PlatformUser;
import be.kdg.ipj3.platformbackend.user.domain.repository.PlatformUserRepository;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;

import static org.assertj.core.api.AssertionsForClassTypes.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;

@ExtendWith(MockitoExtension.class)
public class BuyGameTest {
    @Mock
    PlatformUserRepository platformUserRepository;

    @Mock
    GameRepository gameRepository;

    @Mock
    AnalyticsMessagePublisher analyticsMessagePublisher;

    @InjectMocks
    UserService userService;

    @Nested
    class SuccessFlows {
        @Test
        void buyGame_shouldReturnOwnedCopyOfBoughtGame_whenUserHasSufficientCredits() {
            //Arrange
            UserId userId = new UserId(UUID.randomUUID());
            GameId gameId = new GameId(UUID.randomUUID());

            PlatformUser mockUser = new PlatformUser(userId, "userName1", "", new ArrayList<>(), "", "", 50.0, new ArrayList<>());
            Genre puzzle = new Genre("Puzzle", "Genre where you solve puzzles");
            Game game = new Game(gameId, "Tic Tac Toe", 2,"Game where you...", 20, "testimg.png", "testicon.png", "localhost:8080", puzzle, new ArrayList<>(), "localhost:8080/start/ai","localhost:8080/start", new HashMap<>());

            Mockito.when(platformUserRepository.findUserById(userId))
                    .thenReturn(Optional.of(mockUser));
            Mockito.when(gameRepository.findById(gameId.id()))
                    .thenReturn(Optional.of(game));

            //Act
            OwnedCopy ownedCopyResult = userService.buyGame(userId, gameId);

            //Assert
            assertThat(ownedCopyResult).isNotNull();
            assertThat(ownedCopyResult.getGameId().id()).isEqualTo(gameId.id());
            assertThat(mockUser.getOwnedGames().size()).isEqualTo(1);
            assertThat(mockUser.getCredits()).isEqualTo(30);
            Mockito.verify(analyticsMessagePublisher).publishPurchaseMadeMessage(any(PurchaseMadeMessage.class));
        }
    }

    @Nested
    class ErrorFlows {
        @Test
        void buyGame_shouldThrowInsufficientCreditsException_whenUserHasInsufficientCredits() {
            // Arrange
            UserId userId = new UserId(UUID.randomUUID());
            GameId gameId = new GameId(UUID.randomUUID());

            // User has only 10 credits
            PlatformUser mockUser = new PlatformUser(
                    userId,
                    "userName1",
                    "",
                    new ArrayList<>(),
                    "",
                    "",
                    10.0,
                    new ArrayList<>()
            );

            Genre puzzle = new Genre("Puzzle", "Genre where you solve puzzles");
            Game game = new Game(gameId, "Tic Tac Toe", 2,"Game where you...", 20, "testimg.png", "testicon.png", "localhost:8080", puzzle, new ArrayList<>(), "localhost:8080/start/ai","localhost:8080/start", new HashMap<>());

            Mockito.when(platformUserRepository.findUserById(userId))
                    .thenReturn(Optional.of(mockUser));
            Mockito.when(gameRepository.findById(gameId.id()))
                    .thenReturn(Optional.of(game));

            // Act + Assert
            assertThatThrownBy(() -> userService.buyGame(userId, gameId))
                    .isInstanceOf(InsufficientCreditsException.class)
                    .hasMessageContaining("Not enough credits");
            assertThat(mockUser.getOwnedGames().size()).isEqualTo(0);
            assertThat(mockUser.getCredits()).isEqualTo(10);
            Mockito.verify(analyticsMessagePublisher, never()).publishPurchaseMadeMessage(any(PurchaseMadeMessage.class));
        }

        @Test
        void buyGame_shouldThrowException_whenUserAlreadyOwnsGame() {
            // Arrange
            UserId userId = new UserId(UUID.randomUUID());
            GameId gameId = new GameId(UUID.randomUUID());

            OwnedCopy existingOwnedCopy = new OwnedCopy(new OwnedCopyId(UUID.randomUUID()),gameId,false);

            PlatformUser mockUser = new PlatformUser(
                    userId,
                    "userName1",
                    "",
                    new ArrayList<>(),
                    "",
                    "",
                    50.0,
                    new ArrayList<>(List.of(existingOwnedCopy))
            );

            // Act + Assert
            assertThatThrownBy(() -> mockUser.buyGame(gameId, 20.0))
                    .isInstanceOf(GameAlreadyOwnedException.class)
                    .hasMessageContaining("already owned");
            assertThat(mockUser.getCredits()).isEqualTo(50.0);
            assertThat(mockUser.getOwnedGames().size()).isEqualTo(1);
            Mockito.verify(analyticsMessagePublisher, never()).publishPurchaseMadeMessage(any(PurchaseMadeMessage.class));
        }
    }
}
