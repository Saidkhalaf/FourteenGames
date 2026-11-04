package be.kdg.gobackend.domain.game;

import be.kdg.gobackend.game.domain.Board;
import be.kdg.gobackend.game.domain.Stone;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class BoardTest {

    @Nested
    @DisplayName("Success Flows")
    class SuccessFlows {

        @Test
        @DisplayName("Should create a board with the given size")
        void shouldCreateBoard() {
            // Arrange
            final var board = new Board(9);
            final var x = 3;
            final var y = 3;
            final var stone = Stone.BLACK;

            // Act
            board.placeStone(x, y, stone);

            // Assert
            assertThat(board.getStone(x, y)).isEqualTo(stone);
        }

        @Test
        @DisplayName("getBoardForDto() creates the board as a 2d array")
        void getBoardForDtoCreatesBoardAs2dArray(){
            // Arrange
            final var board = new Board(9);
            final var x = 2;
            final var y = 5;
            final var stone = Stone.BLACK;
            board.placeStone(x, y, stone);

            // Act
            final var boardDto = board.getBoardForDto();

            // Assert
            assertThat(boardDto[x][y]).isEqualTo(String.valueOf(stone.getShortName()));
        }

        @Test
        @DisplayName("Should place a BLACK stone on an empty spot")
        void shouldPlaceBlackStone() {
            // Arrange
            final var board = new Board(9);
            final var stone = Stone.BLACK;

            // Act
            board.placeStone(3, 3, stone);

            // Assert
            assertThat(board.getStone(3, 3)).isEqualTo(stone);
        }

        @Test
        @DisplayName("Should place a WHITE stone on an empty spot")
        void shouldPlaceWhiteStone() {
            // Arrange
            final var board = new Board(9);
            final var stone = Stone.WHITE;

            // Act
            board.placeStone(4, 4, stone);

            // Assert
            assertThat(board.getStone(4, 4)).isEqualTo(stone);
        }

        @Test
        @DisplayName("Should return size correctly")
        void shouldReturnSize() {
            // Arrange
            final var size = 13;
            final var board = new Board(size);

            // Act
            final var res = board.getSize();

            // Assert
            assertThat(res).isEqualTo(size);
        }
    }

    @Nested
    @DisplayName("Scoring Logic")
    class ScoringLogic {

        @Test
        @DisplayName("Empty board should have score 0")
        void emptyBoardScore() {
            // Arrange
            final var board = new Board(9);

            // Act
            final var res = board.calculateScore();

            // Assert
            assertThat(res.getBlackScore()).isEqualTo(0.0);
            assertThat(res.getWhiteScore()).isEqualTo(0.0);

        }

        @Test
        @DisplayName("Stones only: Black +1, White -1")
        void stonesOnlyScore() {
            // Arrange
            final var board = new Board(9);
            // Black stone (1B)
            board.placeStone(0, 0, Stone.BLACK);
            // White stone (1W)
            board.placeStone(1, 1, Stone.WHITE);

            // Act
            final var res = board.calculateScore();

            // Assert: 1B 1W
            assertThat(res.getBlackScore()).isEqualTo(1.0);
            assertThat(res.getWhiteScore()).isEqualTo(1.0);

        }

        @Test
        @DisplayName("Black territory: Empty spot surrounded by Black counts as point")
        void blackTerritory() {
            // Arrange: Create a small corner territory for Black
            // _ B
            // B
            final var board = new Board(9);
            board.placeStone(1, 0, Stone.BLACK);
            board.placeStone(0, 1, Stone.BLACK);
            // (0,0) is black territory
            board.placeStone(8, 8, Stone.WHITE); // to prevent the whole board being territory.

            // Act
            final var res = board.calculateScore();

            // Assert
            // Stones: 2 Black (2B) 1 White (1W)
            // Territory: 1 Empty touching only Black (1B)
            // Total: 3B 1W
            assertThat(res.getBlackScore()).isEqualTo(3.0);
            assertThat(res.getWhiteScore()).isEqualTo(1.0);

        }

        @Test
        @DisplayName("White territory: Empty spot surrounded by White counts as negative point")
        void whiteTerritory() {
            // Arrange: Create a small enclosure for White
            // W _ W
            //   W
            final var board = new Board(9);
            board.placeStone(0, 0, Stone.WHITE);
            board.placeStone(2, 0, Stone.WHITE);
            board.placeStone(1, 1, Stone.WHITE);
            // (1,0) is EMPTY, surrounded by White
            board.placeStone(8, 8, Stone.BLACK); // to prevent the whole board being territory.

            // Act
            final var res = board.calculateScore();

            // Assert
            // Stones: 3 White 3W 1 Black 1B
            // Territory: 1 Empty touching only White 1W
            // Total: 1B 4W
            assertThat(res.getBlackScore()).isEqualTo(1.0);
            assertThat(res.getWhiteScore()).isEqualTo(4.0);

        }

        @Test
        @DisplayName("Neutral territory: Touching both Black and White counts as 0")
        void neutralTerritory() {
            // Arrange
            //   B
            // W _ W
            //   B
            final var board = new Board(9);
            board.placeStone(4, 5, Stone.BLACK);
            board.placeStone(6, 5, Stone.BLACK);
            board.placeStone(5, 4, Stone.WHITE);
            board.placeStone(5, 6, Stone.WHITE);
            // (1,0) is EMPTY, touching both

            // Act
            final var res = board.calculateScore();

            // Assert
            // Stones: Black 2B White 2W
            assertThat(res.getBlackScore()).isEqualTo(2.0);
            assertThat(res.getWhiteScore()).isEqualTo(2.0);

        }

        @Test
        @DisplayName("Larger territory group is calculated correctly via Flood Fill")
        void largeTerritoryFloodFill() {
            // Arrange: Enclose a 2x1 area in corner
            //   B B
            // B _ _ B
            //   B B
            final var board = new Board(9);
            board.placeStone(2, 0, Stone.BLACK);
            board.placeStone(3, 0, Stone.BLACK);
            board.placeStone(1, 1, Stone.BLACK);
            board.placeStone(2, 2, Stone.BLACK);
            board.placeStone(3, 2, Stone.BLACK);
            board.placeStone(4, 1, Stone.BLACK);

            board.placeStone(8, 8, Stone.WHITE); // to prevent the whole board being terretory.

            // Territory at (2,1) and (3,1) should be captured.

            // Act
            final var score = board.calculateScore();

            // Assert
            // Stones: 6 Black 6B 1 White 1W
            // Territory: 2 empty spots 2B
            // Total: 8B 1W
            assertThat(score.getBlackScore()).isEqualTo(8.0);
            assertThat(score.getWhiteScore()).isEqualTo(1.0);
        }
    }

    @Nested
    @DisplayName("Error Flows")
    class ErrorFlows {
        @ParameterizedTest
        @CsvSource({
                "10, 0, Position is out of bounds", // X too high
                "-1, 0, Position is out of bounds", // X too low
                "0, 10, Position is out of bounds", // Y too high
                "0, -1, Position is out of bounds"  // Y too low
        })
        @DisplayName("Should throw exception when placing stone out of bounds")
        void shouldThrowExceptionForOutOfBounds(int x, int y, String message) {
            // Arrange
            final var size = 9;
            final var board = new Board(size);
            final var stone = Stone.BLACK;

            // Act & Assert
            assertThatThrownBy(() -> board.placeStone(x, y, stone))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage(message);
        }

        @Test
        @DisplayName("Should throw exception when placing a stone on a non-empty spot")
        void shouldThrowExceptionForOccupiedSpot() {
            // Arrange
            final var size = 9;
            final var x = 5;
            final var y = 5;
            final var board = new Board(size);
            board.placeStone(x, y, Stone.BLACK);

            // Act & Assert
            assertThatThrownBy(() -> board.placeStone(x, y, Stone.WHITE))
                    .isInstanceOf(IllegalStateException.class)
                    .hasMessage("Spot is already taken");
        }

        @Test
        @DisplayName("Should not create board when size is under 5")
        void shouldNotCreateBoardWhenSizeUnder5() {
            // Act & Assert
            assertThatThrownBy(() -> new Board(4))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("Board size must be between 5 and 19");
        }

        @Test
        @DisplayName("Should not create board when size is above 19")
        void shouldNotCreateBoardWhenSizeOver19() {
            // Act & Assert
            assertThatThrownBy(() -> new Board(20))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("Board size must be between 5 and 19");
        }
    }
}