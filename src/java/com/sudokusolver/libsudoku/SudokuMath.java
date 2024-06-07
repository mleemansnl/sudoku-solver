package com.sudokusolver.libsudoku;

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
public final class SudokuMath {

    /**
     * This is an utility class, so disable instantiation with a private
     * constructor.
     */
    private SudokuMath() {
    }

    /**
     * Helper method to calculate the header node index for the Sudoku cell
     * condition. Condition modelled: for each cell a number is filled in
     *
     * See above description for more information.
     */
    public static int indexForCondCell(SudokuSize digitRange, int row, int column) {
        int offset = 0;
        return offset + (row - 1) * digitRange.getDigitRange() + (column - 1);
    }

    /**
     * Helper method to calculate the header node index for the Sudoku row
     * condition. Condition modelled: for each row every number between
     * 1..digit_range is filled in
     *
     * See above description for more information.
     */
    public static int indexForCondRow(SudokuSize digitRange, int row, int number) {
        int offset = 1 * digitRange.getDigitRange() * digitRange.getDigitRange();
        return offset + (row - 1) * digitRange.getDigitRange() + (number - 1);
    }

    /**
     * Helper method to calculate the header node index for the Sudoku column
     * condition. Condition modelled: for each column every number between
     * 1..digit_range is filled in
     *
     * See above description for more information.
     */
    public static int indexForCondColumn(SudokuSize digitRange, int column, int number) {
        int offset = 2 * digitRange.getDigitRange() * digitRange.getDigitRange();
        return offset + (column - 1) * digitRange.getDigitRange() + (number - 1);
    }

    /**
     * Helper method to calculate the header node index for the Sudoku box
     * condition. Condition modelled: for each box every number between
     * 1..digit_range is filled in
     *
     * See above description for more information.
     */
    public static int indexForCondBox(SudokuSize digitRange, int row, int column, int number) {
        int offset = 3 * digitRange.getDigitRange() * digitRange.getDigitRange();

        int sqrt = (int) Math.floor(Math.sqrt((double) digitRange.getDigitRange()));
        int box = (column - 1) / sqrt + (row - 1) / sqrt * sqrt;

        return offset + box * digitRange.getDigitRange() + (number - 1);
    }

    /**
     * Helper method to calculate the lookup index for oot/starting placement nodes.
     * This is used to lookup a row of nodes for a given (row, column, number)
     * triplet.
     */
    public static int indexForNode(SudokuSize digitRange, int row, int column, int number) {
        return (row - 1) * digitRange.getDigitRange() * digitRange.getDigitRange()
                + (column - 1) * digitRange.getDigitRange() + (number - 1);
    }

}
