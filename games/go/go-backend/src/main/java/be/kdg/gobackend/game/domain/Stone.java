package be.kdg.gobackend.game.domain;

import lombok.Getter;

@Getter
public enum Stone {
    EMPTY('_' ),
    BLACK('B' ),
    WHITE('W' );

    private final char shortName;

    Stone(char shortName) {
        this.shortName = shortName;
    }

    public static Stone fromShortName(char shortName) {
        return switch (shortName) {
            case '_' -> Stone.EMPTY;
            case 'B' -> Stone.BLACK;
            case 'W' -> Stone.WHITE;
            default ->
                    throw new IllegalStateException("tile on board state is not an allowed char. state: " + shortName);
        };
    }
}
