#ifndef LIBSUDOKU_SUDOKU_MATRIX_HPP_
#define LIBSUDOKU_SUDOKU_MATRIX_HPP_

#include <vector>

#include "src/cc/libdlx/dancing-links-matrix.hpp"
#include "sudoku-math.hpp"

namespace sudoku {
/**
 * The SudokuMatrix represents a sparse matrix capturing a Sudoku problem.
 *
 * This sparse matrix models the Sudoku conditions (rules of the game) on
 * the headers and possible placements (numbers in specific Sudoku cells)
 * on the rows. A subset of placements only yields a valid Sudoku solution
 * if all the Sudoku conditions are met (i.e., form an exact cover).
 *
 * The \link Solver class can be used to update this matrix such that on
 * a given row and column, a given number is pre-filled as input.
 */
class SudokuMatrix : public dlx::DancingLinksMatrix {
 public:
  /**
   * Constructs a new sparse matrix modelling a Sudoku problem
   * of the given size.
   */
  explicit SudokuMatrix(SudokuSize size);

  /**
   * Get the size of the solution grid.
   */
  [[nodiscard]] auto getSudokuSize() const -> SudokuSize;

  /**
   * Get the DancingLinkNode row node corresponding to a given placement.
   */
  auto getNodeRow(int row, int column, int number) -> dlx::DancingLinkNode *;

 private:
  // The size of the solution grid
  SudokuSize sudoku_size;

  // Lookup vector for placement nodes by index.
  // This is used to lookup a row of nodes for a given (row, column, number) triplet.
  std::vector<dlx::DancingLinkNode *> index2node;
};

}  // namespace sudoku

#endif  // LIBSUDOKU_SUDOKU_MATRIX_HPP_