
#include "sudoku-processor.hpp"

#include <sstream>

#include "gtest/gtest.h"

namespace app {

namespace {

/**
 * Test: solve an example sudoku and test solution
 */
TEST(MainSudokuProcessor, Small) {
  // Define input and ouput streams
  std::istringstream input(R"(4 _ _ 1
_ 1 3 _
_ 4 1 _
1 _ _ 3)");
  std::ostringstream output;

  bool result = processSudoku(input, output);

  EXPECT_TRUE(result);

  EXPECT_EQ(output.str(), R"(4 3 2 1 
2 1 3 4 
3 4 1 2 
1 2 4 3 
)");
}

}  // namespace
}  // namespace app