package libsudoku_test

import (
	"testing"

	"github.com/mleemansnl/sudoku-solver/src/go/libsudoku"
	"github.com/stretchr/testify/suite"
)

// Test Suite: SudokuSolver
type SudokuSolverTestSuite struct {
	suite.Suite
}

// Go run entry to testify test suite
func TestSudokuSolverTestSuite(t *testing.T) {
	suite.Run(t, new(SudokuSolverTestSuite))
}

/**
 * Test: solve an example sudoku and test solution
 */
func (suite *SudokuSolverTestSuite) TestSmallSudoku() {
	// Solve small example sudoku
	solver := createSmallSudoku()
	solution, hasSolution := solver.Solve()

	// Check solution
	suite.True(hasSolution)
	if hasSolution {
		// Expected solution:
		// 4 3 2 1
		// 2 1 3 4
		// 3 4 1 2
		// 1 2 4 3

		// We expect a solution for a 4 by 4 sudoku
		suite.Equal(libsudoku.SudokuFour, solution.GetSudokuSize())

		// check the value of a preset input: in row 1, column 1, the value should be 4
		suite.Equal(4, solution.GetCellValue(1, 1))

		// in row 1, column 2 the correct value is 3
		suite.Equal(3, solution.GetCellValue(1, 2))

	}
}

func createSmallSudoku() libsudoku.SudokuSolver {
	// Setup the matrix for the following Sudoku:
	// 4 _ _ 1
	// _ 1 3 _
	// _ 4 1 _
	// 1 _ _ 3

	// Define input
	matrix := libsudoku.NewSudokuMatrix(libsudoku.SudokuFour)
	solver := libsudoku.NewSudokuSolver(matrix)

	solver.SetInput(1, 1, 4)
	solver.SetInput(1, 4, 1)

	solver.SetInput(2, 2, 1)
	solver.SetInput(2, 3, 3)

	solver.SetInput(3, 2, 4)
	solver.SetInput(3, 3, 1)

	solver.SetInput(4, 1, 1)
	solver.SetInput(4, 4, 3)

	return solver
}
