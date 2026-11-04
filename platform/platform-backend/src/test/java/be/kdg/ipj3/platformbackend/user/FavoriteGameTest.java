package be.kdg.ipj3.platformbackend.user;

import be.kdg.ipj3.platformbackend.shared.domain.exception.NotFoundException;
import be.kdg.ipj3.platformbackend.game.domain.Game;
import be.kdg.ipj3.platformbackend.game.domain.GameId;
import be.kdg.ipj3.platformbackend.game.domain.Genre;
import be.kdg.ipj3.platformbackend.game.domain.repository.GameRepository;
import be.kdg.ipj3.platformbackend.shared.domain.UserId;
import be.kdg.ipj3.platformbackend.user.application.UserService;
import be.kdg.ipj3.platformbackend.user.domain.OwnedCopy;
import be.kdg.ipj3.platformbackend.user.domain.OwnedCopyId;
import be.kdg.ipj3.platformbackend.user.domain.PlatformUser;
import be.kdg.ipj3.platformbackend.user.domain.repository.PlatformUserRepository;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.Mockito.times;

@ExtendWith(MockitoExtension.class)
public class FavoriteGameTest {

    @Mock
    GameRepository gameRepository;
    @Mock
    PlatformUserRepository platformUserRepository;

    @InjectMocks
    UserService userService;

    @Nested
    class SuccessFlows {
        @Test
        void addFavoriteExistingGame_Should_return_Game() {
            // Arrange
            ArgumentCaptor<PlatformUser> userCaptor = ArgumentCaptor.forClass(PlatformUser.class);
            UUID selectedGameId = UUID.randomUUID();
            UUID currentUserId = UUID.randomUUID();
            UUID ownedCopyUUID= UUID.randomUUID();

            GameId gameId = new GameId(selectedGameId);
            UserId userId = new UserId(currentUserId);
            OwnedCopyId ownedCopyId = new OwnedCopyId(ownedCopyUUID);

            OwnedCopy ownedCopy = new OwnedCopy(ownedCopyId,gameId,false);
            PlatformUser returnValue = new PlatformUser(userId, "","",new ArrayList<>(),"","",0.0,new ArrayList<>(List.of(ownedCopy)));
            Genre puzzle = new Genre("Puzzle","Genre where you solve puzzles");
            Game game1 = new Game(gameId,"Tic Tac Toe", 2,"Game where you...", 20, "testimg.png", "testicon.png", "localhost:8080", puzzle, new ArrayList<>(),"localhost:8080/start/ai", "localhost:8080/start", new HashMap<>());

            Mockito.when(platformUserRepository.findUserById(userId))
                    .thenReturn(Optional.of(returnValue));

            Mockito.when(platformUserRepository.findOwnedGameByGameId(userId.id(),gameId.id()))
                            .thenReturn(Optional.of(ownedCopy));

            Mockito.when(gameRepository.findById(gameId.id()))
                    .thenReturn(Optional.of(game1));

            // Act
            Game result = userService.addFavoriteGame(userId,gameId);
            //Assert
            assertThat(result).isNotNull();
            assertThat(result.getName()).isEqualTo("Tic Tac Toe");

            Mockito.verify(platformUserRepository).save(userCaptor.capture());
            PlatformUser captured = userCaptor.getValue();

            assertThat(captured.getOwnedGames().size()).isEqualTo(1);
            assertThat(captured.getOwnedGames().getFirst().getGameId()).isEqualTo(result.getId());
            assertThat(captured.getOwnedGames().getFirst().isFavorite()).isEqualTo(true);

            Mockito.verifyNoMoreInteractions(gameRepository);
            Mockito.verifyNoMoreInteractions(platformUserRepository);
        }


        @Test
        void removeExistingGameFavorite_Should_not_throw_error() {
            // Arrange
            ArgumentCaptor<PlatformUser> userCaptor = ArgumentCaptor.forClass(PlatformUser.class);
            UUID selectedGameId = UUID.randomUUID();
            UUID currentUserId = UUID.randomUUID();
            UUID ownedCopyUUID= UUID.randomUUID();

            GameId gameId = new GameId(selectedGameId);
            UserId userId = new UserId(currentUserId);
            OwnedCopyId ownedCopyId = new OwnedCopyId(ownedCopyUUID);

            OwnedCopy ownedCopy = new OwnedCopy(ownedCopyId,gameId,false);
            PlatformUser returnValue = new PlatformUser(userId, "","",new ArrayList<>(),"","",0.0,new ArrayList<>(List.of(ownedCopy)));
            Genre puzzle = new Genre("Puzzle","Genre where you solve puzzles");
            Game game1 = new Game(gameId,"Tic Tac Toe", 2,"Game where you...", 20, "testimg.png", "testicon.png", "localhost:8080", puzzle, new ArrayList<>(), "localhost:8080/start/ai", "localhost:8080/start", new HashMap<>());

            Mockito.when(platformUserRepository.findUserById(userId))
                    .thenReturn(Optional.of(returnValue));

            Mockito.when(platformUserRepository.findOwnedGameByGameId(userId.id(),gameId.id()))
                    .thenReturn(Optional.of(ownedCopy));

            Mockito.when(gameRepository.findById(gameId.id()))
                    .thenReturn(Optional.of(game1));

            // Act
            userService.removeFavoriteGame(userId,gameId);
            //Assert
            Mockito.verify(platformUserRepository).save(userCaptor.capture());
            PlatformUser captured = userCaptor.getValue();

            assertThat(captured.getOwnedGames().size()).isEqualTo(1);
            assertThat(captured.getOwnedGames().getFirst().isFavorite()).isEqualTo(false);


            Mockito.verifyNoMoreInteractions(gameRepository);
            Mockito.verifyNoMoreInteractions(platformUserRepository);
        }
    }

    @Nested
    class ErrorFlows{
        @Test
        void addFavoriteNonExistantGame_shouldThrow_404() {
            // Arrange
            String uuid = "00000000-0000-0000-0000-000000000000";
            UUID userId  = UUID.randomUUID();
            GameId invalidGameId = new GameId(UUID.fromString(uuid));

            Mockito.when(gameRepository.findById(invalidGameId.id()))
                    .thenReturn(Optional.empty());

            // Act & Assert
            assertThatThrownBy(() -> userService.addFavoriteGame(new UserId(userId), invalidGameId))
                    .isInstanceOf(NotFoundException.class);

            Mockito.verify(gameRepository, times(1)).findById(invalidGameId.id());
            Mockito.verifyNoInteractions(platformUserRepository);
            Mockito.verifyNoMoreInteractions(gameRepository);
        }


        @Test
        void removeNonExistingGameFavorite_Should_throw_404() {
            // Arrange
            String uuid = "00000000-0000-0000-0000-000000000000";
            UUID userId  = UUID.randomUUID();
            GameId invalidGameId = new GameId(UUID.fromString(uuid));

            Mockito.when(gameRepository.findById(invalidGameId.id()))
                    .thenReturn(Optional.empty());

            // Act & Assert
            assertThatThrownBy(() -> userService.removeFavoriteGame(new UserId(userId), invalidGameId))
                    .isInstanceOf(NotFoundException.class);

            Mockito.verify(gameRepository, times(1)).findById(invalidGameId.id());
            Mockito.verifyNoInteractions(platformUserRepository);
            Mockito.verifyNoMoreInteractions(gameRepository);
        }
    }
}
