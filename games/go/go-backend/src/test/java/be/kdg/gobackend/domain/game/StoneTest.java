package be.kdg.gobackend.domain.game;

import be.kdg.gobackend.game.domain.Stone;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class StoneTest {

    @Nested
    @DisplayName("SuccessFlows")
    class SuccessFlows {

        @Test
        @DisplayName("Should return EMPTY when shortName is '_'")
        void shouldReturnEmptyForUnderscore() {
            // Arrange
            final var shortName = '_';

            // Act
            final var result = Stone.fromShortName(shortName);

            // Assert
            assertThat(result).isEqualTo(Stone.EMPTY);
        }

        @Test
        @DisplayName("Should return BLACK when shortName is 'B'")
        void shouldReturnBlackForB() {
            // Arrange
            final var shortName = 'B';

            // Act
            final var result = Stone.fromShortName(shortName);

            // Assert
            assertThat(result).isEqualTo(Stone.BLACK);
        }

        @Test
        @DisplayName("Should return WHITE when shortName is 'W'")
        void shouldReturnWhiteForW() {
            // Arrange
            final var shortName = 'W';

            // Act
            final var result = Stone.fromShortName(shortName);

            // Assert
            assertThat(result).isEqualTo(Stone.WHITE);
        }
    }

    @Nested
    @DisplayName("ErrorFlows")
    class ErrorFlows {

        @Test
        @DisplayName("Should throw IllegalStateException for invalid shortName")
        void shouldThrowExceptionForInvalidShortName() {
            // Arrange
            final var shortName = 'X';

            // Act & Assert
            assertThatThrownBy(() -> Stone.fromShortName(shortName))
                    .isInstanceOf(IllegalStateException.class)
                    .hasMessage("tile on board state is not an allowed char. state: X");
        }
    }
}