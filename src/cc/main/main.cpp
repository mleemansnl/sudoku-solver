
#include "sudoku-processor.hpp"

auto main() -> int {
  if (!app::processSudoku(std::cin, std::cout)) {
    return 1;
  }

  return 0;
}
