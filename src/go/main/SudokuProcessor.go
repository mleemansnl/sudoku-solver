package main

import (
	"bufio"
	"errors"
	"fmt"
	"io"
	"regexp"
	"strconv"

	"github.com/mleemansnl/sudoku-solver/src/go/libsudoku"
)

/**
 * In the input, an empty cell is represented by an undescore '_'
 */
const TokenEmptyCell = '_'

/**
 * Base Sixteen
 */
const BaseSixteen = 16

/**
 * Reads in a partial Sudoku form input and writes Sudoku solution to output.
 *
 * @param input Stream of lines defining partial input Sudoku. For a Sudoku of
 *              size N, there should be N lines of length N . Each line can
 *              contain numbers 1..N (using 1..F for size 16). Character '_' is
 *              used to define an empty cell. Lines can contain optional spaces,
 *              these are ignored.
 * @return if a solution was found and written to output.
 */
func ProcessSudoku(input io.Reader, output io.StringWriter) bool {
	solver, err := parseInput(input)
	if err != nil {
		output.WriteString(err.Error())
		return false
	}

	// Solve and check if a solution was found
	solution, hasSolution := solver.Solve()
	if !hasSolution {
		output.WriteString("No valid Sudoku solution found\n")
		return false
	}

	writeSolution(output, solution)
	return true
}

/**
 * Helper method to parse input Sudoku from given stream
 */
func parseInput(input io.Reader) (solver libsudoku.SudokuSolver, err error) {
	var size libsudoku.SudokuSize
	var line string
	solver = nil

	// Scan the input line-by-line
	scanner := bufio.NewScanner(input)

	// First line determines sudoku size
	if !scanner.Scan() {
		err = errors.New("Error: Input stream is empty")
		return
	}
	line = scanner.Text()

	// remove whitespace and get length
	regexWhitespace, _ := regexp.Compile("\\s+")
	line = regexWhitespace.ReplaceAllString(line, "")
	inputLength := len(line)

	// determine sudoku size based on the length of the first input line
	switch inputLength {
	case int(libsudoku.SudokuFour):
		size = libsudoku.SudokuFour
	case int(libsudoku.SudokuNine):
		size = libsudoku.SudokuNine
	case int(libsudoku.SudokuSixteen):
		size = libsudoku.SudokuSixteen
	default:
		err = errors.New(fmt.Sprintf("Error: Unrecognized Sudoku size: %d. Valid sizes are: 4, 9, 16",
			inputLength))
		return
	}

	matrix := libsudoku.NewSudokuMatrix(size)
	solver = libsudoku.NewSudokuSolver(matrix)

	// Read in input line by line
	for row := 1; row <= inputLength; row++ {
		// remove whitespace and get length
		line = regexWhitespace.ReplaceAllString(line, "")
		curLength := len(line)

		// Check if this line is complete
		if curLength != inputLength {
			err = errors.New(fmt.Sprintf("Error: All lines should be of equal size. Read: %d. expected: %d",
				curLength, inputLength))
			return
		}

		// parse current row
		column := 1
		for _, token := range line {
			if token != TokenEmptyCell {
				// Interpret number as int
				number := char2number(token, size)
				solver.SetInput(row, column, number)
			}
			column++
		}

		// read in next row
		if row < inputLength {
			if !scanner.Scan() {
				err = errors.New(fmt.Sprintf("Error: Read %d lines. Expected to read %d lines instead",
					row, inputLength))
				return
			}
			line = scanner.Text()
		}
	}

	return
}

/**
 * Helper method to write Sudoku solution to given stream
 */
func writeSolution(output io.StringWriter, solution libsudoku.Solution) {
	sudokuSize := solution.GetSudokuSize()
	gridSize := int(sudokuSize)

	for row := 1; row <= gridSize; row++ {
		for column := 1; column <= gridSize; column++ {
			// Get number value at given cell and print with the appropiate character
			number := solution.GetCellValue(row, column)
			output.WriteString(string(number2char(number, sudokuSize)))
			output.WriteString(" ")
		}
		output.WriteString("\n")
	}
}

func char2number(token rune, sudokuSize libsudoku.SudokuSize) int {
	number, _ := strconv.ParseInt(string(token), BaseSixteen, 0)

	// Sudokus of 16x16 start at 0 instead of 1 (digit range: 0..F)
	if sudokuSize == libsudoku.SudokuSixteen {
		number++
	}

	return int(number)
}

func number2char(number int, sudokuSize libsudoku.SudokuSize) rune {
	// Sudokus of 16x16 start at 0 instead of 1 (digit range: 0..F)
	if sudokuSize == libsudoku.SudokuSixteen {
		number--
	}

	const chars = "0123456789ABCDEF"
	return rune(chars[number%BaseSixteen])
}
