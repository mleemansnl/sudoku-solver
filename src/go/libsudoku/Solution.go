package libsudoku

/**
 * A fully populated Sudoku grid.
 *
 * This solution is produced by the Sudoku Solver and defines a grid of size
 * digit_range x digit_range with every cell filled in with a number in the
 * range 1..digit_range such that all Sudoku rules are observed.
 */
type Solution interface {
	/**
	 * Get the size of the solution grid.
	 */
	GetSudokuSize() SudokuSize

	/**
	 * Get the number at the given sudoku grid cell.
	 *
	 * @param row    A row (1..digit_range) in the sudoku grid
	 * @param column A column (1..digit_range) in the sudoku grid
	 */
	GetCellValue(row int, column int) int
}

/**
 * Constructor converting a set of placements to an easy-to-access sudoku
 * solution grid.
 */
func NewSolution(digitRange SudokuSize, rawSolution []*Placement) Solution {
	// create new grid to hold all placement values row-by-row
	gridSize := digitRange * digitRange

	solution := &solution{
		digitRange: digitRange,
		grid:       make([]int, gridSize),
	}

	// override with actual placement values
	for _, placement := range rawSolution {
		index := (placement.row-1)*int(digitRange) + placement.column - 1
		solution.grid[index] = placement.number
	}

	return solution
}

/**
 * Implementation for the Solution interface
 */
type solution struct {
	// The size of the solution grid
	digitRange SudokuSize

	// The solution grid itself as a one-dimensional row-by-row vector.
	grid []int
}

func (s *solution) GetSudokuSize() SudokuSize {
	return s.digitRange
}

func (s *solution) GetCellValue(row int, column int) int {
	index := (row-1)*int(s.digitRange) + column - 1
	return s.grid[index]
}
