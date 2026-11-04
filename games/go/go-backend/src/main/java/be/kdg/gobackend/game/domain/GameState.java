package be.kdg.gobackend.game.domain;

import be.kdg.gobackend.shared.exception.NotFoundException;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;

// note in general, the game only allows user vs AI at the moment.
@Getter
@Slf4j
@AllArgsConstructor
public class GameState {
    public static final double KOMI = 6.5;
    private final GameStateId id;
    private final Board board;
    private final boolean isAiGame;
    private final PlayerId player1;
    private final PlayerId player2;
    private int turnCount;
    private boolean player1AtTurn;
    private boolean isLastTurnPassed;
    private Score score;
    private Stone winner;
    private LocalDateTime createdAt;

    public GameState(int size, PlayerId player1, PlayerId player2, boolean isAiGame, LocalDateTime createdAt) {
        log.info("Creating a new game with player {} of size {}", player1, size);
        this.player1 = player1;
        this.player2 = player2;
        this.id = new GameStateId();
        this.turnCount = 0;
        this.board = new Board(size);
        this.isAiGame = isAiGame;
        this.player1AtTurn = true;
        this.isLastTurnPassed = false;
        this.score = new Score();
        this.winner = Stone.EMPTY;
        this.createdAt = createdAt;
    }

    public String[][] getBoardForDto() {
        return this.board.getBoardForDto();
    }

    public void placeStone(int x, int y, Stone stone) {
        log.info("placing a stone at {}, {}", x, y);
        board.placeStone(x, y, stone);
        this.isLastTurnPassed = false;
    }

    public void switchPlayerAtTurn() {
        this.player1AtTurn = !this.isPlayer1AtTurn();
        this.turnCount++;
    }

    public int getSize() {
        return this.board.getSize();
    }

    public void passTurn() {
        log.info("Passing turn");
        if (!this.isLastTurnPassed) {
            this.isLastTurnPassed = true;
            return;
        }
        this.score = this.board.calculateScore();
        this.score.addWhite(KOMI);
        this.winner = this.score.getWinner();
        log.info("""
                The game has ended.
                Final score: {}
                Winner: {}
                """, this.score, this.winner);

    }

    public void verifyPlayer(PlayerId currentPlayerId, Boolean isAiGame) {
        if (isAiGame) {
            if (!player1.equals(currentPlayerId)) {
                throw new NotFoundException("Game was not found");
            }
            if (!player1AtTurn) {
                throw new IllegalStateException("It is not player's turn");
            }
        } else {
            if (!player1.equals(currentPlayerId) && !player2.equals(currentPlayerId))
                throw new NotFoundException("Game was not found.");
            if (this.player1AtTurn && !player1.equals(currentPlayerId))
                throw new IllegalStateException("Player is not at turn.");
            if (!this.player1AtTurn && !player2.equals(currentPlayerId))
                throw new IllegalStateException("Player is not at turn.");
        }
    }
}
