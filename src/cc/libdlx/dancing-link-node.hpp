#ifndef LIBDLX_DANCING_LINK_NODE_HPP_
#define LIBDLX_DANCING_LINK_NODE_HPP_

#include <any>
#include <concepts>
#include <memory>
#include <vector>

namespace dlx {
class DancingLinkHeader;

/**
 * The DancingLinkNode represents a single node in the Dancing Links
 * datastructure.
 *
 * The Dancing Links data structure (ref:
 * https://en.wikipedia.org/wiki/Dancing_Links) is an two-dimensional circular
 * doubly linked list with efficient remove/reinsert operations. It is used in
 * the Alogrithm X implementation of \link dlx::Solver .
 *
 * Nodes are typically created via \link DancingLinkHeader::newNode() instead of
 * instantiated directly. By creating nodes via DancingLinkHeader, the header
 * can own the memory for its nodes. See also \link DancingLinkHeader.
 *
 * Each node has weak references its four neighbors: left, right, up, down.
 * Via the remove and reinsert operators, a node can be removed and reinserted
 * into it's horizontal (left-right) or vertical (up-down) lists in O(1) time.
 *
 * A node also can contain a data element of any type.
 * This data element can be used as meta-data for bookkeeping
 * when modelling exact cover problems.
 */
class DancingLinkNode {
 public:
  /**
   * Constructs a new DancingLinkNode with a weak reference to the container
   * header node.
   *
   * \param header A weak reference to the header which will act as a container
   *               and own the memory of this DancingLinkNode.
   */
  explicit DancingLinkNode(DancingLinkHeader *header);

  /**
   * Helper method for combinig multiple nodes into a horizontally linked list.
   *
   * This convinience method will insert each node into the linked list starting
   * at root. The left/right references are updated to reflect the order of
   * nodes provided to this methd.
   */
  template <std::convertible_to<DancingLinkNode *>... Args>
  static void makeRow(DancingLinkNode *root, Args... nodes) {
    for (DancingLinkNode *node : {nodes...}) {
      node->insertLeftOf(root);
    }
  }

  /**
   * Inserts this node to the left of the given other node.
   *
   * If the other node is the root of a list, then this node
   * will end up effectively at the end of that list.
   *
   * \param other the node to which insert this left of.
   * \post other.left == this && this.right == other
   */
  void insertLeftOf(DancingLinkNode *other);

  /**
   * Inserts this node up of the given other node.
   *
   * If the other node is the root of a list, then this node
   * will end up effectively at the end of that list.
   *
   * \param other the node to which insert this up of.
   * \post other.up == this && this.down == other
   */
  void insertUpOf(DancingLinkNode *other);

  /**
   * Removes this node from the list of its left and right neighbors, but keeps
   * a reference to its old neighbors.
   *
   * This method uses the Dancing Links property to remove itself from a list in
   * O(1) while keeping a references to efficiently reinsert itself afterwards.
   *
   * This method is designed to facilitate in the \link Solver::_cover() action.
   * This method's reverse is \link DancingLinkNode::reinsertHorizontal() .
   *
   * \post the left neighbor point to the right neighbor, skipping this node.
   * \post this node keeps a reference to its left and right neighbors.
   */
  void removeHorizontal();

  /**
   * Removes this node from the list of its up and down neighbors, but keeps a
   * reference to its old neighbors.
   *
   * This method uses the Dancing Links property to remove itself from a list in
   * O(1) while keeping a references to efficiently reinsert itself afterwards.
   *
   * This method is designed to facilitate in the \link Solver::_cover() action.
   * This method's reverse is \link DancingLinkNode::reinsertVertical() .
   *
   * \post the up neighbor poinst to the down neighbor, skipping this node
   * \post this node keeps a reference to its up and down neighbors.
   */
  void removeVertical();

