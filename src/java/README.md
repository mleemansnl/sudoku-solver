# Java implementation for Algorithm X & Dancing Links (DLX)

## Packages

- libdlx defines the Dancing Links (DLX) data structure and
  Algorithm X solver for generic exact cover problem.
- libsudoku defines a Sudoku solver that models models a Sudoku puzzle
  as an exact cover problem and solves the puzzle using libdlx.
- main defines the cli binary reading in a partial sudoku via stdin and
  outputtring the solution on stdout.

## Usage

To run the sudoku solver with an example input, use:

```shell
bazel run //src/java/main:sudoku
```