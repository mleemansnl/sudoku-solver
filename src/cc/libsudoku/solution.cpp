
#include "solution.hpp"

namespace sudoku {
Solution::Solution(SudokuSize digit_range, const std::vector<Placement> &raw_solution)
    : digit_range(digit_range) {
  grid.reserve(static_cast<int64_t>(digit_range) * static_cast<int64_t>(digit_range));

  for (auto placement : raw_solution) {
    int index = (placement.row - 1) * static_cast<int>(digit_range) + placement.column - 1;
    grid[index] = placement.number;
  }
}

auto Solution::getSudokuSize() const -> SudokuSize {
  return this->digit_range;
}

auto Solution::getCellValue(int row, int column) -> int {
  int index = (row - 1) * static_cast<int>(this->digit_range) + column - 1;
  return grid[index];
}
}  // namespace sudoku