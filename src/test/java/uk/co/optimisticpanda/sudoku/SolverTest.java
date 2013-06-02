package uk.co.optimisticpanda.sudoku;

import static org.fest.assertions.Assertions.assertThat;

import java.util.List;
import java.util.Map;

import org.junit.Test;

import com.google.common.base.Optional;

public class SolverTest {

    @Test
    public void checkParseGrid() {
        String puzzle = "4.....8.5.3..........7......2.....6.....8.4......1.......6.3.7.5..2.....1.4......";
        Map<String, Character> puzzleGrid = PuzzleParser.parse(puzzle);
        
        assertThat(puzzleGrid.get("A1")).isEqualTo('4');
        assertThat(puzzleGrid.get("I9")).isEqualTo('.');
        assertThat(puzzleGrid.get("E5")).isEqualTo('8');
    }
 
    @Test
    public void checkParse() {
        String grid = "003020600900305001001806400008102900700000008006708200002609500800203009005010300";
        
        Optional<Map<String, List<Character>>> result = new Solver().solve(grid);
        assertThat(result.isPresent()).isTrue();
        Map<String, List<Character>> results = result.get();
        
        assertThat(results.get("A1")).containsOnly('4');
        assertThat(results.get("A2")).containsOnly('8');
        assertThat(results.get("I9")).containsOnly('2');
        assertThat(results.get("E5")).containsOnly('6');
    }

}
