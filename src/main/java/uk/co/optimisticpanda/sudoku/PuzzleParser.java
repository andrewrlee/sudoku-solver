package uk.co.optimisticpanda.sudoku;

import static com.google.common.base.CharMatcher.inRange;
import static com.google.common.base.CharMatcher.is;
import static com.google.common.collect.Lists.charactersOf;
import static com.google.common.collect.Maps.newLinkedHashMap;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

public enum PuzzleParser {
    ;
    
    public static Map<String, Character> parse(String puzzle) {
        String values = inRange('0', '9').or(is('.')).retainFrom(puzzle);
        List<Character> chars = charactersOf(values);

        if (81 != chars.size() || chars.size() != GridInfo.getSquares().size()) {
            throw new RuntimeException("Squares don't match values size, both should equal 81");
        }

        Iterator<Character> charIterator = chars.iterator();
        Iterator<String> squareIterator = GridInfo.getSquares().iterator();

        Map<String, Character> result = newLinkedHashMap();
        while (squareIterator.hasNext() && charIterator.hasNext()) {
            result.put(squareIterator.next(), charIterator.next());
        }
        return result;
    }

}
