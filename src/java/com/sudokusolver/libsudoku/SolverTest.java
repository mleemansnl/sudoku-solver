package com.sudokusolver.libsudoku;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Optional;

import org.junit.jupiter.api.Test;

public class SolverTest {

    /**
     * Test: solve an example sudoku and test solution
     */
    @Test
    void testSmallSudoku() {
        // Solve small example sudoku
        SudokuSolver solver = this.createSmallSudoku();
        Optional<Solution> result = solver.solve();

        // Check solution
        assertTrue(result.isPresent());
        if (result.isPresent()) {
            // Expected solution:
            // 4 3 2 1
            // 2 1 3 4
            // 3 4 1 2
            // 1 2 4 3
            Solution solution = result.get();

            // We expect a solution for a 4 by 4 sudoku
            assertEquals(SudokuSize.Four, solution.getSudokuSize());

            // check the value of a preset input: in row 1, column 1, the value should be 4
            assertEquals(4, solution.getCellValue(1, 1));

            // in row 1, column 2 the correct value is 3
            assertEquals(3, solution.getCellValue(1, 2));
        }
    }

    SudokuSolver createSmallSudoku() {
        // Setup the matrix for the following Sudoku:
        // 4 _ _ 1
        // _ 1 3 _
        // _ 4 1 _
        // 1 _ _ 3

        // Define input
        SudokuMatrix matrix = new SudokuMatrix(SudokuSize.Four);
        SudokuSolver solver = new SudokuSolver(matrix);

        solver.setInput(1, 1, 4);
        solver.setInput(1, 4, 1);

        solver.setInput(2, 2, 1);
        solver.setInput(2, 3, 3);

        solver.setInput(3, 2, 4);
        solver.setInput(3, 3, 1);

        solver.setInput(4, 1, 1);
        solver.setInput(4, 4, 3);

        return solver;
    }
}