  /**
   * Reinserts this node to the list of its left and right neighbors.
   *
   * This method uses the Dancing Links property to reinsert itself to a list in
   * O(1) based on its existing references kept after it removed itself from
   * that list earlier.
   *
   * This method is designed to facilitate in the \link Solver::_uncover()
   * action. This method's reverse is \link DancingLinkNode::removeHorizontal()
   * .
   *
   * \post the left neighbor and right neighbor again point to this node.
   */
  void reinsertHorizontal();

  /**
   * Reinserts this node to the list of its up and down neighbors.
   *
   * This method uses the Dancing Links property to reinsert itself to a list in
   * O(1) based on its existing references kept after it removed itself from
   * that list earlier.
   *
   * This method is designed to facilitate in the \link Solver::_uncover()
   * action. This method's reverse is \link DancingLinkNode::removeVertical() .
   *
   * \post the up neighbor and down neighbor again point to this node.
   */
  void reinsertVertical();

  /**
   * Returns the header acting as container for this node.
   */
  auto getHeader() -> DancingLinkHeader *;

  /**
   * Returns the left neighbor of this node in the horizontal doubly linked
   * list.
   */
  auto getLeft() -> DancingLinkNode *;

  /**
   * Returns the right neighbor of this node in the horizontal doubly linked
   * list.
   */
  auto getRight() -> DancingLinkNode *;

  /**
   * Returns the up neighbor of this node in the vertical doubly linked list.
   */
  auto getUp() -> DancingLinkNode *;

  /**
   * Returns the down neighbor of this node in the vertical doubly linked list.
   */
  auto getDown() -> DancingLinkNode *;

  /**
   * Returns the data element associated with this node.
   * This data element can be used as meta-data for bookkeeping
   * when modelling exact cover problems.
   */
  void setData(std::any data);

  /**
   * Sets the data element associated with this node.
   * This data element can be used as meta-data for bookkeeping
   * when modelling exact cover problems.
   */
  auto getData() -> std::any;

 private:
  // Weak reference to the header acting as container for this node.
  DancingLinkHeader *header;

  // Weak references to the four neighbors in the two-dimensional doubly linked
  // list.
  DancingLinkNode *up, *down, *left, *right;

  // Data assocaited with this node.
  // This data element can be used as meta-data for bookkeeping.
  std::any data;
};

/**
 * The DancingLinkHeader represents a special header node in the Dancing Links
 * datastructure.
 *
 * Headers in the Dancing Links data structure (ref:
 * https://en.wikipedia.org/wiki/Dancing_Links) act as containers for the
 * purpose of owning the memory of the list nodes.
 *
 * A header keeps tracks of the number of nodes in it's vertical circular doubly
 * linked list. This count property is used the Alogrithm X implementation of
 * \link dlx::Solver to optimize on which covering to select. Note that the
 * usage of removeVertical/reinsertVertical intentionally do not update the
 * count property directly.
 *
 * Headers are typically created via \link DancingLinksMatrix::newHeader()
 * instead of instantiated directly. By creating nodes via DancingLinksMatrix,
 * the matrix can own the memory for its header nodes.
 */
class DancingLinkHeader : public DancingLinkNode {
 public:
  /**
   * Constructs a new 'empty' DancingLinkHeader.
   */
  DancingLinkHeader();

  /**
   * Instantiates a new DancingLinkNode and inserts it into the vertical list of
   * this header.
   *
   * This header will own the memory to the new node and a weak reference is
   * returned for further setup of the new node.
   *
   * \post the new node is added to the end of the vertical list (this.up == new
   * node) \post count is increased to reflect this header containing an
   * additional node
   */
  auto newNode() -> DancingLinkNode *;

  /**
   * Returns the number of nodes in this header's vertical list.
   */
  [[nodiscard]] auto getCount() const -> int;

  /**
   * Increases the vertical list counter
   */
  void incCount();

  /**
   * Decreases the vertical list counter
   */
  void decCount();

 private:
  // Counter for the number of nodes in this header's vertical list.
  int count;

  // Container for owning the memory of the list nodes.
  std::vector<std::unique_ptr<DancingLinkNode>> nodes;
};

}  // namespace dlx

#endif  // LIBDLX_DANCING_LINK_NODE_HPP_