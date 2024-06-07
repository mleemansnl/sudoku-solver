
#include "sudoku-processor.hpp"

#include <algorithm>
#include <cstdlib>
#include <optional>
#include <sstream>

#include "src/cc/libsudoku/solver.hpp"

namespace app {

/**
 * In the input, an empty cell is represented by an undescore '_'
 */
const char TokenEmptyCell = '_';

/**
 * Base Sixteen
 */
const int BaseSixteen = 16;

/**
 * Helper method to parse input Sudoku from given stream
 */
auto parseInput(std::istream& input) -> std::unique_ptr<sudoku::Solver>;

/**
 * Helper method to write Sudoku solution to given stream
 */
void writeSolution(std::ostream& output, std::unique_ptr<sudoku::Solution> solution);

/**
 * Reads in a partial Sudoku form input and writes Sudoku solution to output.
 */
auto processSudoku(std::istream& input, std::ostream& output) -> bool {
  try {
    // Parse input
    auto solver = parseInput(input);
    auto result = solver->solve();

    // Check if a solution was found
    if (!result.has_value()) {
      output << "No valid Sudoku solution found" << '\n';
      return false;
    }

    writeSolution(output, std::move(result.value()));
    return true;

  } catch (const char* error_msg) {
    output << error_msg << '\n';
  }
  return false;
}

auto char2number(char value, sudoku::SudokuSize sudoku_size) -> int {
  std::string str{value};
  int number = static_cast<int>(strtol(str.c_str(), nullptr, BaseSixteen));

  // Sudokus of 16x16 start at 0 instead of 1 (digit range: 0..F)
  if (sudoku_size == sudoku::SudokuSize::Sixteen) {
    number++;
  }

  return number;
}

auto number2char(int number, sudoku::SudokuSize sudoku_size) -> char {
  // Sudokus of 16x16 start at 0 instead of 1 (digit range: 0..F)
  if (sudoku_size == sudoku::SudokuSize::Sixteen) {
    number--;
  }

  std::string chars("0123456789ABCDEF");
  return chars.at(number % BaseSixteen);
}

/**
 * Helper method to parse input Sudoku from given stream
 */
auto parseInput(std::istream& input) -> std::unique_ptr<sudoku::Solver> {
  std::string str;
  sudoku::SudokuSize size = sudoku::SudokuSize::Nine;

  // First line determines sudoku size
  if (!std::getline(input, str)) {
    throw "Error: Input stream is empty";
  }

  // remove whitespace and get length
  str.erase(std::remove_if(str.begin(), str.end(), isspace), str.end());
  std::size_t input_length = str.length();

  // determine sudoku size based on the length of the first input line
  switch (input_length) {
    case static_cast<std::size_t>(sudoku::SudokuSize::Four):
      size = sudoku::SudokuSize::Four;
      break;
    case static_cast<std::size_t>(sudoku::SudokuSize::Nine):
      size = sudoku::SudokuSize::Nine;
      break;
    case static_cast<std::size_t>(sudoku::SudokuSize::Sixteen):
      size = sudoku::SudokuSize::Sixteen;
      break;
    default:
      std::stringstream msg;
      msg << "Error: Unrecognized Sudoku size:" << input_length << ". Valid sizes are: 4, 9, 16";
      throw msg.str();
  }

  auto matrix = std::make_unique<sudoku::SudokuMatrix>(size);
  auto solver = std::make_unique<sudoku::Solver>(std::move(matrix));

  // Read in input line by line
  for (int row = 1; row <= static_cast<int>(input_length); row++) {
    // remove whitespace and get length
    str.erase(std::remove_if(str.begin(), str.end(), isspace), str.end());
    std::size_t cur_length = str.length();

    // Check if this line is complete
    if (cur_length != input_length) {
      std::stringstream msg;
      msg << "Error: All lines should be of equal size. Read: " << cur_length << ". expected: " << input_length;
      throw msg.str();
    }

    // parse current row
    int column = 1;
    for (char& token : str) {
      if (token != TokenEmptyCell) {
        // Interpret number as int
        int number = char2number(token, size);
        solver->setInput(row, column, number);
      }
      column++;
    }

    // read in next row
    if (row < static_cast<int>(input_length)) {
      if (!std::getline(input, str)) {
        std::stringstream msg;
        msg << "Error: Read " << (row) << " lines. Expected to read " << input_length << " lines instead";
        throw msg.str();
      }
    }
  }

  return solver;
}

/**
 * Helper method to write Sudoku solution to given stream
 */
void writeSolution(std::ostream& output, std::unique_ptr<sudoku::Solution> solution) {
  auto sudoku_size = solution->getSudokuSize();
  int grid_size = static_cast<int>(sudoku_size);

  for (int row = 1; row <= grid_size; row++) {
    for (int column = 1; column <= grid_size; column++) {
      // Get number value at given cell and print with the appropiate character
      int number = solution->getCellValue(row, column);
      output << number2char(number, sudoku_size) << " ";
    }
    output << '\n';
  }
}

}  // namespace app