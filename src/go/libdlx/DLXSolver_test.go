package libdlx_test

import (
	"testing"

	"github.com/mleemansnl/sudoku-solver/src/go/libdlx"
	"github.com/stretchr/testify/suite"
)

// Test Suite: DLXSolver
type DLXSolverTestSuite struct {
	suite.Suite
}

// Go run entry to testify test suite
func TestDLXSolverTestSuite(t *testing.T) {
	suite.Run(t, new(DLXSolverTestSuite))
}

/**
 * Test: Solver can solve basic exact cover example problem.
 */
func (suite *DLXSolverTestSuite) TestBasic() {
	// Solve the Basic Example from https://en.wikipedia.org/wiki/Exact_cover

	// Define matrix
	matrix := libdlx.NewDancingLinksMatrix()

	// These headers represent covering X = {1,2,3,4}
	header1 := matrix.NewHeader()
	header2 := matrix.NewHeader()
	header3 := matrix.NewHeader()
	header4 := matrix.NewHeader()

	// These rows represent subcollections S = {O,P,E} , where:
	// O = {1,3}
	node_o1 := header1.NewNode()
	node_o3 := header3.NewNode()
	libdlx.MakeRow(node_o1, node_o3)

	// P = {1,2,3}
	node_p1 := header1.NewNode()
	node_p2 := header2.NewNode()
	node_p3 := header3.NewNode()
	libdlx.MakeRow(node_p1, node_p2, node_p3)

	// E = {2,4}
	node_e2 := header2.NewNode()
	node_e4 := header4.NewNode()
	libdlx.MakeRow(node_e2, node_e4)

	// Solve for the above matrix
	solver := libdlx.NewDLXSolver(matrix)
	solution, hasSolution := solver.Solve()

	// Check solution
	suite.True(hasSolution)
	if hasSolution {
		suite.Equal(2, len(solution))
		suite.Equal(node_o1, solution[1])
		suite.Equal(node_e4, solution[0])
	}
}
