package com.sudokusolver.libsudoku;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * A fully populated Sudoku grid.
 *
 * This solution is produced by the Sudoku Solver and defines a grid of size
 * digit_range x digit_range with every cell filled in with a number in the
 * range 1..digit_range such that all Sudoku rules are observed.
 */
public class Solution {

    /**
     * Constructor converting a set of placements to an easy-to-access sudoku
     * solution grid.
     */
    public Solution(SudokuSize digitRange, List<Placement> rawSolution) {
        this.digitRange = digitRange;
        int gridSize = digitRange.getDigitRange() * digitRange.getDigitRange();

        // create new grid and populate with zeroes
        this.grid = new ArrayList<>(Collections.nCopies(gridSize,0));

        // override with actual placement values
        for (Placement placement : rawSolution) {
            int index = (placement.row - 1) * digitRange.getDigitRange() + placement.column - 1;
            this.grid.set(index, placement.number);
        }
    }

    /**
     * Get the size of the solution grid.
     */
    public SudokuSize getSudokuSize() {
        return this.digitRange;
    }

    /**
     * Get the number at the given sudoku grid cell.
     *
     * @param row    A row (1..digit_range) in the sudoku grid
     * @param column A column (1..digit_range) in the sudoku grid
     */
    public int getCellValue(int row, int column) {
        int index = (row - 1) * this.digitRange.getDigitRange() + column - 1;
        return this.grid.get(index);
    }

    // The size of the solution grid
    private SudokuSize digitRange;

    // The solution grid itself as a one-dimensional row-by-row vector.
    private List<Integer> grid;
}
