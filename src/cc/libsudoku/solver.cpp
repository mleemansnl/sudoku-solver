
#include "solver.hpp"

#include <iostream>

namespace sudoku {
Solver::Solver(std::unique_ptr<SudokuMatrix> matrix)
    : matrix(matrix.get()), solver(std::move(matrix)) {}

auto Solver::solve() -> std::optional<std::unique_ptr<Solution>> {
  auto optRawSolution = this->solver.solve();

  // check if a solution was found
  if (optRawSolution.has_value()) {
    // convert internal solution to list of placement values
    auto rawSolution = optRawSolution.value();
    std::vector<Placement> vec_solution;
    vec_solution.reserve(rawSolution.size());

    for (auto *node : rawSolution) {
      vec_solution.push_back(std::any_cast<Placement>(node->getData()));
    }

    return std::make_unique<Solution>(this->matrix->getSudokuSize(), vec_solution);
  }

  return std::nullopt;
}

void Solver::setInput(const int row, const int column, const int number) {
  this->solver.coverRow(this->matrix->getNodeRow(row, column, number));
}

}  // namespace sudoku