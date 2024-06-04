#ifndef LIBSUDOKU_SOLVER_HPP_
#define LIBSUDOKU_SOLVER_HPP_

#include "solution.hpp"
#include "src/cc/libdlx/solver.hpp"
#include "sudoku-matrix.hpp"

namespace sudoku {

/**
 * This Solver tales an input SudokuMatrix and solves to find a valid Sudoku Solution.
 *
 * This Solver wraps dlx::Solver and provides Sudoku-specific convenciences:
 *  - The setInput() method allows to update the SudokuMatrix such that
 *    on the given row and column, the given number is pre-filled as input.
 *  - The solve() method converts a found solution from the raw exact cover dlx::Solution
 *    to a Sudoku Solution object.
 */
class Solver {
 public:
  /**
   * Construsts a new Solver based on the given Sudoku matrix.
   *
   * \param matrix A matrix modelling the input Sudoku to be solved.
   *               The solver owns the memory of this matrix after construction.
   */
  explicit Solver(std::unique_ptr<SudokuMatrix> matrix);

  /**
   * Solve for the provided matrix using the dlx::Solver.
   *
   * \return If no solution is found, std::nullopt is returned.
   *         If a solution is found, a Sudoku Solution object is returned.
   */
  auto solve() -> std::optional<std::unique_ptr<Solution>>;

  /**
   * Update the SudokuMatrix such that on the given row and column,
   * the given number is pre-filled as input.
   */
  void setInput(int row, int column, int number);

 private:
  // The matrix modelling the Sudoku problem
  SudokuMatrix *matrix;

  // The internal solver
  dlx::Solver solver;
};

}  // namespace sudoku

#endif  // LIBSUDOKU_SOLVER_HPP_