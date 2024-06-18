package com.sudokusolver.main;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.StringReader;
import java.io.StringWriter;

import org.junit.jupiter.api.Test;

public class SudokuProcessorTest {

    /**
     * Test: solve an example sudoku and test solution
     */
    @Test
    void testSmallSudoku() {
        StringReader input = new StringReader("""
                4 _ _ 1
                _ 1 3 _
                _ 4 1 _
                1 _ _ 3""");
        StringWriter output = new StringWriter();

        boolean result = SudokuProcessor.processSudoku(input, output);

        assertTrue(result);
        assertEquals("""
                4 3 2 1\s
                2 1 3 4\s
                3 4 1 2\s
                1 2 4 3\s
                """, output.toString());
    }
}
