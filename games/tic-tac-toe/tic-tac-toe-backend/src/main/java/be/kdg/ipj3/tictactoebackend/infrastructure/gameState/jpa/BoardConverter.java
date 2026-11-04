package be.kdg.ipj3.tictactoebackend.infrastructure.gameState.jpa;

import be.kdg.ipj3.tictactoebackend.domain.Cell;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

import java.util.Arrays;


@Converter
public class BoardConverter implements AttributeConverter<Cell[][], String> {
    @Override
    public String convertToDatabaseColumn(Cell[][] cell) {
        if (cell == null) return "_________"; // 9 spaces
        return Arrays.stream(cell)
                .flatMap(Arrays::stream)
                .map(Cell::getName)
                .collect(StringBuilder::new, StringBuilder::append, StringBuilder::append)
                .toString();
    }

    @Override
    public Cell[][] convertToEntityAttribute(String dbData) {
        Cell[][] grid = new Cell[3][3];
        char[] chars = (dbData != null && dbData.length() == 9)
                ? dbData.toCharArray()
                : "_________".toCharArray();

        java.util.stream.IntStream.range(0, 9).forEach(i -> {
            int r = i / 3;
            int c = i % 3;
            char name = chars[i];
            // Find the cell by name (char), default to EMPTY
            grid[r][c] = Arrays.stream(Cell.values())
                    .filter(cell -> cell.getName() == name)
                    .findFirst()
                    .orElse(Cell.EMPTY);
        });
        return grid;
    }
}