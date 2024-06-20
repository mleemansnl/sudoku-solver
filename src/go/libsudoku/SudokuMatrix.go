package libsudoku

import "github.com/mleemansnl/sudoku-solver/src/go/libdlx"

type SudokuMatrix interface {
	// A SudokuMatrix also embeds a DancingLinksMatrix
	libdlx.DancingLinksMatrix

	/**
	 * Get the size of the solution grid.
	 */
	GetSudokuSize() SudokuSize

	/**
	 * Get the DancingLinkNode row node corresponding to a given placement.
	 */
	GetNodeRow(row, column, number int) libdlx.DancingLinkNode
}

/**
 * Constructs a new sparse matrix modelling a Sudoku problem of the given size.
 */
func NewSudokuMatrix(size SudokuSize) SudokuMatrix {
	matrix := &sudokuMatrix{
		DancingLinksMatrix: libdlx.NewDancingLinksMatrix(),
		sudokuSize:         size,
	}

	numDigits := int(size)

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
	numConditions := numDigits * numDigits * typesOfConditions

	// We will use this helper data-structure to quickly lookup column headers by
	// index
	// This is used while filling the matrix with nodes matching the correct column
	columnByIndex := make([]libdlx.DancingLinkHeader, 0, numConditions)

	// Create every column header
	for i := 0; i < numConditions; i++ {
		columnByIndex = append(columnByIndex, matrix.DancingLinksMatrix.NewHeader())
	}

	// Next, we fill the matrix with nodes, representing every possible digit in
	// every cell
	// The total number of nodes to model equals:
	// numNodes = digits * digits * digits
	// = 9 * 9 * 9 = 729 for a typical sudoku of size digits=9
	numNodes := numDigits * numDigits * numDigits

	// We use this helper data-structure to quickly lookup root/starting placement
	// nodes by index
	// This will be used to lookup a row of nodes for a given (row, column, number)
	// triplet.
	matrix.index2node = make([]libdlx.DancingLinkNode, numNodes)

	// Create every node and add to the vertical list of the right column headers
	for row := 1; row <= numDigits; row++ {
		for column := 1; column <= numDigits; column++ {
			for number := 1; number <= numDigits; number++ {
				// Mark for condition: cell, row, column, and box
				nodeCell := columnByIndex[indexForCondCell(size, row, column)].NewNode()
				nodeRow := columnByIndex[indexForCondRow(size, row, number)].NewNode()
				nodeColumn := columnByIndex[indexForCondColumn(size, column, number)].NewNode()
				nodeBox := columnByIndex[indexForCondBox(size, row, column, number)].NewNode()

				// Define matrix row
				libdlx.MakeRow(nodeCell, nodeRow, nodeColumn, nodeBox)

				// Add Placement meta data so that after finding a solution,
				// we can reinterpret the DancingLinkNodes as values in a Sudoku grid
				data := &Placement{row, column, number}
				nodeCell.SetData(data)
				nodeRow.SetData(data)
				nodeColumn.SetData(data)
				nodeBox.SetData(data)

				matrix.index2node[indexForNode(size, row, column, number)] = nodeCell
			}
		}
	}

	// Now we have our sparse matrix fully constructed.
	//
	//                         ^                       ^
	//                         v                       v
	//  [this->root] <-> [condition 1] <-> ... <-> [condition n] <->
	//                         ^                       ^
	//                         v                       |
	//                  <-> [1,1,1] <-> [1,1,1] <->    |
	//                         ^                       v
	//                         v   <-> [9,9,9] <-> [9,9,9] <->
	//                        ....                     ^
	//                                                 v

	return matrix
}

/**
 * For a typical sudoku of size SudokuSize=9, there are 4 types of conditions to
 * check for: 1. in each cell has exactly one value between 1 and 9 2. in each
 * row, each digit may appear only once 3. in each column, each digit may appear
 * only once 4. in each box of 3x3, each digit may appear only once
 */
const typesOfConditions int = 4

/**
 * Implementation for the SudokuMatrix interface
 */
type sudokuMatrix struct {
	// The internal DLX Matrix to hold the Sudoku Exact Cover model
	libdlx.DancingLinksMatrix

	// The size of the solution grid
	sudokuSize SudokuSize

	// Lookup vector for placement nodes by index.
	// This is used to lookup a row of nodes for a given (row, column, number)
	// triplet.
	index2node []libdlx.DancingLinkNode
}

func (m *sudokuMatrix) GetSudokuSize() SudokuSize {
	return m.sudokuSize
}

func (m *sudokuMatrix) GetNodeRow(row, column, number int) libdlx.DancingLinkNode {
	return m.index2node[indexForNode(m.sudokuSize, row, column, number)]
}
