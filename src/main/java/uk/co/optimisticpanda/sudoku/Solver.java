package uk.co.optimisticpanda.sudoku;

import static com.google.common.base.Optional.absent;
import static com.google.common.base.Optional.of;
import static com.google.common.collect.Iterables.all;
import static com.google.common.collect.Lists.newArrayList;
import static uk.co.optimisticpanda.sudoku.Util.copy;
import static uk.co.optimisticpanda.sudoku.Util.haveOnlyASingleItem;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.google.common.base.Optional;

public class Solver {

    // Reduce the search space using constraint propagation and then search for a valid solution
    public Optional<Map<String, List<Character>>> solve(String grid) {
        return search(parse(grid));
    }
    
    // Reduce the search space using constraint propagation.
    Optional<Map<String, List<Character>>> parse(String puzzle) {
        Map<String, Character> puzzleGrid = PuzzleParser.parse(puzzle);
        Map<String, List<Character>> answerGrid = GridInfo.newAnswer();

        for (Entry<String, Character> puzzleSquare : puzzleGrid.entrySet()) {
            if (GridInfo.isAProvidedAnswer(puzzleSquare) && //
                    !assignValues(answerGrid, puzzleSquare.getKey(), puzzleSquare.getValue()).isPresent()) {
                absent(); // Problem assigning this answer to the grid 
            }
        }
        return of(answerGrid);
    }

    private Optional<Map<String, List<Character>>> assignValues(Map<String, List<Character>> values, String square, Character digit) {
        List<Character> otherValues = newArrayList(values.get(square));
        otherValues.remove(digit);
        for (Character character : otherValues) {
            if (!eliminate(values, square, character).isPresent()) {
                return absent(); 
            }
        }
        return of(values);
    }

    private Optional<Map<String, List<Character>>> eliminate(Map<String, List<Character>> values, String square, Character digit) {
        List<Character> otherValues = values.get(square);
        if (!otherValues.contains(digit)) { 
            return of(values); // Not present in this square
        }
        otherValues.remove(digit);
        if (otherValues.isEmpty()) {
            return absent(); // Removed last possibility in this square!
        }
        if (otherValues.size() == 1) {
            Character d2 = otherValues.get(0);
            for (String peer : GridInfo.getPeers(square)) {
                if (!eliminate(values, peer, d2).isPresent()) {
                    return absent(); // Moved peer into invalid state
                }
            }
        }
        for (List<String> unit : GridInfo.getUnits(square)) {
            List<String> availablePlaces = newArrayList();
            for (String squareInUnit : unit) {
                if (values.get(squareInUnit).contains(digit)) {
                    availablePlaces.add(squareInUnit);
                }
            }
            if (availablePlaces.isEmpty()) {
                return absent(); // cannot place square anywhere in this unit!
            }
            if (availablePlaces.size() == 1) {
                if (!assignValues(values, availablePlaces.get(0), digit).isPresent()) {
                    return absent(); // problem occurred when assigning this digit
                }
            }
        }
        return of(values);
    }

    private Optional<Map<String, List<Character>>> search(Optional<Map<String, List<Character>>> results) {
        if (!results.isPresent()) {
            return Optional.absent(); // something failed earlier
        }
        Map<String, List<Character>> solution = results.get();
        if (all(solution.values(), haveOnlyASingleItem())) {
            return results; // all squares have been found, we have found a complete solution!
        }
        String square = squareWithFewestPossibilities(solution);
        List<Character> list = solution.get(square);

        for (Character digit : list) {
            //try to assign each digit to the square with the least amount of possible values.
            //And then recursively search for complete solutions until we find one
            Optional<Map<String, List<Character>>> assignValues = assignValues(copy(solution), square, digit);
            Optional<Map<String, List<Character>>> result = search(assignValues);
            
            if (result.isPresent()) {
                return result;
            }
        }
        return Optional.absent(); //This is not a complete solution 
    }

    private String squareWithFewestPossibilities(Map<String, List<Character>> answerGrid) {
        Entry<String, List<Character>> min = null;
        for (Entry<String, List<Character>> answerSquare : answerGrid.entrySet()) {
            if (answerSquare.getValue().size() == 1) {
                continue; // already solved
            }
            if (min == null) {
                min = answerSquare;
                break;
            }
            if (min.getValue().size() > answerSquare.getValue().size()) {
                min = answerSquare;
            }
        }
        return min.getKey();
    }

}
