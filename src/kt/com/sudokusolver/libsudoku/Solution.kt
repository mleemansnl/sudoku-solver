package com.sudokusolver.libsudoku

/**
 * A fully populated Sudoku grid.
 *
 * This solution is produced by the Sudoku Solver and defines a grid of size
 * digit_range x digit_range with every cell filled in with a number in the
 * range 1..digit_range such that all Sudoku rules are observed.
 */
class Solution(
    // The size of the solution grid
    val sudokuSize: SudokuSize,
) {
    // The solution grid itself as a one-dimensional row-by-row vector.
    private lateinit var grid: List<Int>

    /**
     * Constructor converting a set of placements to an easy-to-access sudoku
     * solution grid.
     */
    constructor(
        sudokuSize: SudokuSize,
        rawSolution: List<Placement>,
    ) : this(sudokuSize) {
        val gridSize = sudokuSize.digitRange * sudokuSize.digitRange

        // create new grid and populate with zeroes
        val grid = MutableList(gridSize) { 0 }
        this.grid = grid

        // override with actual placement values
        for (placement in rawSolution) {
            val index = (placement.row - 1) * sudokuSize.digitRange + placement.column - 1
            grid[index] = placement.number
        }
    }

    /**
     * Get the number at the given sudoku grid cell.
     *
     * @param row    A row (1..digit_range) in the sudoku grid
     * @param column A column (1..digit_range) in the sudoku grid
     */
    fun getCellValue(
        row: Int,
        column: Int,
    ): Int {
        val index = (row - 1) * this.sudokuSize.digitRange + column - 1
        return this.grid[index]
    }
}
