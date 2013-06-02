package uk.co.optimisticpanda.sudoku;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.google.common.base.Joiner;
import com.google.common.collect.Ordering;
import com.google.common.primitives.Ints;

public enum GridDisplayer {
    ;
    private static final Ordering<List<Character>> sizeOrdering = new Ordering<List<Character>>() {
        @Override
        public int compare(List<Character> left, List<Character> right) {
            return Ints.compare(left.size(), right.size());
        }
    };

    public static void display(Map<String, List<Character>> grid) {
        int width = sizeOrdering.max(grid.values()).size() + 2;

        int i = 1;
        StringBuilder builder = new StringBuilder();
        Iterator<List<Character>> values = grid.values().iterator();
        while (values.hasNext()) {
            String value = Joiner.on("").join(values.next());
            builder.append(String.format("%9s", value)).append(" ");
            if (i % 3 == 0) {
                builder.append(" | ");
            }
            if (i % 9 == 0) {
                builder.append("\n");
            }
            if (i % 27 == 0) {
                for (int j = 2; j < (width * 9); j++) {
                    builder.append(j % (width * 3) == 0 ? "+" : "-");
                }
                builder.append("\n");
            }
            i++;
        }
        System.out.println(builder.toString());
    }

    // def display(values):
    // "Display these values as a 2-D grid."
    // width = 1+max(len(values[s]) for s in squares)
    // line = '+'.join(['-'*(width*3)]*3)
    // for r in rows:
    // print ''.join(values[r+c].center(width)+('|' if c in '36' else '')
    // for c in cols)
    // if r in 'CF': print line
    // print

}
