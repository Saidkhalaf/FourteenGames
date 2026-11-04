package be.kdg.gobackend.game.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

@Getter
@AllArgsConstructor
public class Board {
    private static final int MIN_SIZE = 5;
    private static final int MAX_SIZE = 19;
    private static final int[][] DIRECTIONS = {{0, 1}, {0, -1}, {1, 0}, {-1, 0}};

    private final int size;
    private final Stone[] stones;

    public Board(int size) {
        if (size < MIN_SIZE || size > MAX_SIZE)
            throw new IllegalArgumentException("Board size must be between %d and %d".formatted(MIN_SIZE, MAX_SIZE));
        this.size = size;
        this.stones = new Stone[size * size];
        Arrays.fill(this.stones, Stone.EMPTY);
    }

    public String[][] getBoardForDto() {
        final var board = new String[size][size];
        for (int y = 0; y < size; y++) {
            for (int x = 0; x < size; x++) {
                board[x][y] = String.valueOf(getStone(x, y).getShortName());
            }
        }
        return board;
    }


    public void placeStone(int x, int y, Stone stone) {
        if (isOutOfBounds(x, y))
            throw new IllegalArgumentException("Position is out of bounds");
        if (getStone(x, y) != Stone.EMPTY)
            throw new IllegalStateException("Spot is already taken");
        stones[y * size + x] = stone;
        checkCaptures(x, y);
    }

    private boolean isOutOfBounds(int x, int y) {
        return 0 > x || x >= size || 0 > y || y >= size;
    }

    private void removeStone(int x, int y) {
        stones[y * size + x] = Stone.EMPTY;
    }

    public Stone getStone(int x, int y) {
        return stones[y * size + x];
    }

    private void checkCaptures(int x, int y) {
        // liberties / neighbors
        final var stone = getStone(x, y);
        for (int[] dir : DIRECTIONS) {
            final var nx = x + dir[0];
            final var ny = y + dir[1];
            // out of bounds
            if (isOutOfBounds(nx, ny)) continue;
            final var neighborStone = getStone(nx, ny);
            if (neighborStone == Stone.EMPTY || neighborStone == stone) continue;
            tryCapture(nx, ny, neighborStone);
        }
        tryCapture(x, y, stone);
    }

    private void tryCapture(int x, int y, Stone stone) {
        final var visited = new boolean[size][size];
        final var result = analyseGroup(x, y, visited, stone);
        if (!result.touches.contains(Stone.EMPTY))
            for (Point point : result.visitedPoints())
                removeStone(point.x, point.y);
    }

    // Scoring Logic

    public Score calculateScore() {
        final var score = new Score();
        // instead of calculating the actual score, for simplicity, only the score difference is calculated
        final var visited = new boolean[size][size];

        for (int y = 0; y < size; y++) {
            for (int x = 0; x < size; x++) {
                final var currentStone = getStone(x, y);

                switch (currentStone) {
                    // one point for if the stone is yours
                    case BLACK -> score.addOneBlack();
                    case WHITE -> score.addOneWhite();

                    // for empty tiles, do an analysis to find the terretory size
                    case EMPTY -> {
                        if (visited[x][y]) continue;
                        score.add(calculateTerritoryScore(x, y, visited));
                    }
                }
            }
        }

        return score;
    }

    private Score calculateTerritoryScore(int x, int y, boolean[][] visited) {
        final var result = analyseGroup(x, y, visited, Stone.EMPTY);
        final var touchesBlack = result.touches.contains(Stone.BLACK);
        final var touchesWhite = result.touches.contains(Stone.WHITE);
        if (touchesBlack == touchesWhite) return new Score();
        final var groupSize = result.visitedPoints().size();
        if (touchesBlack) {
            return new Score(groupSize, 0);
        } else {
            return new Score(0, groupSize);
        }

    }

    private TerritoryResult analyseGroup(int startX, int startY, boolean[][] visited, Stone group) {
        final var touches = new HashSet<Stone>();
        touches.add(group);
        final var visitedPoints = new HashSet<Point>();

        final var queue = new ArrayDeque<Point>();
        final var point = new Point(startX, startY);
        queue.add(point);
        visitedPoints.add(point);
        visited[startX][startY] = true;

        while (!queue.isEmpty()) {
            final var current = queue.poll();

            for (final var dir : DIRECTIONS) {
                final var nx = current.x + dir[0];
                final var ny = current.y + dir[1];

                // out of bounds
                if (isOutOfBounds(nx, ny)) continue;

                final var neighborStone = getStone(nx, ny);

                touches.add(neighborStone);
                if (neighborStone == group && !visited[nx][ny]) {
                    visited[nx][ny] = true;
                    final var newPoint = new Point(nx, ny);
                    queue.add(newPoint);
                    visitedPoints.add(newPoint);
                }
            }
        }

        return new TerritoryResult(touches, visitedPoints);
    }

    private record TerritoryResult(Set<Stone> touches, Set<Point> visitedPoints) {}

    private record Point(int x, int y) {}
}