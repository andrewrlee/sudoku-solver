package uk.co.optimisticpanda.sudoku;

import static org.fest.assertions.Assertions.assertThat;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import org.junit.Before;
import org.junit.Test;

import com.google.common.base.Charsets;
import com.google.common.base.Optional;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.google.common.io.Files;
import com.google.common.primitives.Ints;

public class BenchmarkingTests {
    private Solver parser;

    @Before
    public void createSolver() {
        parser = new Solver();
    }

    @Test
    public void checkSolutions() throws IOException {
        solveAll(readTestFile("easy.txt"), "EASY");
        solveAll(readTestFile("hard.txt"), "HARD");
        solveAll(readTestFile("hardest.txt"), "HARDEST");
    }

    private void solveAll(List<String> grids, String name) {

        List<Long> durations = Lists.newArrayList();

        for (String grid : grids) {
            durations.add(solve(grid));
        }

        long totalDuration = sum(durations);

        System.out.printf("Solved %d of %d %s puzzles (avg %.3f seconds (%.2f Hz), max %.3f seconds).\n", durations.size(), //
                grids.size(), //
                name, //
                totalDuration / grids.size() / 1000.0, //
                1000.0 / (totalDuration / grids.size()), //
                Ints.max(Ints.toArray(durations)) / 1000.0);
    }

    private long sum(List<Long> durations) {
        long total = 0L;
        for (Long duration : durations) {
            total += duration;
        }
        return total;
    }

    private long solve(String grid) {
        long before = System.nanoTime();
        assertThat(solved(parser.solve(grid)).isPresent()).isTrue();
        return TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - before);
    }

    private Optional<Map<String, List<Character>>> solved(Optional<Map<String, List<Character>>> potentialSolution) {
        for (List<String> squares : GridInfo.getUnitList()) {
            Set<Character> squareValues = Sets.newHashSet();
            for (String square : squares) {
                List<Character> values = potentialSolution.get().get(square);
                assertThat(values).hasSize(1);
                squareValues.add(values.get(0));
            }
            if (!Sets.newHashSet(Lists.charactersOf("123456789")).equals(squareValues)) {
                return Optional.absent();
            }
        }
        return potentialSolution;
    }

    private List<String> readTestFile(String name) throws IOException {
        return Files.readLines(new File("src/test/resources/" + name), Charsets.UTF_8);
    }

}
