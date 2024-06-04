#include "dancing-link-node.hpp"

// https://en.wikipedia.org/wiki/Dancing_Links
namespace dlx {

DancingLinkNode::DancingLinkNode(DancingLinkHeader *header)
    : header(header),
      up(this),
      down(this),
      left(this),
      right(this) {
}

void DancingLinkNode::insertLeftOf(DancingLinkNode *other) {
  // Before:
  //  ... <-> [other-left] <-> [other] <-> ...
  // After:
  //  ... <-> [other-left] <-> [this] <-> [other] <-> ...

  // Make left neighbor of other point to this, and vice versa
  other->left->right = this;
  this->left = other->left;

  // Make other point to this, and vice versa
  other->left = this;
  this->right = other;
}

void DancingLinkNode::insertUpOf(DancingLinkNode *other) {
  // Before:
  //  ... <-> [other-up] <-> [other] <-> ...
  // After:
  //  ... <-> [other-up] <-> [this] <-> [other] <-> ...

  // Make up neighbor of other point to this, and vice versa
  other->up->down = this;
  this->up = other->up;

  // Make other point to this, and vice versa
  other->up = this;
  this->down = other;
}

void DancingLinkNode::removeHorizontal() {
  // Before:
  //  ... <-> [left] <-> [this] <-> [right] <-> ...
  // After:
  //  ... <-> [left] <-> [right] <-> ...
  // With the references in this still intact:
  //           [left] <- [this] -> [right]
  this->right->left = this->left;
  this->left->right = this->right;
}

void DancingLinkNode::removeVertical() {
  // Before:
  //  ... <-> [up] <-> [this] <-> [down] <-> ...
  // After:
  //  ... <-> [up] <-> [down] <-> ...
  // With the references in this still intact:
  //           [up] <- [this] -> [down]
  this->up->down = this->down;
  this->down->up = this->up;
}

void DancingLinkNode::reinsertHorizontal() {
  // Before:
  //  ... <-> [left] <-> [right] <-> ...
  // After:
  //  ... <-> [left] <-> [this] <-> [right] <-> ...
  this->left->right = this;
  this->right->left = this;
}

void DancingLinkNode::reinsertVertical() {
  // Before:
  //  ... <-> [up] <-> [down] <-> ...
  // After:
  //  ... <-> [up] <-> [this] <-> [down] <-> ...
  this->up->down = this;
  this->down->up = this;
}

auto DancingLinkNode::getHeader() -> DancingLinkHeader * {
  return this->header;
}

auto DancingLinkNode::getLeft() -> DancingLinkNode * {
  return this->left;
}

auto DancingLinkNode::getRight() -> DancingLinkNode * {
  return this->right;
}

auto DancingLinkNode::getUp() -> DancingLinkNode * {
  return this->up;
}

auto DancingLinkNode::getDown() -> DancingLinkNode * {
  return this->down;
}

void DancingLinkNode::setData(std::any data) {
  this->data = std::move(data);
}

auto DancingLinkNode::getData() -> std::any {
  return this->data;
}

DancingLinkHeader::DancingLinkHeader()
    : DancingLinkNode(this), count(0) {}

auto DancingLinkHeader::newNode() -> DancingLinkNode * {
  std::unique_ptr<DancingLinkNode> node = std::make_unique<DancingLinkNode>(this);
  node->insertUpOf(this);
  this->incCount();

  auto *nodePtr = node.get();
  this->nodes.push_back(std::move(node));
  return nodePtr;
}

auto DancingLinkHeader::getCount() const -> int {
  return this->count;
}

void DancingLinkHeader::incCount() {
  this->count++;
}

void DancingLinkHeader::decCount() {
  this->count--;
}

}  // namespace dlx