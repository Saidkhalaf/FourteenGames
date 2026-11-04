package be.kdg.gobackend.api;

import be.kdg.gobackend.TestHelpers;
import be.kdg.gobackend.game.domain.achievement.Achievement;
import be.kdg.gobackend.game.api.MatchController;
import be.kdg.gobackend.game.infrastructure.ai.dtos.AiRequestBodyDto;
import be.kdg.gobackend.game.application.GameStateService;
import be.kdg.gobackend.game.domain.GameState;
import be.kdg.gobackend.game.domain.Stone;
import be.kdg.gobackend.game.domain.PlayerId;
import be.kdg.gobackend.game.domain.repository.AiCatalog;
import be.kdg.gobackend.game.infrastructure.DbGameStateRepository;
import be.kdg.gobackend.game.infrastructure.ai.dtos.AiAnswerDto;
import be.kdg.gobackend.analytics.infrastructure.AnalyticsMessagePublisher;
import be.kdg.gobackend.game.infrastructure.jpa.JpaGameStateEntity;
import be.kdg.gobackend.game.infrastructure.jpa.JpaGameStateRepository;
import be.kdg.gobackend.registration.infrastructure.RabbitAchievementPublisher;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.security.oauth2.core.oidc.StandardClaimNames;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@WebMvcTest(MatchController.class)
@Import({GameStateService.class, DbGameStateRepository.class})
class MatchControllerTest {
    @Autowired
    private MatchController sut;

    @MockitoBean
    private JpaGameStateRepository jpaGameStateRepository;

    @MockitoBean
    private AiCatalog aiCatalog;

    @MockitoBean
    private RabbitAchievementPublisher achievementPublisher;

    @MockitoBean
    private AnalyticsMessagePublisher analyticsMessagePublisher;

    @Nested
    @DisplayName("Success Flows")
    class SuccessFlows {
        @Test
        @DisplayName("Should successfully let the Ai make a new move")
        void aiMakesAMove() {
            // Arrange
            final var playerUUID = UUID.randomUUID();
            final var playerId = new PlayerId(playerUUID);
            final var aiPlayerId = new PlayerId(UUID.randomUUID());
            final var match = new GameState(9, playerId, aiPlayerId, true, LocalDateTime.now());
            match.placeStone(0, 8, Stone.WHITE);

            when(jpaGameStateRepository.findById(match.getId().id()))
                    .thenReturn(Optional.of(JpaGameStateEntity.fromDomain(match)));
            final var aiAnswerDto = new AiAnswerDto(50, 5, 6);
            when(aiCatalog.askForMove(Mockito.any(AiRequestBodyDto.class)))
                    .thenReturn(Optional.of(aiAnswerDto));

            final var jwtToken = Jwt.withTokenValue("token")
                    .header("alg", "none")
                    .subject(playerUUID.toString())
                    .claim(StandardClaimNames.GIVEN_NAME, "test_user")
                    .claim(StandardClaimNames.FAMILY_NAME, "user")
                    .claim(StandardClaimNames.EMAIL, "test_user@test.be")
                    .build();

            // Act
            final var result = sut.letAiPlaceStone(jwtToken, match.getId().id());

            // Assert
            assertNotNull(result.getBody());
            assertEquals(match.getId().id(), result.getBody().id());
            assertEquals(9, result.getBody().size());
            assertEquals("B", result.getBody().board()[5][6]);
            TestHelpers.assertBoardCounts(result.getBody().board(), 79, 1, 1);

            Mockito.verify(jpaGameStateRepository).findById(Mockito.any(UUID.class));
            Mockito.verify(jpaGameStateRepository).save(Mockito.any(JpaGameStateEntity.class));
            Mockito.verify(aiCatalog).askForMove(Mockito.any(AiRequestBodyDto.class));
            Mockito.verify(achievementPublisher, never()).unlock(Mockito.any(UUID.class), Mockito.any(Achievement.class));
        }

        @Test
        @DisplayName("Passing after a pass should end game")
        void aiPassesAfterPlayerPassEndsGame() {
            // Arrange
            final var playerUUID = UUID.randomUUID();
            final var playerId = new PlayerId(playerUUID);
            final var aiPlayerId = new PlayerId(UUID.randomUUID());
            final var match = new GameState(9, playerId, aiPlayerId, true, LocalDateTime.now());
            match.passTurn();

            when(jpaGameStateRepository.findById(match.getId().id()))
                    .thenReturn(Optional.of(JpaGameStateEntity.fromDomain(match)));

            when(aiCatalog.askForMove(any()))
                    .thenReturn(Optional.of(new AiAnswerDto(81, -1, -1)));

            final var jwtToken = Jwt.withTokenValue("token")
                    .header("alg", "none")
                    .subject(playerUUID.toString())
                    .build();

            // Act
            final var result = sut.letAiPlaceStone(jwtToken, match.getId().id());

            // Assert
            assertNotNull(result.getBody());
            assertEquals(match.getId().id(), result.getBody().id());
            assertNotEquals(Stone.EMPTY, result.getBody().winner());
            Mockito.verify(aiCatalog).askForMove(any());
            Mockito.verify(jpaGameStateRepository).save(any());
        }

        @Nested
        class ErrorFlows {
            @Test
            @DisplayName("Should fail when the ai does not make a move")
            void aiMakesEmptyMove() {
                // Arrange
                UUID playerUUID = UUID.randomUUID();
                final var playerId = new PlayerId(playerUUID);
                final var aiPlayerId = new PlayerId(playerUUID);
                final var match = new GameState(9, playerId, aiPlayerId, true, LocalDateTime.now());
                match.placeStone(0, 8, Stone.BLACK);
                final var jpaEntity = JpaGameStateEntity.fromDomain(match);

                when(jpaGameStateRepository.findById(match.getId().id()))
                        .thenReturn(Optional.of(jpaEntity));
                when(aiCatalog.askForMove(Mockito.any(AiRequestBodyDto.class)))
                        .thenReturn(Optional.empty());

                final var jwtToken = Jwt.withTokenValue("token")
                        .header("alg", "none")
                        .subject(playerUUID.toString())
                        .claim(StandardClaimNames.GIVEN_NAME, "test_user")
                        .claim(StandardClaimNames.FAMILY_NAME, "user")
                        .claim(StandardClaimNames.EMAIL, "test_user@test.be")
                        .build();

                // Act & Assert
                assertThatThrownBy(() -> sut.letAiPlaceStone(jwtToken, match.getId().id()))
                        .isInstanceOf(IllegalStateException.class);

                Mockito.verify(jpaGameStateRepository).findById(Mockito.any(UUID.class));
                Mockito.verify(jpaGameStateRepository, never()).save(Mockito.any(JpaGameStateEntity.class));
                Mockito.verify(aiCatalog).askForMove(Mockito.any(AiRequestBodyDto.class));
                Mockito.verify(achievementPublisher, never()).unlock(Mockito.any(UUID.class), Mockito.any(Achievement.class));
            }
        }
    }
}