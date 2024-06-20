package com.sudokusolver.libdlx;

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
 * Each node has weak references its four neighbors: left, right, up, down. Via
 * the remove and reinsert operators, a node can be removed and reinserted into
 * it's horizontal (left-right) or vertical (up-down) lists in O(1) time.
 *
 * A node also can contain a data element of any type. This data element can be
 * used as meta-data for bookkeeping when modelling exact cover problems.
 */
public class DancingLinkNode {

    /**
     * Constructs a new DancingLinkNode with a reference to the container header
     * node.
     *
     * @param header A reference to the header which will act as a container and own
     *               the memory of this DancingLinkNode.
     */
    public DancingLinkNode(DancingLinkHeader header) {
        this();
        this.setHeader(header);
    }

    /**
     * Protected constructor to enable correct DancingLinkHeader construction.
     */
    protected DancingLinkNode() {
        this.up = this;
        this.down = this;
        this.left = this;
        this.right = this;
    }

    /**
     * Set the header pointer
     */
    protected void setHeader(DancingLinkHeader header) {
        this.header = header;
    }

    /**
     * Helper method for combinig multiple nodes into a horizontally linked list.
     *
     * This convinience method will insert each node into the linked list starting
     * at root. The left/right references are updated to reflect the order of nodes
     * provided to this methd.
     */
    public static void makeRow(DancingLinkNode root, DancingLinkNode... nodes) {
        for (DancingLinkNode node : nodes) {
            node.insertLeftOf(root);
        }
    }

    /**
     * Inserts this node to the left of the given other node.
     *
     * If the other node is the root of a list, then this node will end up
     * effectively at the end of that list.
     *
     * @param other the node to which insert this left of.
     * @post other.left == this && this.right == other
     */
    public void insertLeftOf(DancingLinkNode other) {
        // Before:
        // ... <-> [other-left] <-> [other] <-> ...
        // After:
        // ... <-> [other-left] <-> [this] <-> [other] <-> ...

        // Make left neighbor of other point to this, and vice versa
        other.left.right = this;
        this.left = other.left;

        // Make other point to this, and vice versa
        other.left = this;
        this.right = other;
    }

    /**
     * Inserts this node up of the given other node.
     *
     * If the other node is the root of a list, then this node will end up
     * effectively at the end of that list.
     *
     * @param other the node to which insert this up of.
     * @post other.up == this && this.down == other
     */
    public void insertUpOf(DancingLinkNode other) {
        // Before:
        // ... <-> [other-up] <-> [other] <-> ...
        // After:
        // ... <-> [other-up] <-> [this] <-> [other] <-> ...

        // Make up neighbor of other point to this, and vice versa
        other.up.down = this;
        this.up = other.up;

        // Make other point to this, and vice versa
        other.up = this;
        this.down = other;
    }

    /**
     * Removes this node from the list of its left and right neighbors, but keeps a
     * reference to its old neighbors.
     *
     * This method uses the Dancing Links property to remove itself from a list in
     * O(1) while keeping a references to efficiently reinsert itself afterwards.
     *
     * This method is designed to facilitate in the \link Solver::_cover() action.
     * This method's reverse is \link DancingLinkNode::reinsertHorizontal() .
     *
     * @post the left neighbor point to the right neighbor, skipping this node.
     * @post this node keeps a reference to its left and right neighbors.
     */
    public void removeHorizontal() {
        // Before:
        // ... <-> [left] <-> [this] <-> [right] <-> ...
        // After:
        // ... <-> [left] <-> [right] <-> ...
        // With the references in this still intact:
        // [left] <- [this] -> [right]
        this.right.left = this.left;
        this.left.right = this.right;
    }

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
     * @post the up neighbor poinst to the down neighbor, skipping this node
     * @post this node keeps a reference to its up and down neighbors.
     */
    public void removeVertical() {
        // Before:
        // ... <-> [up] <-> [this] <-> [down] <-> ...
        // After:
        // ... <-> [up] <-> [down] <-> ...
        // With the references in this still intact:
        // [up] <- [this] -> [down]
        this.up.down = this.down;
        this.down.up = this.up;
    }

    /**
     * Reinserts this node to the list of its left and right neighbors.
     *
     * This method uses the Dancing Links property to reinsert itself to a list in
     * O(1) based on its existing references kept after it removed itself from that
     * list earlier.
     *
     * This method is designed to facilitate in the \link Solver::_uncover() action.
     * This method's reverse is \link DancingLinkNode::removeHorizontal() .
     *
     * @post the left neighbor and right neighbor again point to this node.
     */
    public void reinsertHorizontal() {
        // Before:
        // ... <-> [left] <-> [right] <-> ...
        // After:
        // ... <-> [left] <-> [this] <-> [right] <-> ...
        this.left.right = this;
        this.right.left = this;
    }

    /**
     * Reinserts this node to the list of its up and down neighbors.
     *
     * This method uses the Dancing Links property to reinsert itself to a list in
     * O(1) based on its existing references kept after it removed itself from that
     * list earlier.
     *
     * This method is designed to facilitate in the \link Solver::_uncover() action.
     * This method's reverse is \link DancingLinkNode::removeVertical() .
     *
     * @post the up neighbor and down neighbor again point to this node.
     */
    public void reinsertVertical() {
        // Before:
        // ... <-> [up] <-> [down] <-> ...
        // After:
        // ... <-> [up] <-> [this] <-> [down] <-> ...
        this.up.down = this;
        this.down.up = this;
    }

    /**
     * Returns the header acting as container for this node.
     */
    public DancingLinkHeader getHeader() {
        return this.header;
    }

    /**
     * Returns the left neighbor of this node in the horizontal doubly linked list.
     */
    public DancingLinkNode getLeft() {
        return this.left;
    }

    /**
     * Returns the right neighbor of this node in the horizontal doubly linked list.
     */
    public DancingLinkNode getRight() {
        return this.right;
    }

    /**
     * Returns the up neighbor of this node in the vertical doubly linked list.
     */
    public DancingLinkNode getUp() {
        return this.up;
    }

    /**
     * Returns the down neighbor of this node in the vertical doubly linked list.
     */
    public DancingLinkNode getDown() {
        return this.down;
    }

    /**
     * Returns the data element associated with this node. This data element can be
     * used as meta-data for bookkeeping when modelling exact cover problems.
     */
    public void setData(Object data) {
        this.data = data;
    }

    /**
     * Sets the data element associated with this node. This data element can be
     * used as meta-data for bookkeeping when modelling exact cover problems.
     */
    public Object getData() {
        return this.data;
    }

    // Reference to the header acting as container for this node.
    private DancingLinkHeader header;

    // References to the four neighbors in the two-dimensional doubly linked list.
    private DancingLinkNode up;
    private DancingLinkNode down;
    private DancingLinkNode left;
    private DancingLinkNode right;

    // Data associated with this node.
    // This data element can be used as meta-data for bookkeeping.
    private Object data;
}
