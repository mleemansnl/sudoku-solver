# Sudoku Solver | Algorithm X and Dancing Links (DLX)

Sudoku Solver is a command-line tool build with Bazel for solving Sudoku puzzles
of size 4x4, traditional 9x9, and 16x16.

The code presented here is a reference implementation of Algorithm X and
Dancing Links (DLX) as originally described by Donald Knuth. For more
information, check the documentation in this repository or the following
references:

- [Wikipedia > Knuth's Algoritm X](https://en.wikipedia.org/wiki/Knuth%27s_Algorithm_X)
- [Wikipedia > Dancing Links](https://en.wikipedia.org/wiki/Dancing_Links)
- [Arxiv > Knuth's original paper on Dancing Links](https://arxiv.org/abs/cs/0011047)

In addition, this repository showcases how to use [Bazel](https://bazel.build/) for
building, testing, and linting in various programming languages.

## Features

The solvers implemented offer:

- Solves 4x4, traditional 9x9, and 16x16 Sudokus
- Uses the Dancing Links (DLX) data structure for efficient O(1) remove and
  reinsert operations.
- Uses for Algorithm X for exact cover problem solving on a sparse matrix constructed
  via Dancing Link nodes.
- Models a Sudoku puzzle as an exact cover problem and solves the puzzle using
  Algorithm X and DLX.

Reference implementations are provided in multiple programming languages and
use [Bazel](https://bazel.build/) for building, testing, and linting:

| Language    | Directory            | Test Framework | Linters      |
| ----------- | -------------------- | -------------- | ------------ |
| C++ 20      | [src/cc](src/cc)     | GTest 1.14     | clang-tidy   |
| Java 17     | [src/java](src/java) | JUnit 5.10     | pmd          |
| Golang 1.22 | [src/go](src/go)     | Testify 1.9.0  | nogo         |
| Markdown    |                      |                | markdownlint |
| GH Actions  |                      |                | actionlint   |

For more information on how Bazel is used, see [Bazel.md](Bazel.md).

## Usage

To run the sudoku solver with an example input, use one of the example files and
choose on of the implementations:

```shell
cat examples/sudoku-9x9.txt | bazel run //src/cc/main:sudoku
```

The tool takes an input sudoku from stdin and writes the solution to stdout.
Note that the bazel run command both compiles and runs the code as needed.

## Installation

### Prerequisites

This project relies on a [Dev Container](https://code.visualstudio.com/docs/devcontainers/containers)
to correctly set up your environment.
Before you begin, ensure you have met the following requirements:

- You have installed [Docker](https://www.docker.com/get-started/) on your environment.
- You have installed Visual Studio Code with the
  [Dev Containers extension](https://marketplace.visualstudio.com/items?itemName=ms-vscode-remote.remote-containers).

### Open in Dev Container

Clone the repository and open it in VSCode:

```shell
git clone https://github.com/mleemansnl/sudoku-solver.git
cd sudoku-solver/
code .
```

Run the `Dev Containers: Open Folder in Container...` command from the Command
Palette (F1) or quick actions Status bar item.

### Build

For the dev container environment, build using [Bazel](https://bazel.build/):

```shell
bazel build //...
```

### Test

For the dev container environment, run all tests via this script:

```shell
./test.sh
```

### Lint

For the dev container environment, run all linters via this script:

```shell
./lint.sh
```

### Updating Build files with Gazelle

For the golang implementation, Gazelle is used for Bazel BUILD file generation.
Upon changing golang code, run the following command:

```shell
bazel run //:gazelle
```

External golang dependencies are managed by Gazelle via go mod and are
listed explicitly in MODULE.bazel.
Upon adding or upgreding a new external golang dependency, run:

```shell
bazel run @rules_go//go -- mod tidy
```

## License

This project is licensed under the terms of GNU GPLv3.

See the [LICENSE](LICENSE) file for details.
