#ifndef LIBDLX_SOLVER_HPP_
#define LIBDLX_SOLVER_HPP_

#include <optional>
#include <vector>

#include "dancing-links-matrix.hpp"

namespace dlx {
/**
 * A solution to an exact cover problem.
 *
 * A solution is represented by a subset of matrix rows which together
 * cover all the headers / conditions in the modelled exact cover problem.
 *
 * Each node in this vector represents a particular row from the original
 * \link DancingLinksMatrix passed to the \link Solver producing this solution.
 *
 * Using the \link DancingLinkNode::getLeft() and \link DancingLinkNode::getRight(),
 * to iterate over the other nodes in a particular row.
 *
 * The data element from \link DancingLinkNode::getData() can be used to infer
 * the meaning or interpretation of a particular row in a given solution.
 */
using Solution = std::vector<DancingLinkNode *>;

/**
 * The Solver implements the Alogrithm X for solving an exact cover problem.
 *
 * Algorithm X is (ref: https://en.wikipedia.org/wiki/Knuth%27s_Algorithm_X)
 * is an algorithm for solving the exact cover problem, first proposed by Donald Knuth
 * in https://arxiv.org/abs/cs/0011047 . The exact cover problem is represented in Algorithm X
 * by a matrix A consisting of 0s and 1s. The goal is to select a subset of the rows such that
 * the digit 1 appears in each column exactly once.
 *
 * To solve the exact cover problem, this algorithm recursively reduces matrix A till a solution
 * is found. In pseudo-code, the algorithm will (https://en.wikipedia.org/wiki/Knuth%27s_Algorithm_X):
 *  1. Check if the matrix has no columns. If so, then we have found a valid solution.
 *  2. Otherwise, select a column c with the lowest count.
 *  3. Remove (cover, see below) the chosen column.
 *  4. Select a row r where A[c,r] = 1 (i.e., a row in that column):
 *      - Include row r in the candidate partial solution.
 *      - Remove (cover, see below) the chosen row, as well as all other columns covered by this row.
 *  5. Recurse on the reduced matrix.
 *  6. If the recursion did not yield a valid solution:
 *      - Remove row r from the candidate solution.
 *      - Reinsert (uncover, see below) the chosen row and the columns it covers.
 *      - And try again with a different row (back to step 4).
 *
 * This algorithm uses a the Dancing Links technique implemented by \link DancingLinkNode
 * and \link DancingLinksMatrix to efficiently find an exact cover solution. The Dancing Links
 * technique relies on two key operations:
 *     - \link Solver::cover() The cover operation efficiently removes a row from the matrix
 *       upon including such a row in a candidate partial solution.
 *       Cover relies on the efficient O(1) remove operations in \link DancingLinkNode.
 *     - \link Solver::uncover() The uncover operation efficiently reinserts a row to the matrix
 *       when the algorithm backtracks to select a different row for a candidate partial solution.
 *       Uncover relies on the efficient O(1) reinsert operations in \link DancingLinkNode.
 */
class Solver {
 public:
  /**
   * Constructs a new Solver based on the given matrix.
   *
   * \param matrix A matrix modelling the exact cover problem to be solved.
   *               The solver owns the memory of this matrix after construction.
   */
  explicit Solver(std::unique_ptr<DancingLinksMatrix> matrix);

  /**
   * Solve for the provided matrix using an Alogrithm X implementation.
   *
   * \return If no solution is found, std::nullopt is returned.
   *         If a solution is found, a Solution object is returned.
   */
  auto solve() -> std::optional<Solution>;

  /**
   * Cover a given row, marking it as predetermined input to the exact cover solution.
   *
   * Before finding a solution, one can setup a partial solution to use as a starting point.
   */
  void coverRow(DancingLinkNode *row_root);

 private:
  // The matrix modelling the exact cover problem
  std::unique_ptr<DancingLinksMatrix> matrix;

  // The (partial) solution constructed during solving
  Solution solution;

  /**
   * Algorithm X Step function in the solving algorithm.
   *
   * This function implements one recursive call, implementing the steps documented above
   * and calling search() recursively till a solution is found.
   *
   * \return if a solution has been found
   * \post if return is true, then this->solution holds a valid solution.
   */
  auto search() -> bool;

  /**
   * Helper method to find the column c with the lowest count.
   * Used during the search() step to deterministically select the next column to cover.
   */
  auto selectHeaderColumn() -> std::optional<DancingLinkHeader *>;

  /**
   * The cover operation efficiently removes a row from the matrix
   * upon including such a row in a candidate partial solution.
   * Cover relies on the efficient O(1) remove operations in \link DancingLinkNode.
   */
  static void cover(DancingLinkHeader *header);

  /**
   * The uncover operation efficiently reinserts a row to the matrix
   * when the algorithm backtracks to select a different row for a candidate partial solution.
   * Uncover relies on the efficient O(1) reinsert operations in \link DancingLinkNode.
   */
  static void uncover(DancingLinkHeader *header);
};

}  // namespace dlx

#endif  // LIBDLX_SOLVER_HPP_