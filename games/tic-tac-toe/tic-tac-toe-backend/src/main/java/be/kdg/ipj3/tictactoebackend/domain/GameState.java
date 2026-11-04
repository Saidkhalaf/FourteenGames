package be.kdg.ipj3.tictactoebackend.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;

@Getter
@AllArgsConstructor
public class GameState {
    private final GameStateId id;
    private final Cell[][] board;
    private CharacterId playerOne;
    private CharacterId playerTwo;
    private Boolean isPlayerOneTurn;
    private GameStatus status;
    private CharacterId winner;
    private boolean isAiGame;

    public GameState(CharacterId playerOne, CharacterId playerTwo, boolean isAiGame) {
        this.id = GameStateId.create();
        this.board = new Cell[3][3];
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                this.board[i][j] = Cell.EMPTY;
            }
        }
        this.playerOne = playerOne;
        this.playerTwo = playerTwo;
        // hardcoded, player 1 always starts
        this.isPlayerOneTurn = true;
        this.status = GameStatus.IN_PROGRESS;
        this.isAiGame = isAiGame;
    }

    public String[][] getGameStateForAI() {
        return Arrays.stream(this.board)
                .map(row -> Arrays.stream(row)
                        .map(Cell::getAiValue)
                        .toArray(String[]::new)
                )
                .toArray(String[][]::new);
    }

    public Character[][] getGameStateAsStrings() {
        return Arrays.stream(this.board)
                .map(row -> Arrays.stream(row)
                        .map(Cell::getName)
                        .toArray(Character[]::new)
                )
                .toArray(Character[][]::new);
    }

    private void setCell(int x, int y, Cell cell) {
        if (x < 0 || x >= 3 || y < 0 || y >= 3) {
            throw new IllegalArgumentException("Coordinates out of bounds");
        }
        if (this.board[x][y] != Cell.EMPTY) {
            throw new IllegalStateException("This cell is already occupied");
        }
        this.board[x][y] = cell;
    }

    public void makeMove(int x, int y, Cell symbol) {
        if (!this.status.equals(GameStatus.IN_PROGRESS)) throw new IllegalStateException("Game is already over");
        setCell(x, y, symbol);

        if (hasWon(symbol)) {
            this.status = GameStatus.WON;
            this.winner = this.isPlayerOneTurn ? this.playerOne : this.playerTwo;
        } else if (isFull()) {
            this.status = GameStatus.DRAW;
        } else {
            this.isPlayerOneTurn = !this.isPlayerOneTurn;
        }
    }

    public boolean hasWon(Cell playerCell) {
        // Check rows and columns
        for (int i = 0; i < 3; i++) {
            if ((board[i][0] == playerCell && board[i][1] == playerCell && board[i][2] == playerCell) ||
                    (board[0][i] == playerCell && board[1][i] == playerCell && board[2][i] == playerCell)) {
                return true;
            }
        }
        // Check diagonals
        return (board[0][0] == playerCell && board[1][1] == playerCell && board[2][2] == playerCell) ||
                (board[0][2] == playerCell && board[1][1] == playerCell && board[2][0] == playerCell);
    }

    public boolean isFull() {
        return Arrays.stream(board)
                     .flatMap(Arrays::stream)
                     .noneMatch(cell -> cell == Cell.EMPTY);
    }

    public void checkTurn(CharacterId playerId) {
        if (!this.status.equals(GameStatus.IN_PROGRESS)) throw new IllegalStateException("Game is already over");
        if (this.isPlayerOneTurn ? !this.playerOne.equals(playerId) : !this.playerTwo.equals(playerId)) {
            throw new IllegalStateException("It is not your turn");
        }
    }

    public Cell getSymbol(CharacterId playerId) {
        if (this.playerOne.equals(playerId)) {
            return Cell.X;
        } else if (this.playerTwo.equals(playerId)) {
            return Cell.O;
        } else {
            throw new IllegalArgumentException("You are not in the game");
        }
    }
}
