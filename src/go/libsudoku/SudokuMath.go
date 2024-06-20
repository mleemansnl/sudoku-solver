package libsudoku

import "math"

/*******
 * The below helper methods calculate the header node index for a given Sudoku
 * condition.
 *
 * The SudokuMatrix will model the exact cover problem for a sudoku of size
 * <digit_range>. Each header column in this matrix will represent one condition
 * choice. We model these conditions in 4 groups of header columns: 1. the first
 * digit_range^2 columns represent for each cell a number is filled in 2. the
 * second digit_range^2 columns represent for each row every number between
 * 1..digit_range is filled in 3. the third digit_range^2 columns represent for
 * each column every number between 1..digit_range is filled in 4. the fourth
 * digit_range^2 columns represent for each box every number between
 * 1..digit_range is filled in
 *
 * The offset in the below helper methods are based on the above groupings.
 ******/

/**
 * Helper method to calculate the header node index for the Sudoku cell
 * condition. Condition modelled: for each cell a number is filled in
 *
 * See above description for more information.
 */
func indexForCondCell(digitRange SudokuSize, row int, column int) int {
	offset := 0
	return offset + (row-1)*int(digitRange) + (column - 1)
}

/**
 * Helper method to calculate the header node index for the Sudoku row
 * condition. Condition modelled: for each row every number between
 * 1..digit_range is filled in
 *
 * See above description for more information.
 */
func indexForCondRow(digitRange SudokuSize, row int, number int) int {
	offset := 1 * int(digitRange) * int(digitRange)
	return offset + (row-1)*int(digitRange) + (number - 1)
}

/**
 * Helper method to calculate the header node index for the Sudoku column
 * condition. Condition modelled: for each column every number between
 * 1..digit_range is filled in
 *
 * See above description for more information.
 */
func indexForCondColumn(digitRange SudokuSize, column int, number int) int {
	offset := 2 * int(digitRange) * int(digitRange)
	return offset + (column-1)*int(digitRange) + (number - 1)
}

/**
 * Helper method to calculate the header node index for the Sudoku box
 * condition. Condition modelled: for each box every number between
 * 1..digit_range is filled in
 *
 * See above description for more information.
 */
func indexForCondBox(digitRange SudokuSize, row int, column int, number int) int {
	offset := 3 * int(digitRange) * int(digitRange)

	sqrt := int(math.Floor(math.Sqrt(float64(digitRange))))
	box := (column-1)/sqrt + (row-1)/sqrt*sqrt

	return offset + box*int(digitRange) + (number - 1)
}

/**
 * Helper method to calculate the lookup index for oot/starting placement nodes.
 * This is used to lookup a row of nodes for a given (row, column, number)
 * triplet.
 */
func indexForNode(digitRange SudokuSize, row int, column int, number int) int {
	return (row-1)*int(digitRange)*int(digitRange) +
		(column-1)*int(digitRange) + (number - 1)
}
