# Sudoku Solver using Algorithm X & Dancing Links (DLX)

Sudoku Solver is a command-line tool for solving Sudoku puzzles of size 4x4, 
traditional 9x9, and 16x16.

The code presented here is a reference implementation of Algorithm X and
Dancing Links (DLX) as originally described by Donald Knuth. For more
information, check the documentation in this repository or the following
references:

- [Wikipedia > Knuth's Algoritm X](https://en.wikipedia.org/wiki/Knuth%27s_Algorithm_X)
- [Wikipedia > Dancing Links](https://en.wikipedia.org/wiki/Dancing_Links)
- [Arxiv > Knuth's original paper on Dancing Links](https://arxiv.org/abs/cs/0011047)

## Features

- Solves 4x4, traditional 9x9, and 16x16 Sudokus
- Uses the Dancing Links (DLX) data structure for efficient O(1) remove and
  reinsert operations.
- Uses for Algorithm X for exact cover problem solving on a sparse matrix constructed
  via Dancing Link nodes.
- Models a Sudoku puzzle as an exact cover problem and solves the puzzle using
  Algorithm X and DLX.
- Reference implementation in C++

## Usage

- [ ] Todo: Add instructions on how to solve an example input

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

## License

This project is licensed under the terms of GNU GPLv3.

See the [LICENSE](LICENSE) file for details.
