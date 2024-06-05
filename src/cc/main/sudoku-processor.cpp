
#include "sudoku-processor.hpp"

#include <algorithm>
#include <cstdlib>
#include <optional>
#include <sstream>

#include "src/cc/libsudoku/solver.hpp"

namespace app {

const std::string InvalidInput = "Input is invalid";
const std::string NoSolution = "No valid Sudoku solution found";
const char TokenEmptyCell = '_';

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
      output << NoSolution << '\n';
      return false;
    }

    writeSolution(output, std::move(result.value()));
    return true;

  } catch (const char* error_msg) {
    output << error_msg << '\n';
  }
  return false;
}

auto char2number(char value) -> int {
  std::string str{value};
  return static_cast<int>(strtol(str.c_str(), nullptr, BaseSixteen));
}

auto number2char(int number) -> char {
  std::string chars("123456789ABCDEFG");
  return chars.at((number - 1) % BaseSixteen);
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

  // determine sudoku size
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
        solver->setInput(row, column, char2number(token));
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
  int sudoku_size = static_cast<int>(solution->getSudokuSize());

  for (int row = 1; row <= sudoku_size; row++) {
    for (int column = 1; column <= sudoku_size; column++) {
      output << solution->getCellValue(row, column) << " ";
    }
    output << '\n';
  }
}

}  // namespace app