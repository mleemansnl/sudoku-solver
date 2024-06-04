#include "solver.hpp"

#include "gtest/gtest.h"

namespace dlx {
namespace {

/**
 * Test: Solver can solve basic exact cover example problem.
 */
TEST(DLXSolver, Basic) {
  // Solve the Basic Example from https://en.wikipedia.org/wiki/Exact_cover

  // Define matrix
  std::unique_ptr<DancingLinksMatrix> matrix = std::make_unique<DancingLinksMatrix>();

  // These headers represent covering X = {1,2,3,4}
  auto *header1 = matrix->newHeader();
  auto *header2 = matrix->newHeader();
  auto *header3 = matrix->newHeader();
  auto *header4 = matrix->newHeader();

  // These rows represent subcollections S = {O,P,E} , where:
  //  O = {1,3}
  auto *node_o1 = header1->newNode();
  auto *node_o3 = header3->newNode();
  DancingLinkNode::makeRow(node_o1, node_o3);

  //  P = {1,2,3}
  auto *node_p1 = header1->newNode();
  auto *node_p2 = header2->newNode();
  auto *node_p3 = header3->newNode();
  DancingLinkNode::makeRow(node_p1, node_p2, node_p3);

  //  E = {2,4}
  auto *node_e2 = header2->newNode();
  auto *node_e4 = header4->newNode();
  DancingLinkNode::makeRow(node_e2, node_e4);

  // Solve for the above matrix
  Solver solver(std::move(matrix));
  auto result = solver.solve();

  // Check solution
  EXPECT_TRUE(result.has_value());
  if (result.has_value()) {
    auto solution = result.value();

    EXPECT_EQ(2, solution.size());
    EXPECT_EQ(node_o1, solution[1]);
    EXPECT_EQ(node_e4, solution[0]);
  }
}

}  // namespace
}  // namespace dlx