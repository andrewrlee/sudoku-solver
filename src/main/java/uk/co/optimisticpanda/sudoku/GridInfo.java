package uk.co.optimisticpanda.sudoku;

import static com.google.common.base.Predicates.equalTo;
import static com.google.common.base.Predicates.not;
import static com.google.common.collect.Collections2.filter;
import static com.google.common.collect.FluentIterable.from;
import static com.google.common.collect.Lists.charactersOf;
import static com.google.common.collect.Lists.newArrayList;
import static com.google.common.collect.Lists.partition;
import static com.google.common.collect.Maps.newHashMap;
import static com.google.common.collect.Maps.newLinkedHashMap;
import static com.google.common.collect.Sets.cartesianProduct;
import static com.google.common.collect.Sets.newHashSet;
import static java.util.Collections.unmodifiableList;
import static java.util.Collections.unmodifiableSet;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import com.google.common.base.Function;
import com.google.common.base.Joiner;
import com.google.common.collect.Ordering;

public enum GridInfo {
    INSTANCE;

    private final List<Character> ROWS = charactersOf("123456789");
    private final List<Character> COLS = charactersOf("ABCDEFGHI");
    private final Set<List<String>> unitList;
    private List<String> squares;
    private Map<String, List<List<String>>> units;
    private Map<String, List<String>> peers;

    @SuppressWarnings("unchecked")
    private List<String> crossProduct(List<Character> a, List<Character> b) {
        Set<List<Character>> combinations = cartesianProduct(newHashSet(a), newHashSet(b));
        return from(combinations).transform(new Function<List<Character>, String>() {
            public String apply(List<Character> input) {
                return Joiner.on("").join(input);
            }
        }).toList();

    }

    private GridInfo() {
        unitList = gatherUnitLists(ROWS, COLS);
        squares = Ordering.natural().sortedCopy(crossProduct(COLS, ROWS));
        units = gatherUnits(squares, unitList);
        peers = gatherPeers(squares, units);
    }

    private static Map<String, List<List<String>>> gatherUnits(List<String> squares, Set<List<String>> unitList) {
        Map<String, List<List<String>>> result = newHashMap();
        for (String square : squares) {
            List<List<String>> squareResults = newArrayList();
            for (List<String> unit : unitList) {
                if (unit.contains(square)) {
                    squareResults.add(unit);
                }
            }
            result.put(square, squareResults);
        }
        return result;
    }

    private static Map<String, List<String>> gatherPeers(List<String> squares, Map<String, List<List<String>>> units) {
        Map<String, List<String>> result = newHashMap();
        for (String square : squares) {
            Set<String> squareResults = newHashSet();
            for (List<String> list : units.get(square)) {
                squareResults.addAll(filter(list, not(equalTo(square))));
            }
            result.put(square, newArrayList(squareResults));
        }
        return result;
    }

    private Set<List<String>> gatherUnitLists(List<Character> rows, List<Character> cols) {
        Set<List<String>> result = newHashSet();
        // Gather horizontal rows
        for (Character row : rows) {
            List<String> rowValues = newArrayList();
            for (Character col : cols) {
                rowValues.add("" + col + row);
            }
            result.add(rowValues);
        }

        // Gather Vertical rows
        for (Character col : cols) {
            List<String> colValues = newArrayList();
            for (Character row : rows) {
                colValues.add("" + col + row);
            }
            result.add(colValues);
        }

        // Gather 3x3 squares
        for (List<Character> rowGroup : partition(rows, 3)) {
            for (List<Character> colGroup : partition(cols, 3)) {
                List<String> square = newArrayList();
                for (Character row : rowGroup) {
                    for (Character col : colGroup) {
                        square.add("" + col + row);
                    }
                }
                result.add(square);
            }
        }
        return result;
    }

    public static List<String> getSquares() {
        return unmodifiableList(INSTANCE.squares);
    }

    public static Set<List<String>> getUnitList() {
        return unmodifiableSet(INSTANCE.unitList);
    }

    public static List<List<String>> getUnits(String square) {
        return unmodifiableList(INSTANCE.units.get(square));
    }

    public static List<String> getPeers(String square) {
        return unmodifiableList(//
                Ordering.natural().sortedCopy(INSTANCE.peers.get(square)));
    }

    public static Map<String, List<Character>> newAnswer() {
        Map<String, List<Character>> result = newLinkedHashMap();
        for (String square : GridInfo.getSquares()) {
            result.put(square, newArrayList(INSTANCE.ROWS));
        }
        return result;
    }
    
    public static boolean isAProvidedAnswer(Entry<String, Character> square){
        return INSTANCE.ROWS.contains(square.getValue());
    }
    
}
