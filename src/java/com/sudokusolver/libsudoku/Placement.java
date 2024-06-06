package com.sudokusolver.libsudoku;

/**
 * In Sudoku, a placement models a number for a specific row-column cell.
 *
 * Given a digit range D (in a normal 9x9 Sudoku, D == 9), then: - row value
 * ranges 1..D inclusive - column value ranges 1..D inclusive - number value
 * ranges 1..D inclusive
 */
public class Placement {

    // The row and column (1..D) indentify a specific row-column cell in a Sudoku
    // grid.
    public int row;
    public int column;

    // The number (1..D) indicates the value of the given cell.
    public int number;

    public Placement(int row, int column, int number) {
        this.row = row;
        this.column = column;
        this.number = number;
    }
}
