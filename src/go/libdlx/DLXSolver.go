package libdlx

import "math"

/**
 * The Solver implements the Alogrithm X for solving an exact cover problem.
 *
 * Algorithm X is (ref: https://en.wikipedia.org/wiki/Knuth%27s_Algorithm_X) is
 * an algorithm for solving the exact cover problem, first proposed by Donald
 * Knuth in https://arxiv.org/abs/cs/0011047 . The exact cover problem is
 * represented in Algorithm X by a matrix A consisting of 0s and 1s. The goal is
 * to select a subset of the rows such that the digit 1 appears in each column
 * exactly once.
 *
 * To solve the exact cover problem, this algorithm recursively reduces matrix A
 * till a solution is found. In pseudo-code, the algorithm will
 * (https://en.wikipedia.org/wiki/Knuth%27s_Algorithm_X): 1. Check if the matrix
 * has no columns. If so, then we have found a valid solution. 2. Otherwise,
 * select a column c with the lowest count. 3. Remove (cover, see below) the
 * chosen column. 4. Select a row r where A[c,r] = 1 (i.e., a row in that
 * column): - Include row r in the candidate partial solution. - Remove (cover,
 * see below) the chosen row, as well as all other columns covered by this row.
 * 5. Recurse on the reduced matrix. 6. If the recursion did not yield a valid
 * solution: - Remove row r from the candidate solution. - Reinsert (uncover,
 * see below) the chosen row and the columns it covers. - And try again with a
 * different row (back to step 4).
 *
 * This algorithm uses a the Dancing Links technique implemented by \link
 * DancingLinkNode and \link DancingLinksMatrix to efficiently find an exact
 * cover solution. The Dancing Links technique relies on two key operations: -
 * \link Solver::cover() The cover operation efficiently removes a row from the
 * matrix upon including such a row in a candidate partial solution. Cover
 * relies on the efficient O(1) remove operations in \link DancingLinkNode. -
 * \link Solver::uncover() The uncover operation efficiently reinserts a row to
 * the matrix when the algorithm backtracks to select a different row for a
 * candidate partial solution. Uncover relies on the efficient O(1) reinsert
 * operations in \link DancingLinkNode.
 */
type DLXSolver interface {

	/**
	 * Solve for the provided matrix using an Alogrithm X implementation.
	 *
	 * @return If no solution is found, (_, false) is returned.
	 * 		   If a solution is found, (list, true) is returned.
	 */
	Solve() ([]DancingLinkNode, bool)

	/**
	 * Cover a given row, marking it as predetermined input to the exact cover
	 * solution.
	 *
	 * Before finding a solution, one can setup a partial solution to use as a
	 * starting point.
	 */
	CoverRow(DancingLinkNode)
}

/**
 * Constructs a new Solver based on the given matrix.
 *
 * @param matrix A matrix modelling the exact cover problem to be solved.
 */
func NewDLXSolver(m DancingLinksMatrix) DLXSolver {
	return &dlxSolver{
		matrix:   m,
		solution: make([]DancingLinkNode, 0),
	}
}

/**
 * Implementation for the DLXSolver interface
 */
type dlxSolver struct {
	// The matrix modelling the exact cover problem
	matrix DancingLinksMatrix

	// The (partial) solution constructed during solving
	solution []DancingLinkNode
}

func (s *dlxSolver) Solve() ([]DancingLinkNode, bool) {
	if s.search() {
		return s.solution, true
	}

	return nil, false
}

func (s *dlxSolver) CoverRow(rowRoot DancingLinkNode) {
	// Add placement to solution
	s.pushSolution(rowRoot)

	// Cover every column in this row
	dlxCover(rowRoot.GetHeader())
	for node := rowRoot.GetRight(); node != rowRoot; node = node.GetRight() {
		dlxCover(node.GetHeader())
	}
}

/**
 * Algorithm X Step function in the solving algorithm.
 *
 * This function implements one recursive call, implementing the steps
 * documented above and calling search() recursively till a solution is found.
 *
 * @return if a solution has been found
 * @post if return is true, then this.solution holds a valid solution.
 */
