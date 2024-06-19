# Java implementation for Algorithm X & Dancing Links (DLX)

## Packages

- com.sudokusolver.libdlx defines the Dancing Links (DLX) data structure and
  Algorithm X solver for generic exact cover problem.
- com.sudokusolver.libsudoku defines a Sudoku solver that models models a Sudoku
  puzzle as an exact cover problem and solves the puzzle using libdlx.
- com.sudokusolver.main defines the cli binary reading in a partial sudoku via
  System.in and outputting the solution on System.out.

## Usage

To run the sudoku solver with an example input, use:

```shell
cat examples/sudoku-4x4.txt | bazel run //src/java/com/sudokusolver/main:Main
cat examples/sudoku-9x9.txt | bazel run //src/java/com/sudokusolver/main:Main
cat examples/sudoku-16x16.txt | bazel run //src/java/com/sudokusolver/main:Main
```
