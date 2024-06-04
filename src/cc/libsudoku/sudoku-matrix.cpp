
#include "sudoku-matrix.hpp"

#include "placement.hpp"

namespace sudoku {

/**
 * For a typical sudoku of size SudokuSize=9, there are 4 types of conditions to check for:
 *  1. in each cell has exactly one value between 1 and 9
 *  2. in each row, each digit may appear only once
 *  3. in each column, each digit may appear only once
 *  4. in each box of 3x3, each digit may appear only once
 */
constexpr int TypesOfConditions = 4;

/**
 * Constructs a new sparse matrix modelling a Sudoku problem
 * of the given size.
 */
SudokuMatrix::SudokuMatrix(SudokuSize sudoku_size)
    : sudoku_size(sudoku_size) {
  const int num_digits = static_cast<int>(this->sudoku_size);

  /**
   * This matrix will model the exact cover problem for a sudoku of size <sudoku_size>
   * The matrix is represented as a sparse matrix structure and
   * implemented using the dancing link nodes as doubly-linked lists.
   *
   * Each column in this matrix will represent one condition choice.
   *
   * We model these conditions in 4 groups of header columns:
   *  1. the first digit_range^2 columns represent
   *     for each cell a number is filled in
   *  2. the second digit_range^2 columns represent
   *     for each row every number between 1..digit_range is filled in
   *  3. the third digit_range^2 columns represent
   *     for each column every number between 1..digit_range is filled in
   *  4. the fourth digit_range^2 columns represent
   *     for each box every number between 1..digit_range is filled in
   * Therefore, the total number of conditions to model equals:
   *   NumConditions = num_digits * num_digits * TypesOfConditions
   *                 = 9 * 9 * 4 = 324   for a typical sudoku of size num_digits=9
   *
   */
  const int NumConditions = num_digits * num_digits * TypesOfConditions;

  // We will use this helper data-structure to quickly lookup column headers by index
  // This is used while filling the matrix with nodes matching the correct column
  std::vector<dlx::DancingLinkHeader *> columnByIndex(NumConditions);

  // Create every column header
  for (int i = 0; i < NumConditions; i++) {
    columnByIndex[i] = this->newHeader();
  }

  // Next, we fill the matrix with nodes, representing every possible digit in every cell
  // The total number of nodes to model equals:
  //    numNodes = digits * digits * digits
  //             = 9 * 9 * 9 = 729   for a typical sudoku of size digits=9
  const int NumNodes = num_digits * num_digits * num_digits;

  // We use this helper data-structure to quickly lookup root/starting placement nodes by index
  // This will be used to lookup a row of nodes for a given (row, column, number) triplet.
  this->index2node.reserve(NumNodes);

  // Create every node and add to the vertical list of the right column headers
  for (int row = 1; row <= num_digits; row++) {
    for (int column = 1; column <= num_digits; column++) {
      for (int number = 1; number <= num_digits; number++) {
        // Mark for condition: cell, row, column, and box
        auto *nodeCell = columnByIndex[indexForCondCell(sudoku_size, row, column)]->newNode();
        auto *nodeRow = columnByIndex[indexForCondRow(sudoku_size, row, number)]->newNode();
        auto *nodeColumn = columnByIndex[indexForCondColumn(sudoku_size, column, number)]->newNode();
        auto *nodeBox = columnByIndex[indexForCondBox(sudoku_size, row, column, number)]->newNode();

        // Define matrix row
        dlx::DancingLinkNode::makeRow(nodeCell, nodeRow, nodeColumn, nodeBox);

        // Add Placement meta data so that after finding a solution,
        // we can reinterpret the DancingLinkNodes as values in a Sudoku grid
        nodeCell->setData(Placement{row, column, number});
        nodeRow->setData(Placement{row, column, number});
        nodeColumn->setData(Placement{row, column, number});
        nodeBox->setData(Placement{row, column, number});

        this->index2node[indexForNode(sudoku_size, row, column, number)] = nodeCell;
      }
    }
  }

  // Now we have our sparse matrix fully constructed.
  //
  //                         ^                       ^
  //                         v                       v
  //  [this->root] <-> [condition 1] <-> ... <-> [condition n] <->
  //                         ^                       ^
  //                         v                       |
  //                  <-> [1,1,1] <-> [1,1,1] <->    |
  //                         ^                       v
  //                         v   <-> [9,9,9] <-> [9,9,9] <->
  //                        ....                     ^
  //                                                 v
}

auto SudokuMatrix::getSudokuSize() const -> SudokuSize {
  return this->sudoku_size;
}

auto SudokuMatrix::getNodeRow(const int row, const int column, const int number) -> dlx::DancingLinkNode * {
  return this->index2node[indexForNode(this->sudoku_size, row, column, number)];
}

}  // namespace sudoku