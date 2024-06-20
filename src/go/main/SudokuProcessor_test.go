package main_test

import (
	"strings"
	"testing"

	"github.com/mleemansnl/sudoku-solver/src/go/main"
	"github.com/stretchr/testify/suite"
)

// Test Suite: SudokuProcessor
type SudokuProcessorTestSuite struct {
	suite.Suite
}

// Go run entry to testify test suite
func TestSSudokuProcessorTestSuite(t *testing.T) {
	suite.Run(t, new(SudokuProcessorTestSuite))
}

/**
 * Test: solve an example sudoku and test solution
 */
func (suite *SudokuProcessorTestSuite) TestSmallSudoku() {
	// Solve small example sudoku
	input := `4 _ _ 1
_ 1 3 _
_ 4 1 _
1 _ _ 3`
	var output strings.Builder

	result := main.ProcessSudoku(
		strings.NewReader(input),
		&output,
	)

	suite.True(result)
	suite.Equal(`4 3 2 1 
2 1 3 4 
3 4 1 2 
1 2 4 3 
`, output.String())
}
