package be.kdg.ipj3.tictactoebackend.application;

import be.kdg.ipj3.tictactoebackend.domain.*;
import be.kdg.ipj3.tictactoebackend.infrastructure.gameState.ai.dtos.GameStateDtoAI;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@Transactional
@Slf4j
public class GameStateService {

    private final GameStateRepository gameStateRepository;
    private final PlayerService playerService;
    private final AiCatalog aiCatalog;
    private final CharacterId aiUuid;

    public GameStateService(GameStateRepository gameStateRepository, PlayerService playerService, AiCatalog aiCatalog, @Value("${game-state-service.ai-uuid}") final UUID aiId) {
        this.gameStateRepository = gameStateRepository;
        this.playerService = playerService;
        this.aiCatalog = aiCatalog;
        this.aiUuid = new CharacterId(aiId);
    }

    public GameState createBoardAi(CharacterId player) {
        log.info("Creating new game board, match between {} and an AI", player.id());
        playerService.getOrCreatePlayer(player);
        checkIfPlaying(player);
        final var board = new GameState(player, aiUuid, true);
        gameStateRepository.save(board);
        return board;
    }

    private void checkIfPlaying(CharacterId player) {
        final var playerGame = gameStateRepository.getGameForPlayer(player);
        if (playerGame.isPresent() && !playerGame.get().isEmpty())
            throw new IllegalStateException("Player " + player.id() + " already playing a game with id: " + playerGame.get().getFirst().getId().id());
    }

    public GameState getState(GameStateId stateId) {
        log.info("Getting game state for {}", stateId.id());
        return gameStateRepository.get(stateId).orElseThrow(stateId::notFound);
    }

    public GameState getGameForPlayer(CharacterId playerId) {
        log.info("Getting game for player {}", playerId.id());
        final var gameStates = gameStateRepository.getGameForPlayer(playerId).orElse(List.of());
        if (gameStates.isEmpty()) throw new NotFoundException("No game found for player " + playerId.id());
        if (gameStates.size() > 1)
            throw new IllegalStateException("More than one game found for player " + playerId.id());
        return gameStates.getFirst();
    }

    public GameState makeMove(int x, int y, CharacterId playerId, GameStateId stateId) {
        log.info("Making move at ({}, {}) by player {}", x, y, playerId.id());
        final var board = gameStateRepository.get(stateId).orElseThrow(stateId::notFound);
        final var symbol = board.getSymbol(playerId);
        board.checkTurn(playerId);
        board.makeMove(x, y, symbol);
        gameStateRepository.save(board);
        return board;
    }

    public GameState makeAiMove(GameStateId stateId) {
        log.info("Making ai move");
        final var board = gameStateRepository.get(stateId).orElseThrow(stateId::notFound);
        board.checkTurn(aiUuid);
        final var answer = aiCatalog.askForMove(GameStateDtoAI.from(board)).orElseThrow(() -> new IllegalStateException("Ai could not make a move"));
        log.info("Ai move: ({}, {})", answer.row(), answer.col());
        board.makeMove(answer.row(), answer.col(), Cell.O);
        gameStateRepository.save(board);
        return board;
    }
}
