#ifndef LIBSUDOKU_SOLUTION_HPP_
#define LIBSUDOKU_SOLUTION_HPP_

#include <vector>

#include "placement.hpp"
#include "sudoku-math.hpp"

namespace sudoku {
/**
 * A fully populated Sudoku grid.
 *
 * This solution is produced by the Sudoku Solver and defines a grid of size
 * digit_range x digit_range with every cell filled in with a number
 * in the range 1..digit_range such that all Sudoku rules are observed.
 */
class Solution {
 public:
  /**
   * Constructor converting a set of placements to an easy-to-access sudoku solution grid.
   */
  explicit Solution(SudokuSize digit_range, const std::vector<Placement> &raw_solution);

  /**
   * Get the size of the solution grid.
   */
  [[nodiscard]] auto getSudokuSize() const -> SudokuSize;

  /**
   * Get the number at the given sudoku grid cell.
   *
   * \param row A row (1..digit_range) in the sudoku grid
   * \param column A column (1..digit_range) in the sudoku grid
   */
  auto getCellValue(int row, int column) -> int;

 private:
  // The size of the solution grid
  SudokuSize digit_range;

  // The solution grid itself as a one-dimensional row-by-row vector.
  std::vector<int> grid;
};

}  // namespace sudoku

#endif  // LIBSUDOKU_SOLUTION_HPP_