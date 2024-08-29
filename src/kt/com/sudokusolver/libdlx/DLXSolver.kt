package com.sudokusolver.libdlx

/**
 * The Solver implements the Alogrithm X for solving an exact cover problem.
 *
 * Algorithm X is (ref: https://en.wikipedia.org/wiki/Knuth%27s_Algorithm_X) is an algorithm for
 * solving the exact cover problem, first proposed by Donald Knuth in
 * https://arxiv.org/abs/cs/0011047 . The exact cover problem is represented in Algorithm X by a
 * matrix A consisting of 0s and 1s. The goal is to select a subset of the rows such that the digit
 * 1 appears in each column exactly once.
 *
 * To solve the exact cover problem, this algorithm recursively reduces matrix A till a solution is
 * found. In pseudo-code, the algorithm will (https://en.wikipedia.org/wiki/Knuth%27s_Algorithm_X):
 * 1. Check if the matrix has no columns. If so, then we have found a valid solution. 2. Otherwise,
 * select a column c with the lowest count. 3. Remove (cover, see below) the chosen column. 4.
 * Select a row r where A[c,r] = 1 (i.e., a row in that column): - Include row r in the candidate
 * partial solution. - Remove (cover, see below) the chosen row, as well as all other columns
 * covered by this row.
 * 5. Recurse on the reduced matrix. 6. If the recursion did not yield a valid solution: - Remove
 * row r from the candidate solution. - Reinsert (uncover, see below) the chosen row and the columns
 * it covers. - And try again with a different row (back to step 4).
 *
 * This algorithm uses a the Dancing Links technique implemented by \link DancingLinkNode and \link
 * DancingLinksMatrix to efficiently find an exact cover solution. The Dancing Links technique
 * relies on two key operations: - \link Solver::cover() The cover operation efficiently removes a
 * row from the matrix upon including such a row in a candidate partial solution. Cover relies on
 * the efficient O(1) remove operations in \link DancingLinkNode. - \link Solver::uncover() The
 * uncover operation efficiently reinserts a row to the matrix when the algorithm backtracks to
 * select a different row for a candidate partial solution. Uncover relies on the efficient O(1)
 * reinsert operations in \link DancingLinkNode.
 */
public class DLXSolver(initMatrix: DancingLinksMatrix) {

    // The matrix modelling the exact cover problem
    private val matrix: DancingLinksMatrix = initMatrix

    // The (partial) solution constructed during solving
    private val solution: MutableList<DancingLinkNode> = mutableListOf()

    /**
     * Solve for the provided matrix using an Alogrithm X implementation.
     *
     * @return If no solution is found, Optional.empty() is returned. If a solution is found, a
     * Solution object is returned.
     */
    fun solve(): List<DancingLinkNode>? {
        if (this.search()) {
            return this.solution
        }

        return null
    }

    /**
     * Cover a given row, marking it as predetermined input to the exact cover solution.
     *
     * Before finding a solution, one can setup a partial solution to use as a starting point.
     */
    fun coverRow(rowRoot: DancingLinkNode) {
        // Add placement to solution
        this.solution.add(rowRoot)

        // Cover every column in this row
        rowRoot.header?.let { DLXSolver.cover(it) }
        var node = rowRoot.right
        while (node !== rowRoot) {
            node.header?.let { DLXSolver.cover(it) }

            node = node.right // advance iterator
        }
    }

    /**
     * Algorithm X Step function in the solving algorithm.
     *
     * This function implements one recursive call, implementing the steps documented above and
     * calling search() recursively till a solution is found.
     *
     * @return if a solution has been found
     * @post if return is true, then this.solution holds a valid solution.
     */
    fun search(): Boolean {
        val root = this.matrix.root

        // check if there are condition columns left.
        // if not, then we found a valid solution
        if (root.right === root) {
            // no columns left, so we have a valid solution
            // the solution is already stored in solution as part of the cover actions
            return true;
        }

        // no solution found, so we continue our search
        // optimization: find column with lowest count
        val targetHeader = this.selectHeaderColumn()

        // check if this is a good solution
        if (targetHeader === null) {
            return false;
        }

        // cover this header column
        DLXSolver.cover(targetHeader)
        // try every placement part of this column
        // add it to the solution, recurse solve the smaller matrix,
        // and backtrack if no good solution was found
        var row = targetHeader.down
        while (row !== targetHeader) {
            // try solution with current row
            this.solution.add(row)

            // cover all other columns in this row
            var node = row.right
            while (node !== row) {
                node.header?.let { DLXSolver.cover(it) }

                node = node.right // advance iterator
            }

            // recursive search with this row in the solution and all it's columns covered
            if (this.search()) {
                return true;
            }

            // remove row from solution, so we may try with the next row
            this.solution.removeAt(this.solution.size - 1)

            // uncover all columns in row
            node = row.left
            while (node !== row) {
                node.header?.let { DLXSolver.uncover(it) }

                node = node.left // advance iterator
            }

            // advance iterator
            row = row.down
        }

        // uncover column
        DLXSolver.uncover(targetHeader)

        return false
    }

    /**
     * Helper method to find the column c with the lowest count. Used during the
     * search() step to deterministically select the next column to cover.
     */
    fun selectHeaderColumn(): DancingLinkHeader? {
        val root = this.matrix.root

        // find column with lowest count
        var targetHeader: DancingLinkHeader?  = null;
        var minCount = Int.MAX_VALUE

        var header = root.right.header
        while (header !== null && header !== root) {
            // check if the current header has a lower count
            // than the currently found targetHeader
            if (header.count < minCount) {
                minCount = header.count
                targetHeader = header
            }

            // advance iterator
            header = header.right.header
        }
        
        return targetHeader
    }

    companion object {
        /**
         * The cover operation efficiently removes a row from the matrix upon including such a row
         * in a candidate partial solution. Cover relies on the efficient O(1) remove operations in
         * \link DancingLinkNode.
         */
        private fun cover(header: DancingLinkHeader) {
            // remove column header from the matrix
            // indicating this condition is satisfied
            header.removeHorizontal()

            // remove all rows in this column from other columns they are in
            // indicating this is no longer a way to satisfy those conditions
            var row = header.down
            while (row !== header) {
                var node = row.right
                while (node !== row) {
                    node.removeVertical()
                    node.header?.decCount()

                    node = node.right // advance iterator
                }
                row = row.down // advance iterator
            }
        }

        /**
         * The uncover operation efficiently reinserts a row to the matrix when the
         * algorithm backtracks to select a different row for a candidate partial
         * solution. Uncover relies on the efficient O(1) reinsert operations in \link
         * DancingLinkNode.
         */
        fun uncover(header: DancingLinkHeader) {

            // put back all rows in the column into other columns they were in
            var row = header.up
            while (row !== header) {
                var node = row.left
                while (node !== row) {
                    node.reinsertVertical()
                    node.header?.incCount()

                    node = node.left // advance iterator
                }
                row = row.up // advance iterator
            }

            // put back column into the matrix
            header.reinsertHorizontal()
        }
    }

}
