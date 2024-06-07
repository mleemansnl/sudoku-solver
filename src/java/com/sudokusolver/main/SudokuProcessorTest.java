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
        StringReader input = new StringReader("4 _ _ 1\n" + //
                "_ 1 3 _\n" + //
                "_ 4 1 _\n" + //
                "1 _ _ 3");
        StringWriter output = new StringWriter();

        boolean result = SudokuProcessor.processSudoku(input, output);

        assertTrue(result);
        assertEquals("4 3 2 1 \n" + //
                "2 1 3 4 \n" + //
                "3 4 1 2 \n" + //
                "1 2 4 3 \n" + //
                "", output.toString());
    }
}
