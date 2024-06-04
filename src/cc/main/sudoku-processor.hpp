#ifndef APP_SUDOKU_PROCESSOR_HPP_
#define APP_SUDOKU_PROCESSOR_HPP_

#include <iostream>
#include <string>

namespace app {

extern const std::string InvalidInput;
extern const std::string NoSolution;
extern const char TokenEmptyCell;

/**
 * Reads in a partial Sudoku form input and writes Sudoku solution to output.
 * 
 * \param input Stream of lines defining partial input Sudoku.
 *              For a Sudoku of size N, there should be N lines of length N .
 *              Each line can contain numbers 1..N (using 1..F for size 16).
 *              Character '_' is used to define an empty cell.
 *              Lines can contain optional spaces, these are ignored.
 * \return if a solution was found and written to output.
*/
auto processSudoku(std::istream& input, std::ostream& output) -> bool;

}  // namespace app

#endif  // APP_SUDOKU_PROCESSOR_HPP_