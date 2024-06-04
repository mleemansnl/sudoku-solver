#include "solver.hpp"

#include <limits>

namespace dlx {
Solver::Solver(std::unique_ptr<DancingLinksMatrix> matrix)
    : matrix(std::move(matrix)) {}

auto Solver::solve() -> std::optional<Solution> {
  if (this->search()) {
    return this->solution;
  }

  return std::nullopt;
}

// @return if a solution is found
auto Solver::search() -> bool {
  auto *root = this->matrix->getRoot();

  // check if there are condition columns left.
  // if not, then we found a valid solution
  if (root->getRight() == root) {
    // no columns left, so we have a valid solution
    // the solution is already stored in solution as part of the cover actions
    return true;
  }

  // no solution found, so we continue our search
  // optimization: find column with lowest count
  std::optional<DancingLinkHeader *> opt_header = this->selectHeaderColumn();

  // check if this is a good solution
  if (!opt_header.has_value()) {
    return false;
  }

  auto *target_header = opt_header.value();

  // cover this header column
  Solver::cover(target_header);

  // try every placement part of this column
  // add it to the solution, recurse solve the smaller matrix,
  // and backtrack if no good solution was found
  for (auto *row = target_header->getDown(); row != target_header; row = row->getDown()) {
    // try solution with current row
    this->solution.push_back(row);

    // cover all other columns in this row
    for (auto *node = row->getRight(); node != row; node = node->getRight()) {
      Solver::cover(node->getHeader());
    }

    // recursive search with this row in the solution and all it's columns covered
    if (this->search()) {
      return true;
    }

    // remove row from solution, so we may try with the next row
    this->solution.pop_back();

    // uncover all columns in row
    for (auto *node = row->getLeft(); node != row; node = node->getLeft()) {
      Solver::uncover(node->getHeader());
    }
  }

  // uncover column
  Solver::uncover(target_header);

  return false;
}

void Solver::coverRow(DancingLinkNode *row_root) {
  // Add placement to solution
  this->solution.push_back(row_root);

  // Cover every column in this row
  Solver::cover(row_root->getHeader());
  for (auto *node = row_root->getRight(); node != row_root; node = node->getRight()) {
    Solver::cover(node->getHeader());
  }
}

auto Solver::selectHeaderColumn() -> std::optional<DancingLinkHeader *> {
  auto *root = this->matrix->getRoot();

  // find column with lowest count
  DancingLinkHeader *target_header = nullptr;
  int minCount = std::numeric_limits<int>::max();

  for (auto *header = root->getRight()->getHeader();
       header != root;
       header = header->getRight()->getHeader()) {
    // check if the current header has a lower count
    // than the currently found targetHeader
    if (header->getCount() < minCount) {
      minCount = header->getCount();
      target_header = header;
    }
  }

  if (target_header != nullptr) {
    return target_header;
  }

  return std::nullopt;
}

void Solver::cover(DancingLinkHeader *header) {
  // remove column header from the matrix
  // indicating this condition is satisfied
  header->removeHorizontal();

  // remove all rows in this column from other columns they are in
  // indicating this is no longer a way to satisfy those conditions
  for (auto *row = header->getDown(); row != header; row = row->getDown()) {
    for (auto *node = row->getRight(); node != row; node = node->getRight()) {
      node->removeVertical();
      node->getHeader()->decCount();
    }
  }
}

void Solver::uncover(DancingLinkHeader *header) {
  // put back all rows in the column into other columns they were in
  for (auto *row = header->getUp(); row != header; row = row->getUp()) {
    for (auto *node = row->getLeft(); node != row; node = node->getLeft()) {
      node->reinsertVertical();
      node->getHeader()->incCount();
    }
  }

  // put back column into the matrix
  header->reinsertHorizontal();
}

}  // namespace dlx