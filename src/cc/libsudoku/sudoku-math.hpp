#ifndef LIBSUDOKU_SUDOKU_MATH_HPP_
#define LIBSUDOKU_SUDOKU_MATH_HPP_

#include <cmath>
#include <cstdint>

namespace sudoku {
/**
 * SudokuSize predefines valid possible digit range sizes for a valid sudoku grid.
 *
 * Note that each valid value is a perfect square number,
 * a property needed to correctly model the sudoku grid conditions.
 */
enum class SudokuSize : std::uint8_t {
  // A Sudoku of 4 x 4, using numbers 1..4 and having boxes of 2x2
  Four = 4,

  // A Sudoku of 9 x 9, using numbers 1..9 and having boxes of 3x3
  Nine = 9,

  // A Sudoku of 16 x 16, using numbers 1..16 (or 1..F) and having boxes of 4x4
  Sixteen = 16
};

/*******
 * The below helper methods calculate the header node index for a given Sudoku condition.
 *
 * The SudokuMatrix will model the exact cover problem for a sudoku of size <digit_range>.
 * Each header column in this matrix will represent one condition choice.
 * We model these conditions in 4 groups of header columns:
 *  1. the first digit_range^2 columns represent
 *     for each cell a number is filled in
 *  2. the second digit_range^2 columns represent
 *     for each row every number between 1..digit_range is filled in
 *  3. the third digit_range^2 columns represent
 *     for each column every number between 1..digit_range is filled in
 *  4. the fourth digit_range^2 columns represent
 *     for each box every number between 1..digit_range is filled in
 * 
 * The offset in the below helper methods are based on the above groupings.
 ******/

/**
 * Helper method to calculate the header node index for the Sudoku cell condition.
 * Condition modelled: for each cell a number is filled in
 * 
 * See above description for more information.
*/
inline auto indexForCondCell(const SudokuSize digit_range, const int row, const int column) -> int {
  const int offset = 0;
  return offset + (row - 1) * static_cast<int>(digit_range) + (column - 1);
}

/**
 * Helper method to calculate the header node index for the Sudoku row condition.
 * Condition modelled: for each row every number between 1..digit_range is filled in
 * 
 * See above description for more information.
*/
inline auto indexForCondRow(const SudokuSize digit_range, const int row, const int number) -> int {
  const int offset = 1 * static_cast<int>(digit_range) * static_cast<int>(digit_range);
  return offset + (row - 1) * static_cast<int>(digit_range) + (number - 1);
}

/**
 * Helper method to calculate the header node index for the Sudoku column condition.
 * Condition modelled: for each column every number between 1..digit_range is filled in
 * 
 * See above description for more information.
*/
inline auto indexForCondColumn(const SudokuSize digit_range, const int column, const int number) -> int {
  const int offset = 2 * static_cast<int>(digit_range) * static_cast<int>(digit_range);
  return offset + (column - 1) * static_cast<int>(digit_range) + (number - 1);
}

/**
 * Helper method to calculate the header node index for the Sudoku box condition.
 * Condition modelled: for each box every number between 1..digit_range is filled in
 * 
 * See above description for more information.
*/
inline auto indexForCondBox(const SudokuSize digit_range, const int row, const int column, const int number) -> int {
  const int offset = 3 * static_cast<int>(digit_range) * static_cast<int>(digit_range);

  const int sqrt = static_cast<int>(std::floor(std::sqrt(static_cast<float>(digit_range))));
  const int box = ((column - 1) / sqrt) + ((row - 1) / sqrt) * sqrt;

  return offset + box * static_cast<int>(digit_range) + (number - 1);
}

/**
 * Helper method to calculate the lookup index for oot/starting placement nodes.
 * This is used to lookup a row of nodes for a given (row, column, number) triplet.
*/
inline auto indexForNode(const SudokuSize digit_range, const int row, const int column, const int number) -> int {
  return ((row - 1) * static_cast<int>(digit_range) * static_cast<int>(digit_range)) + ((column - 1) * static_cast<int>(digit_range)) + (number - 1);
}

}  // namespace sudoku

#endif  // LIBSUDOKU_SUDOKU_MATH_HPP_