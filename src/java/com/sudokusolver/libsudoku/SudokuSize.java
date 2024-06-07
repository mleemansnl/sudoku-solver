package com.sudokusolver.libsudoku;

/**
 * SudokuSize predefines valid possible digit range sizes for a valid sudoku
 * grid.
 *
 * Note that each valid value is a perfect square number, a property needed to
 * correctly model the sudoku grid conditions.
 */
public enum SudokuSize {
    // A Sudoku of 4 x 4, using numbers 1..4 and having boxes of 2x2
    Four(4),
  
    // A Sudoku of 9 x 9, using numbers 1..9 and having boxes of 3x3
    Nine(9),
  
    // A Sudoku of 16 x 16, using numbers 1..16 (often printed as 0..F) and having boxes of 4x4
    Sixteen(16);

    /**
     * Constructor to store digitRange value associated with a SudokuSize
     */
    SudokuSize(int digitRange) {
        this.digitRange = digitRange;
    }

    /**
     * Get digitRange associated with this enum value.
     */
    public int getDigitRange() {
        return this.digitRange;
    }

    private int digitRange;
}
