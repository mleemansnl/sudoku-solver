package libsudoku

import "github.com/mleemansnl/sudoku-solver/src/go/libdlx"

/**
 * This Solver takes an input SudokuMatrix and solves to find a valid Sudoku
 * Solution.
 *
 * This Solver wraps dlx::Solver and provides Sudoku-specific convenciences: -
 * The setInput() method allows to update the SudokuMatrix such that on the
 * given row and column, the given number is pre-filled as input. - The solve()
 * method converts a found solution from the raw exact cover dlx::Solution to a
 * Sudoku Solution object.
 */
type SudokuSolver interface {
	/**
	    * Solve for the provided matrix using the dlx::Solver.
	    *
	    * @return If no solution is found, (_, falkse) is returned.
		 * 		   If a solution is found, a Sudoku Solution object is returned.
	*/
	Solve() (Solution, bool)

	/**
	 * Update the SudokuMatrix such that on the given row and column, the given
	 * number is pre-filled as input.
	 */
	SetInput(row, column, number int)
}

func NewSudokuSolver(matrix SudokuMatrix) SudokuSolver {
	return &sudokuSolver{
		matrix: matrix,
		solver: libdlx.NewDLXSolver(matrix),
	}
}

type sudokuSolver struct {
	// The matrix modelling the Sudoku problem
	matrix SudokuMatrix

	// The internal solver
	solver libdlx.DLXSolver
}

func (s *sudokuSolver) Solve() (Solution, bool) {
	if rawSolution, hasSolution := s.solver.Solve(); hasSolution {
		// convert internal solution to list of placement values
		placements := make([]*Placement, 0, len(rawSolution))

		for _, node := range rawSolution {
			placements = append(placements, node.GetData().(*Placement))
		}

		return NewSolution(s.matrix.GetSudokuSize(), placements), true
	}

	return nil, false
}

func (s *sudokuSolver) SetInput(row, column, number int) {
	s.solver.CoverRow(s.matrix.GetNodeRow(row, column, number))
}
