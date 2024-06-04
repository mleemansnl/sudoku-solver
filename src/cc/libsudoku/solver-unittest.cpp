#include "solver.hpp"

#include "gtest/gtest.h"

namespace sudoku {
namespace {

/**
 * Test Helper: create a small example soduku problem to be solved
 */
auto createSmallSudoku() -> std::unique_ptr<Solver> {
  // Setup the matrix for the following Sudoku:
  //  4 _ _ 1
  //  _ 1 3 _
  //  _ 4 1 _
  //  1 _ _ 3

  // Define input
  auto matrix = std::make_unique<SudokuMatrix>(SudokuSize::Four);
  auto solver = std::make_unique<Solver>(std::move(matrix));

  solver->setInput(1, 1, 4);
  solver->setInput(1, 4, 1);

  solver->setInput(2, 2, 1);
  solver->setInput(2, 3, 3);

  solver->setInput(3, 2, 4);
  solver->setInput(3, 3, 1);

  solver->setInput(4, 1, 1);
  solver->setInput(4, 4, 3);

  return solver;
}

/**
 * Test: solve an example sudoku and test solution
 */
TEST(SudokuSolver, Small) {
  // Solve small example sudoku
  auto solver = createSmallSudoku();
  auto result = solver->solve();

  // Check solution
  EXPECT_TRUE(result.has_value());
  if (result.has_value()) {
    // Expected solution:
    //  4 3 2 1
    //  2 1 3 4
    //  3 4 1 2
    //  1 2 4 3
    std::unique_ptr<Solution> solution = std::move(result.value());

    // We expect a solution for a 4 by 4 sudoku
    EXPECT_EQ(SudokuSize::Four, solution->getSudokuSize());

    // check the value of a preset input: in row 1, column 1, the value should be 4
    EXPECT_EQ(4, solution->getCellValue(1, 1));

    // in row 1, column 2 the correct value is 3
    EXPECT_EQ(3, solution->getCellValue(1, 2));
  }
}

}  // namespace
}  // namespace sudoku
