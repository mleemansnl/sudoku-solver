package com.sudokusolver.libdlx;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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
public class Solver {

    /**
     * Constructs a new Solver based on the given matrix.
     *
     * \param matrix A matrix modelling the exact cover problem to be solved. The
     * solver owns the memory of this matrix after construction.
     */
    public Solver(DancingLinksMatrix matrix) {
        this.matrix = matrix;
        this.solution = new ArrayList<>();
    }

    /**
     * Solve for the provided matrix using an Alogrithm X implementation.
     *
     * \return If no solution is found, std::nullopt is returned. If a solution is
     * found, a Solution object is returned.
     */
    public Optional<List<DancingLinkNode>> solve() {
        if (this.search()) {
            return Optional.of(this.solution);
        }

        return Optional.empty();
    }

    /**
     * Cover a given row, marking it as predetermined input to the exact cover
     * solution.
     *
     * Before finding a solution, one can setup a partial solution to use as a
     * starting point.
     */
    public void coverRow(DancingLinkNode row_root) {
        // Add placement to solution
        this.solution.add(row_root);

        // Cover every column in this row
        Solver.cover(row_root.getHeader());
        for (DancingLinkNode node = row_root.getRight(); node != row_root; node = node.getRight()) {
            Solver.cover(node.getHeader());
        }
    }

    // The matrix modelling the exact cover problem
    private DancingLinksMatrix matrix;

    // The (partial) solution constructed during solving
    private List<DancingLinkNode> solution;

    /**
     * Algorithm X Step function in the solving algorithm.
     *
     * This function implements one recursive call, implementing the steps
     * documented above and calling search() recursively till a solution is found.
     *
     * \return if a solution has been found \post if return is true, then
     * this->solution holds a valid solution.
     */
    private boolean search() {
        DancingLinkHeader root = this.matrix.getRoot();

        // check if there are condition columns left.
        // if not, then we found a valid solution
        if (root.getRight() == root) {
            // no columns left, so we have a valid solution
            // the solution is already stored in solution as part of the cover actions
            return true;
        }

        // no solution found, so we continue our search
        // optimization: find column with lowest count
        Optional<DancingLinkHeader> opt_header = this.selectHeaderColumn();

        // check if this is a good solution
        if (opt_header.isEmpty()) {
            return false;
        }

        DancingLinkHeader target_header = opt_header.get();

        // cover this header column
        Solver.cover(target_header);

        // try every placement part of this column
        // add it to the solution, recurse solve the smaller matrix,
        // and backtrack if no good solution was found
        for (DancingLinkNode row = target_header.getDown(); row != target_header; row = row.getDown()) {
            // try solution with current row
            this.solution.add(row);

            // cover all other columns in this row
            for (DancingLinkNode node = row.getRight(); node != row; node = node.getRight()) {
                Solver.cover(node.getHeader());
            }

            // recursive search with this row in the solution and all it's columns covered
            if (this.search()) {
                return true;
            }

            // remove row from solution, so we may try with the next row
            this.solution.remove(this.solution.size() - 1);

            // uncover all columns in row
            for (DancingLinkNode node = row.getLeft(); node != row; node = node.getLeft()) {
                Solver.uncover(node.getHeader());
            }
        }

        // uncover column
        Solver.uncover(target_header);

        return false;
    }

    /**
     * Helper method to find the column c with the lowest count. Used during the
     * search() step to deterministically select the next column to cover.
     */
    private Optional<DancingLinkHeader> selectHeaderColumn() {
        DancingLinkHeader root = this.matrix.getRoot();

        // find column with lowest count
        DancingLinkHeader target_header = null;
        int minCount = Integer.MAX_VALUE;

        for (DancingLinkHeader header = root.getRight().getHeader(); header != root; header = header.getRight()
                .getHeader()) {
            // check if the current header has a lower count
            // than the currently found targetHeader
            if (header.getCount() < minCount) {
                minCount = header.getCount();
                target_header = header;
            }
        }

        if (target_header != null) {
            return Optional.of(target_header);
        }

        return Optional.empty();
    }

    /**
     * The cover operation efficiently removes a row from the matrix upon including
     * such a row in a candidate partial solution. Cover relies on the efficient
     * O(1) remove operations in \link DancingLinkNode.
     */
    static private void cover(DancingLinkHeader header) {

        // remove column header from the matrix
        // indicating this condition is satisfied
        header.removeHorizontal();

        // remove all rows in this column from other columns they are in
        // indicating this is no longer a way to satisfy those conditions
        for (DancingLinkNode row = header.getDown(); row != header; row = row.getDown()) {
            for (DancingLinkNode node = row.getRight(); node != row; node = node.getRight()) {
                node.removeVertical();
                node.getHeader().decCount();
            }
        }
    }

    /**
     * The uncover operation efficiently reinserts a row to the matrix when the
     * algorithm backtracks to select a different row for a candidate partial
     * solution. Uncover relies on the efficient O(1) reinsert operations in \link
     * DancingLinkNode.
     */
    static private void uncover(DancingLinkHeader header) {

        // put back all rows in the column into other columns they were in
        for (DancingLinkNode row = header.getUp(); row != header; row = row.getUp()) {
            for (DancingLinkNode node = row.getLeft(); node != row; node = node.getLeft()) {
                node.reinsertVertical();
                node.getHeader().incCount();
            }
        }

        // put back column into the matrix
        header.reinsertHorizontal();
    }
}
