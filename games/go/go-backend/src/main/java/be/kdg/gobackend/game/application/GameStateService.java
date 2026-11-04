package be.kdg.gobackend.game.application;

import be.kdg.gobackend.analytics.infrastructure.AnalyticsMessagePublisher;
import be.kdg.gobackend.analytics.infrastructure.messages.GameEndedMessage;
import be.kdg.gobackend.analytics.infrastructure.messages.GameStartedMessage;
import be.kdg.gobackend.analytics.infrastructure.messages.SessionStartedMessage;
import be.kdg.gobackend.analytics.infrastructure.messages.WinnerDeclaredMessage;
import be.kdg.gobackend.game.domain.GameState;
import be.kdg.gobackend.game.domain.GameStateId;
import be.kdg.gobackend.game.domain.PlayerId;
import be.kdg.gobackend.game.domain.Stone;
import be.kdg.gobackend.game.domain.achievement.Achievement;
import be.kdg.gobackend.game.domain.repository.AiCatalog;
import be.kdg.gobackend.game.domain.repository.GameStateRepository;
import be.kdg.gobackend.game.infrastructure.ai.dtos.AiEndGameDto;
import be.kdg.gobackend.game.infrastructure.ai.dtos.AiRequestBodyDto;
import be.kdg.gobackend.registration.infrastructure.RabbitAchievementPublisher;
import be.kdg.gobackend.shared.exception.NotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@Transactional
@Slf4j
@RequiredArgsConstructor
public class GameStateService {

    private final GameStateRepository gameStateRepository;
    private final AiCatalog aiCatalog;
    private final RabbitAchievementPublisher achievementPublisher;
    private final AnalyticsMessagePublisher analyticsMessagePublisher;

    public GameState getState(GameStateId stateId) {
        log.info("getting the state with id {}", stateId);
        return gameStateRepository.get(stateId).orElseThrow(stateId::notFound);
    }

    public GameState startAiGame(PlayerId playerId, int size) {
        log.info("player {} starting a game of size {}", playerId, size);
        final var notFinishedAiGame = gameStateRepository.getOngoingAiGameForPlayer(playerId);
        notFinishedAiGame.ifPresent(gameStateRepository::removeGame);
        final var gameState = new GameState(size, playerId,new PlayerId(UUID.randomUUID()), true, LocalDateTime.now());
        gameStateRepository.save(gameState);
        achievementPublisher.unlock(playerId.id(), Achievement.LETS_GO);
        if (size == 19) achievementPublisher.unlock(playerId.id(), Achievement.GO_BIG_OR_GO_HOME);
        analyticsMessagePublisher.publishGameStartedMessage(GameStartedMessage.of(gameState));
        analyticsMessagePublisher.publishSessionStartedMessage(SessionStartedMessage.of(gameState));
        return gameState;
    }

    public GameState startGame(PlayerId player1Id, PlayerId player2Id, int size) {
        log.info("player {} started a games vs player {} of size {}", player1Id.id(), player2Id.id(), size);
        final var gameState = new GameState(size, player1Id, player2Id, false, LocalDateTime.now());
        gameStateRepository.save(gameState);
        achievementPublisher.unlock(player1Id.id(), Achievement.LETS_GO);
        achievementPublisher.unlock(player2Id.id(), Achievement.LETS_GO);
        if(size == 19) {
            achievementPublisher.unlock(player1Id.id(), Achievement.GO_BIG_OR_GO_HOME);
            achievementPublisher.unlock(player2Id.id(), Achievement.GO_BIG_OR_GO_HOME);
        }
        analyticsMessagePublisher.publishGameStartedMessage(GameStartedMessage.of(gameState));
        analyticsMessagePublisher.publishSessionStartedMessage(SessionStartedMessage.of(gameState));
        return gameState;
    }

