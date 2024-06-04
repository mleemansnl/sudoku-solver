#include "dancing-link-node.hpp"

#include "gtest/gtest.h"

namespace dlx {
namespace {
/**
 * Test: constructor sets-up doubly-linked lists initial state correctly
 */
TEST(DancingLinkNode, Constructor) {
  auto node = std::make_unique<DancingLinkNode>(nullptr);

  EXPECT_EQ(node.get(), node->getLeft());
  EXPECT_EQ(node.get(), node->getRight());
  EXPECT_EQ(node.get(), node->getUp());
  EXPECT_EQ(node.get(), node->getDown());
}

/**
 * Test: Test correctness of insertLeftOf, removeHorizontal, and reinsertHorizontal methods
 */
TEST(DancingLinkNode, HorizontalList) {
  auto node1 = std::make_unique<DancingLinkNode>(nullptr);
  auto node2 = std::make_unique<DancingLinkNode>(nullptr);
  auto node3 = std::make_unique<DancingLinkNode>(nullptr);

  node2->insertLeftOf(node1.get());
  node3->insertLeftOf(node1.get());

  EXPECT_EQ(node2.get(), node1->getRight());
  EXPECT_EQ(node3.get(), node2->getRight());
  EXPECT_EQ(node1.get(), node3->getRight());

  EXPECT_EQ(node3.get(), node1->getLeft());
  EXPECT_EQ(node1.get(), node2->getLeft());
  EXPECT_EQ(node2.get(), node3->getLeft());

  node2->removeHorizontal();

  EXPECT_EQ(node3.get(), node1->getRight());
  EXPECT_EQ(node3.get(), node2->getRight());
  EXPECT_EQ(node1.get(), node3->getRight());

  EXPECT_EQ(node3.get(), node1->getLeft());
  EXPECT_EQ(node1.get(), node2->getLeft());
  EXPECT_EQ(node1.get(), node3->getLeft());

  node2->reinsertHorizontal();

  EXPECT_EQ(node2.get(), node1->getRight());
  EXPECT_EQ(node3.get(), node2->getRight());
  EXPECT_EQ(node1.get(), node3->getRight());

  EXPECT_EQ(node3.get(), node1->getLeft());
  EXPECT_EQ(node1.get(), node2->getLeft());
  EXPECT_EQ(node2.get(), node3->getLeft());
}

/**
 * Test: Test correctness of makeRow convenience method
 */
TEST(DancingLinkNode, HorizontalListConstructor) {
  auto node1 = std::make_unique<DancingLinkNode>(nullptr);
  auto node2 = std::make_unique<DancingLinkNode>(nullptr);
  auto node3 = std::make_unique<DancingLinkNode>(nullptr);

  DancingLinkNode::makeRow(node1.get(), node2.get(), node3.get());

  EXPECT_EQ(node2.get(), node1->getRight());
  EXPECT_EQ(node3.get(), node2->getRight());
  EXPECT_EQ(node1.get(), node3->getRight());

  EXPECT_EQ(node3.get(), node1->getLeft());
  EXPECT_EQ(node1.get(), node2->getLeft());
  EXPECT_EQ(node2.get(), node3->getLeft());
}

/**
 * Test: Test correctness of insertUpOf, removeVertical, and reinsertVertical methods
 */
TEST(DancingLinkNode, VerticalList) {
  auto node1 = std::make_unique<DancingLinkNode>(nullptr);
  auto node2 = std::make_unique<DancingLinkNode>(nullptr);
  auto node3 = std::make_unique<DancingLinkNode>(nullptr);

  node2->insertUpOf(node1.get());
  node3->insertUpOf(node1.get());

  EXPECT_EQ(node2.get(), node1->getDown());
  EXPECT_EQ(node3.get(), node2->getDown());
  EXPECT_EQ(node1.get(), node3->getDown());

  EXPECT_EQ(node3.get(), node1->getUp());
  EXPECT_EQ(node1.get(), node2->getUp());
  EXPECT_EQ(node2.get(), node3->getUp());

  node2->removeVertical();

  EXPECT_EQ(node3.get(), node1->getDown());
  EXPECT_EQ(node3.get(), node2->getDown());
  EXPECT_EQ(node1.get(), node3->getDown());

  EXPECT_EQ(node3.get(), node1->getUp());
  EXPECT_EQ(node1.get(), node2->getUp());
  EXPECT_EQ(node1.get(), node3->getUp());

  node2->reinsertVertical();

  EXPECT_EQ(node2.get(), node1->getDown());
  EXPECT_EQ(node3.get(), node2->getDown());
  EXPECT_EQ(node1.get(), node3->getDown());

  EXPECT_EQ(node3.get(), node1->getUp());
  EXPECT_EQ(node1.get(), node2->getUp());
  EXPECT_EQ(node2.get(), node3->getUp());
}

TEST(DancingLinkNode, HeaderConstructor) {
  std::unique_ptr<DancingLinkHeader> header = std::make_unique<DancingLinkHeader>();
  auto *node1 = header.get();
  auto *node2 = header->newNode();
  auto *node3 = header->newNode();

  EXPECT_EQ(node2, node1->getDown());
  EXPECT_EQ(node3, node2->getDown());
  EXPECT_EQ(node1, node3->getDown());

  EXPECT_EQ(node3, node1->getUp());
  EXPECT_EQ(node1, node2->getUp());
  EXPECT_EQ(node2, node3->getUp());

  node2->removeVertical();

  EXPECT_EQ(node3, node1->getDown());
  EXPECT_EQ(node3, node2->getDown());
  EXPECT_EQ(node1, node3->getDown());

  EXPECT_EQ(node3, node1->getUp());
  EXPECT_EQ(node1, node2->getUp());
  EXPECT_EQ(node1, node3->getUp());

  node2->reinsertVertical();

  EXPECT_EQ(node2, node1->getDown());
  EXPECT_EQ(node3, node2->getDown());
  EXPECT_EQ(node1, node3->getDown());

  EXPECT_EQ(node3, node1->getUp());
  EXPECT_EQ(node1, node2->getUp());
  EXPECT_EQ(node2, node3->getUp());
}

}  // namespace
}  // namespace dlx