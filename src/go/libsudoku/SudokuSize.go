package libsudoku

/**
 * SudokuSize predefines valid possible digit range sizes for a valid sudoku
 * grid.
 *
 * Note that each valid value is a perfect square number, a property needed to
 * correctly model the sudoku grid conditions.
 */
type SudokuSize int

const (
	// A Sudoku of 4 x 4, using numbers 1..4 and having boxes of 2x2
	SudokuFour SudokuSize = 4

	// A Sudoku of 9 x 9, using numbers 1..9 and having boxes of 3x3
	SudokuNine SudokuSize = 9

	// A Sudoku of 16 x 16, using numbers 1..16 (often printed as 0..F)
	// and having boxes of 4x4
	SudokuSixteen SudokuSize = 16
)
