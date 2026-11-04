package be.kdg.ipj3.tictactoebackend.domain;

import lombok.Getter;

@Getter public enum Cell {
    EMPTY('_', "0"),
    X('X', "-1"),
    O('O', "1");

    private final char name;
    private final String aiValue;

    Cell(char name, String aiValue) {
        this.name = name;
        this.aiValue = aiValue;
    }
}