func (s *dlxSolver) search() bool {
	root := s.matrix.GetRoot()

	// check if there are condition columns left.
	// if not, then we found a valid solution
	if root.GetRight() == root {
		// no columns left, so we have a valid solution
		// the solution is already stored in solution as part of the cover actions
		return true
	}

	// no solution found, so we continue our search
	// optimization: find column with lowest count
	targetHeader, foundHeader := s.selectHeaderColumn()

	// check if this is a good solution
	if !foundHeader {
		return false
	}

	// cover this header column
	dlxCover(targetHeader)

	// try every placement part of this column
	// add it to the solution, recurse solve the smaller matrix,
	// and backtrack if no good solution was found
	for row := targetHeader.GetDown(); row != targetHeader; row = row.GetDown() {
		// try solution with current row
		s.pushSolution(row)

		// cover all other columns in this row
		for node := row.GetRight(); node != row; node = node.GetRight() {
			dlxCover(node.GetHeader())
		}

		// recursive search with this row in the solution and all it's columns covered
		if s.search() {
			return true
		}

		// remove row from solution, so we may try with the next row
		s.popSolution()

		// uncover all columns in row
		for node := row.GetLeft(); node != row; node = node.GetLeft() {
			dlxUncover(node.GetHeader())
		}

	}

	// uncover column
	dlxUncover(targetHeader)

	return false
}

/**
 * Add the given node to the end of the solver's solution slice
 */
func (s *dlxSolver) pushSolution(n DancingLinkNode) {
	s.solution = append(s.solution, n)
}

/**
 * Remove the last node from the solver's solution slice
 */
func (s *dlxSolver) popSolution() {
	s.solution = s.solution[:len(s.solution)-1]
}

/**
 * Helper method to find the column c with the lowest count. Used during the
 * search() step to deterministically select the next column to cover.
 * @returns (header, true) if a suitable header is found, (nil, false) otherwise
 */
func (s *dlxSolver) selectHeaderColumn() (DancingLinkHeader, bool) {
	root := s.matrix.GetRoot()

	// find column with lowest count
	var targetHeader DancingLinkHeader = nil
	minCount := math.MaxInt32

	for header := root.GetRight().GetHeader(); header != root; header = header.GetRight().GetHeader() {
		// check if the current header has a lower count
		// than the currently found targetHeader
		if header.GetCount() < minCount {
			minCount = header.GetCount()
			targetHeader = header
		}
	}

	if targetHeader != nil {
		return targetHeader, true
	}

	return nil, false
}

/**
 * The cover operation efficiently removes a row from the matrix upon including
 * such a row in a candidate partial solution. Cover relies on the efficient
 * O(1) remove operations in \link DancingLinkNode.
 */
func dlxCover(header DancingLinkHeader) {

	// remove column header from the matrix
	// indicating this condition is satisfied
	header.RemoveHorizontal()

	// remove all rows in this column from other columns they are in
	// indicating this is no longer a way to satisfy those conditions
	for row := header.GetDown(); row != header; row = row.GetDown() {
		for node := row.GetRight(); node != row; node = node.GetRight() {
			node.RemoveVertical()
			node.GetHeader().DecCount()
		}
	}
}

/**
 * The uncover operation efficiently reinserts a row to the matrix when the
 * algorithm backtracks to select a different row for a candidate partial
 * solution. Uncover relies on the efficient O(1) reinsert operations in \link
 * DancingLinkNode.
 */
func dlxUncover(header DancingLinkHeader) {

	// put back all rows in the column into other columns they were in
	for row := header.GetUp(); row != header; row = row.GetUp() {
		for node := row.GetLeft(); node != row; node = node.GetLeft() {
			node.ReinsertVertical()
			node.GetHeader().IncCount()
		}
	}

	// put back column into the matrix
	header.ReinsertHorizontal()
}
