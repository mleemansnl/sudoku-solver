package com.sudokusolver.main;

import java.io.Writer;
import java.io.PrintWriter;
import java.util.Optional;
import java.util.Scanner;

import com.sudokusolver.libsudoku.Solution;
import com.sudokusolver.libsudoku.SudokuMatrix;
import com.sudokusolver.libsudoku.SudokuSize;
import com.sudokusolver.libsudoku.SudokuSolver;

public final class SudokuProcessor {

    private SudokuProcessor() {
    }

    /**
     * In the input, an empty cell is represented by an undescore '_'
     */
    public static final char TokenEmptyCell = '_';

    /**
     * Base Sixteen
     */
    public static final int BaseSixteen = 16;

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
    public static boolean processSudoku(Readable input, Writer output) {
        // Prepare streams
        PrintWriter writer = new PrintWriter(output, true);

        try {
            // Parse input
            SudokuSolver solver = SudokuProcessor.parseInput(input);
            Optional<Solution> result = solver.solve();

            // Check if a solution was found
            if (!result.isPresent()) {
                writer.print("No valid Sudoku solution found\n");
                return false;
            }

            writeSolution(writer, result.get());
            return true;

        } catch (Exception e) {
            writer.write(e.getMessage());
            writer.write("\n");
        }
        return false;
    }

    private static int char2number(char value, SudokuSize sudokuSize) {
        int number = Integer.parseInt(String.valueOf(value), BaseSixteen);

        // Sudokus of 16x16 start at 0 instead of 1 (digit range: 0..F)
        if (sudokuSize == SudokuSize.Sixteen) {
            number++;
        }

        return number;
    }

    private static char number2char(int number, SudokuSize sudokuSize) {
        // Sudokus of 16x16 start at 0 instead of 1 (digit range: 0..F)
        if (sudokuSize == SudokuSize.Sixteen) {
            number--;
        }

        String chars = "0123456789ABCDEF";
        return chars.charAt(number % BaseSixteen);
    }

    /**
     * Helper method to parse input Sudoku from given stream
     */
    private static SudokuSolver parseInput(Readable inputStream) throws Exception {
        Scanner input = new Scanner(inputStream);
        try {
            SudokuSize size = SudokuSize.Nine;

            // First line determines sudoku size
            if (!input.hasNextLine()) {
                throw new Exception("Error: Input stream is empty");
            }
            String line = input.nextLine();

            // remove whitespace and get length
            line = line.replaceAll("\\s+", "");
            final int inputLength = line.length();

            // determine sudoku size based on the length of the first input line
            if (inputLength == SudokuSize.Four.getDigitRange()) {
                size = SudokuSize.Four;
            } else if (inputLength == SudokuSize.Nine.getDigitRange()) {
                size = SudokuSize.Nine;
            } else if (inputLength == SudokuSize.Sixteen.getDigitRange()) {
                size = SudokuSize.Sixteen;
            } else {
                throw new Exception(
                        "Error: Unrecognized Sudoku size: %d. Valid sizes are: 4, 9, 16".formatted(inputLength));
            }

            SudokuMatrix matrix = new SudokuMatrix(size);
            SudokuSolver solver = new SudokuSolver(matrix);

            // Read in input line by line
            for (int row = 1; row <= inputLength; row++) {
                // remove whitespace and get length
                line = line.replaceAll("\\s+", "");
                final int curLength = line.length();

                // Check if this line is complete
                if (curLength != inputLength) {

                    throw new Exception("Error: All lines should be of equal size. Read: %d. expected: %d"
                            .formatted(curLength, inputLength));
                }

                // parse current row
                int column = 1;
                for (char token : line.toCharArray()) {
                    if (token != TokenEmptyCell) {
                        // Interpret number as int
                        int number = char2number(token, size);
                        solver.setInput(row, column, number);
                    }
                    column++;
                }

                // read in next row
                if (row < inputLength) {
                    if (!input.hasNextLine()) {
                        throw new Exception(
                                "Error: Read  %d lines. Expected to read %d lines instead".formatted(row, inputLength));
                    }
                    line = input.nextLine();
                }
            }

            return solver;
        } finally {
            input.close();
        }
    }

    /**
     * Helper method to write Sudoku solution to given stream
     */
    private static void writeSolution(PrintWriter output, Solution solution) {
        SudokuSize sudokuSize = solution.getSudokuSize();
        int gridSize = sudokuSize.getDigitRange();

        for (int row = 1; row <= gridSize; row++) {
            for (int column = 1; column <= gridSize; column++) {
                // Get number value at given cell and print with the appropiate character
                int number = solution.getCellValue(row, column);
                output.write(SudokuProcessor.number2char(number, sudokuSize));
                output.write(" ");
            }
            output.write("\n");
        }
    }

}
