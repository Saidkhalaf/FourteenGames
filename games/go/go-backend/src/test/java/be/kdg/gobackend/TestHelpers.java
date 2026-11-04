package be.kdg.gobackend;

import org.junit.jupiter.api.Assertions;

import java.util.Arrays;

import static java.util.stream.Collectors.counting;
import static java.util.stream.Collectors.groupingBy;

public class TestHelpers {
    public static void assertBoardCounts(String[][] board, int emptyCount, int blackCount, int whiteCount){
        final var counts = Arrays.stream(board)
                .flatMap(Arrays::stream)
                .collect(groupingBy(c -> c, counting()));
        Assertions.assertEquals(emptyCount, counts.getOrDefault("_", 0L));
        Assertions.assertEquals(blackCount, counts.getOrDefault("B", 0L));
        Assertions.assertEquals(whiteCount, counts.getOrDefault("W", 0L));
    }
}