    public GameState placeStone(GameStateId stateId, int x, int y, PlayerId playerId) {
        log.info("player in match {} placing a stone at {}, {}", stateId, x, y);
        final var gameState = gameStateRepository.get(stateId).orElseThrow(stateId::notFound);
        gameState.verifyPlayer(playerId, gameState.isAiGame());
        final Stone toPlaceStone;
        if(gameState.isPlayer1AtTurn()){
            toPlaceStone = Stone.WHITE;
        }else{
            toPlaceStone = Stone.BLACK;
        }
        gameState.placeStone(x, y, toPlaceStone);
        gameState.switchPlayerAtTurn();
        gameStateRepository.save(gameState);
        return gameState;
    }

    public GameState letAiPlaceStone(GameStateId stateId, PlayerId playerId) {
        log.info("ai placing a stone in match {}", stateId);
        final var gameState = gameStateRepository.get(stateId).orElseThrow(stateId::notFound);
        final var aiResponse = aiCatalog.askForMove(AiRequestBodyDto.from(gameState))
                                        .orElseThrow(() -> new IllegalStateException("Ai could not make a move"));
        log.info("ai chose row {}, col {}, move {}", aiResponse.row(), aiResponse.col(), aiResponse.best_move());
        if (aiResponse.col() == -1 && aiResponse.row() == -1) {
            gameState.passTurn();
            if (!gameState.getWinner().equals(Stone.EMPTY))
                handleEndGame(gameState);
        } else {
            gameState.placeStone(aiResponse.row(), aiResponse.col(), Stone.BLACK);
            gameState.switchPlayerAtTurn();
        }
        gameStateRepository.save(gameState);
        return gameState;
    }

    public GameState passTurn(GameStateId stateId, PlayerId playerId) {
        log.info("passing turn to player in match {}", stateId);
        final var gameState = gameStateRepository.get(stateId).orElseThrow(stateId::notFound);
        gameState.verifyPlayer(playerId, gameState.isAiGame());
        gameState.passTurn();
        if (gameState.getWinner() != Stone.EMPTY) {
            analyticsMessagePublisher.publishGameEndedMessage(GameEndedMessage.of(gameState));
            analyticsMessagePublisher.publishWinnerDeclaredMessage(WinnerDeclaredMessage.of(gameState));
        }
        gameStateRepository.save(gameState);
        achievementPublisher.unlock(playerId.id(), Achievement.GO_AHEAD);
        if (!gameState.getWinner().equals(Stone.EMPTY))
            handleEndGame(gameState);
        return gameState;
    }

    public GameState getPlayingStateForPlayerAndState(PlayerId playerId) {
        log.info("getting the state for player {}", playerId);
        return gameStateRepository.getPlayingGameForPlayer(playerId)
                .orElseThrow(() -> new NotFoundException("No game found for player " + playerId.id()));
    }

    private void handleEndGame(GameState gameState) {
        log.info("handling end game for game {}", gameState.getId());
        sendEndGameSummary(gameState);
        switch (gameState.getWinner()) {
            case BLACK:
                achievementPublisher.unlock(gameState.getPlayer2().id(), Achievement.LETS_GOOO);
                achievementPublisher.unlock(gameState.getPlayer1().id(), Achievement.GO_HOME);
                break;
            case WHITE:
                achievementPublisher.unlock(gameState.getPlayer1().id(), Achievement.LETS_GOOO);
                achievementPublisher.unlock(gameState.getPlayer2().id(), Achievement.GO_HOME);
                break;
        }
        analyticsMessagePublisher.publishGameEndedMessage(GameEndedMessage.of(gameState));
        analyticsMessagePublisher.publishWinnerDeclaredMessage(WinnerDeclaredMessage.of(gameState));
    }

    private void sendEndGameSummary(GameState gameState) {
        log.info("sending end game summary for game {}", gameState.getId());
        aiCatalog.SendSummaryToAI(AiEndGameDto.fromDomain(gameState));
    }

}
