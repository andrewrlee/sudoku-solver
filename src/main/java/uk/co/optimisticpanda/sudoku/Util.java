package uk.co.optimisticpanda.sudoku;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.google.common.base.Predicate;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

public enum Util {
    ;

    public static Map<String, List<Character>> copy(Map<String, List<Character>> map) {
        Map<String, List<Character>> copy = Maps.newLinkedHashMap();
        for (Entry<String, List<Character>> entry : map.entrySet()) {
            copy.put(entry.getKey(), Lists.newArrayList(entry.getValue()));
        }
        return copy;
    }

    public static Predicate<List<Character>> haveOnlyASingleItem() {
        return new Predicate<List<Character>>() {
            public boolean apply(List<Character> input) {
                return input.size() == 1;
            }
        };
    }

}
