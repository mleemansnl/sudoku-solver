package com.sudokusolver.libsudoku;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.sudokusolver.libdlx.DancingLinkNode;
import com.sudokusolver.libdlx.DLXSolver;

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
public class SudokuSolver {

    /**
     * Construsts a new Solver based on the given Sudoku matrix.
     *
     * @param matrix A matrix modelling the input Sudoku to be solved.
     */
    public SudokuSolver(SudokuMatrix matrix) {
        this.matrix = matrix;
        this.solver = new DLXSolver(matrix);
    }

    /**
     * Solve for the provided matrix using the dlx::Solver.
     *
     * @return If no solution is found, std::nullopt is returned. If a solution is
     *         found, a Sudoku Solution object is returned.
     */
    public Optional<Solution> solve() {
        Optional<List<DancingLinkNode>> optRawSolution = this.solver.solve();

        // check if a solution was found
        if (optRawSolution.isPresent()) {
            // convert internal solution to list of placement values
            List<DancingLinkNode> rawSolution = optRawSolution.get();
            List<Placement> listPlacements = new ArrayList<>(rawSolution.size());

            for (DancingLinkNode node : rawSolution) {
                listPlacements.add((Placement) node.getData());
            }

            return Optional.of(new Solution(this.matrix.getSudokuSize(), listPlacements));
        }

        return Optional.empty();
    }

    /**
     * Update the SudokuMatrix such that on the given row and column, the given
     * number is pre-filled as input.
     */
    public void setInput(int row, int column, int number) {
        this.solver.coverRow(this.matrix.getNodeRow(row, column, number));
    }

    // The matrix modelling the Sudoku problem
    private SudokuMatrix matrix;

    // The internal solver
    private DLXSolver solver;
}
