package uk.co.optimisticpanda.sudoku;

import static org.fest.assertions.Assertions.assertThat;

import org.junit.Test;

//@formatter:off
public class GridTest {

    @Test
    public void checkUnitListSize() {
        assertThat(GridInfo.getUnitList()).hasSize(27);
    }
    
    @Test
    public void checkSquaresSize() {
        assertThat(GridInfo.getSquares()).hasSize(81);
    }
    
    @Test
    public void checkUnitsAndPeers() {
        for (String string : GridInfo.getSquares()) {
            assertThat(GridInfo.getUnits(string)).hasSize(3);
            assertThat(GridInfo.getPeers(string)).hasSize(20);
        }
    }

    @Test
    public void checkDetailedUnits() {
        assertThat(GridInfo.getUnits("C2").get(0))//
                .containsExactly("A2", "B2", "C2", "D2", "E2", "F2", "G2", "H2", "I2");
        
        assertThat(GridInfo.getUnits("C2").get(1))//
            .containsExactly("C1", "C2", "C3", "C4", "C5", "C6", "C7", "C8", "C9");
        
        assertThat(GridInfo.getUnits("C2").get(2))//
            .containsExactly("A1", "A2", "A3", "B1", "B2", "B3", "C1", "C2", "C3");
        
        assertThat(GridInfo.getUnits("C2").get(0))//
            .containsExactly("A2", "B2", "C2", "D2", "E2", "F2", "G2", "H2", "I2");
        
        assertThat(GridInfo.getUnits("C2").get(1))//
            .containsExactly("C1", "C2", "C3", "C4", "C5", "C6", "C7", "C8", "C9");
        
        assertThat(GridInfo.getUnits("C2").get(2))//
            .containsExactly("A1", "A2", "A3", "B1", "B2", "B3", "C1", "C2", "C3");

    }

    @Test
    public void checkDetailedPeers() {
        assertThat(GridInfo.getPeers("C2"))//
        .containsOnly(
                "A2", "B2", "D2", "E2", "F2", "G2", "H2", "I2", // 
                "C1", "C3", "C4", "C5", "C6", "C7", "C8", "C9", //
                "A1", "A3", "B1", "B3");
    }
}
