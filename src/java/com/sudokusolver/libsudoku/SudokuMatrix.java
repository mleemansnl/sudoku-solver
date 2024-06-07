package com.sudokusolver.libsudoku;

import java.util.ArrayList;
import java.util.List;

import com.sudokusolver.libdlx.DancingLinkHeader;
import com.sudokusolver.libdlx.DancingLinkNode;
import com.sudokusolver.libdlx.DancingLinksMatrix;

/**
 * The SudokuMatrix represents a sparse matrix capturing a Sudoku problem.
 *
 * This sparse matrix models the Sudoku conditions (rules of the game) on the
 * headers and possible placements (numbers in specific Sudoku cells) on the
 * rows. A subset of placements only yields a valid Sudoku solution if all the
 * Sudoku conditions are met (i.e., form an exact cover).
 *
 * The \link Solver class can be used to update this matrix such that on a given
 * row and column, a given number is pre-filled as input.
 */
public class SudokuMatrix extends DancingLinksMatrix {

    /**
     * Constructs a new sparse matrix modelling a Sudoku problem of the given size.
     */
    public SudokuMatrix(SudokuSize size) {
        this.sudokuSize = size;
        int numDigits = size.getDigitRange();

        /**
         * This matrix will model the exact cover problem for a sudoku of size
         * <sudoku_size> The matrix is represented as a sparse matrix structure and
         * implemented using the dancing link nodes as doubly-linked lists.
         *
         * Each column in this matrix will represent one condition choice.
         *
         * We model these conditions in 4 groups of header columns: 1. the first
         * digit_range^2 columns represent for each cell a number is filled in 2. the
         * second digit_range^2 columns represent for each row every number between
         * 1..digit_range is filled in 3. the third digit_range^2 columns represent for
         * each column every number between 1..digit_range is filled in 4. the fourth
         * digit_range^2 columns represent for each box every number between
         * 1..digit_range is filled in Therefore, the total number of conditions to
         * model equals: numConditions = num_digits * num_digits * TypesOfConditions = 9
         * * 9 * 4 = 324 for a typical sudoku of size num_digits=9
         *
         */
        final int numConditions = numDigits * numDigits * TypesOfConditions;

        // We will use this helper data-structure to quickly lookup column headers by
        // index
        // This is used while filling the matrix with nodes matching the correct column
        List<DancingLinkHeader> columnByIndex = new ArrayList<>(numConditions);

        // Create every column header
        for (int i = 0; i < numConditions; i++) {
            columnByIndex.add(i, this.newHeader());
        }

        // Next, we fill the matrix with nodes, representing every possible digit in
        // every cell
        // The total number of nodes to model equals:
        // numNodes = digits * digits * digits
        // = 9 * 9 * 9 = 729 for a typical sudoku of size digits=9
        final int numNodes = numDigits * numDigits * numDigits;

        // We use this helper data-structure to quickly lookup root/starting placement
        // nodes by index
        // This will be used to lookup a row of nodes for a given (row, column, number)
        // triplet.
        this.index2node = new ArrayList<>(numNodes);

        // Create every node and add to the vertical list of the right column headers
        for (int row = 1; row <= numDigits; row++) {
            for (int column = 1; column <= numDigits; column++) {
                for (int number = 1; number <= numDigits; number++) {
                    // Mark for condition: cell, row, column, and box
                    DancingLinkNode nodeCell = columnByIndex.get(SudokuMath.indexForCondCell(size, row, column))
                            .newNode();
                    DancingLinkNode nodeRow = columnByIndex.get(SudokuMath.indexForCondRow(size, row, number))
                            .newNode();
                    DancingLinkNode nodeColumn = columnByIndex.get(SudokuMath.indexForCondColumn(size, column, number))
                            .newNode();
                    DancingLinkNode nodeBox = columnByIndex.get(SudokuMath.indexForCondBox(size, row, column, number))
                            .newNode();

                    // Define matrix row
                    DancingLinkNode.makeRow(nodeCell, nodeRow, nodeColumn, nodeBox);

                    // Add Placement meta data so that after finding a solution,
                    // we can reinterpret the DancingLinkNodes as values in a Sudoku grid
                    nodeCell.setData(new Placement(row, column, number));
                    nodeRow.setData(new Placement(row, column, number));
                    nodeColumn.setData(new Placement(row, column, number));
                    nodeBox.setData(new Placement(row, column, number));

                    this.index2node.add(SudokuMath.indexForNode(size, row, column, number), nodeCell);
                }
            }
        }

        // Now we have our sparse matrix fully constructed.
        //
        // ^ ^
        // v v
        // [this->root] <-> [condition 1] <-> ... <-> [condition n] <->
        // ^ ^
        // v |
        // <-> [1,1,1] <-> [1,1,1] <-> |
        // ^ v
        // v <-> [9,9,9] <-> [9,9,9] <->
        // .... ^
        // v

    }

    /**
     * Get the size of the solution grid.
     */
    public SudokuSize getSudokuSize() {
        return this.sudokuSize;
    }

    /**
     * Get the DancingLinkNode row node corresponding to a given placement.
     */
    public DancingLinkNode getNodeRow(int row, int column, int number) {
        return this.index2node.get(SudokuMath.indexForNode(this.sudokuSize, row, column, number));
    }

    /**
     * For a typical sudoku of size SudokuSize=9, there are 4 types of conditions to
     * check for: 1. in each cell has exactly one value between 1 and 9 2. in each
     * row, each digit may appear only once 3. in each column, each digit may appear
     * only once 4. in each box of 3x3, each digit may appear only once
     */
    private static int TypesOfConditions = 4;

    // The size of the solution grid
    private SudokuSize sudokuSize;

    // Lookup vector for placement nodes by index.
    // This is used to lookup a row of nodes for a given (row, column, number)
    // triplet.
    private List<DancingLinkNode> index2node;
}
