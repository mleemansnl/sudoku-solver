package libdlx

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
type DancingLinkNode interface {

	/**
	 * Inserts this node to the left of the given other node.
	 *
	 * If the other node is the root of a list, then this node will end up
	 * effectively at the end of that list.
	 *
	 * @param other the node to which insert this left of.
	 * @post other.left == this && this.right == other
	 */
	InsertLeftOf(DancingLinkNode)

	/**
	 * Inserts this node up of the given other node.
	 *
	 * If the other node is the root of a list, then this node will end up
	 * effectively at the end of that list.
	 *
	 * @param other the node to which insert this left of.
	 * @post other.up == this && this.down == other
	 */
	InsertUpOf(DancingLinkNode)

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
	RemoveHorizontal()

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
	RemoveVertical()

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
	ReinsertHorizontal()

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
	ReinsertVertical()

	/**
	 * Returns the header acting as container for this node.
	 */
	GetHeader() DancingLinkHeader

	/**
	 * Returns the left neighbor of this node in the horizontal doubly linked list.
	 */
	GetLeft() DancingLinkNode

	/**
	 * Returns the right neighbor of this node in the horizontal doubly linked list.
	 */
	GetRight() DancingLinkNode

	/**
	 * Returns the up neighbor of this node in the vertical doubly linked list.
	 */
	GetUp() DancingLinkNode

	/**
	 * Returns the down neighbor of this node in the vertical doubly linked list.
	 */
	GetDown() DancingLinkNode

	/**
	 * Returns the data element associated with this node. This data element can be
	 * used as meta-data for bookkeeping when modelling exact cover problems.
	 */
	SetData(interface{})

	/**
	 * Sets the data element associated with this node. This data element can be
	 * used as meta-data for bookkeeping when modelling exact cover problems.
	 */
	GetData() interface{}

	/**
	 * Sets the left neighbor of this node in the horizontal doubly linked list.
	 */
	setLeft(DancingLinkNode)

	/**
	 * Sets the right neighbor of this node in the horizontal doubly linked list.
	 */
	setRight(DancingLinkNode)

	/**
	 * Sets the up neighbor of this node in the vertical doubly linked list.
	 */
	setUp(DancingLinkNode)

	/**
	 * Sets the down neighbor of this node in the vertical doubly linked list.
	 */
	setDown(DancingLinkNode)
}

/**
 * Constructs a new DancingLinkNode with a reference to the container header
 * node.
 *
 * @param header A reference to the header which will act as a container and own
 *               the memory of this DancingLinkNode.
 */
func NewDancingLinkNode(header DancingLinkHeader) DancingLinkNode {
	node := newDancingLinkNode()
	node.header = header
	return node
}

/**
 * Helper method for combinig multiple nodes into a horizontally linked list.
 *
 * This convinience method will insert each node into the linked list starting
 * at root. The left/right references are updated to reflect the order of nodes
 * provided to this methd.
 */
func MakeRow(root DancingLinkNode, nodes ...DancingLinkNode) {
	for _, node := range nodes {
		node.InsertLeftOf(root)
	}
}

/**
 * Private constructor to enable correct DancingLinkHeader construction.
 */
func newDancingLinkNode() *dancingLinkNode {
	node := &dancingLinkNode{}
	node.up = node
	node.down = node
	node.left = node
	node.right = node
	return node
}

/**
 * Implementation for the DancingLinkNode interface
 */
type dancingLinkNode struct {
	// Reference to the header acting as container for this node.
	header DancingLinkHeader

	// References to the four neighbors in the two-dimensional doubly linked list.
	up    DancingLinkNode
	down  DancingLinkNode
	left  DancingLinkNode
	right DancingLinkNode

	// Data associated with this node.
	// This data element can be used as meta-data for bookkeeping.
	data interface{}
}

func (n *dancingLinkNode) InsertLeftOf(other DancingLinkNode) {
	// Before:
	// ... <-> [other-left] <-> [other] <-> ...
	// After:
	// ... <-> [other-left] <-> [this] <-> [other] <-> ...

	// Make left neighbor of other point to this, and vice versa
	other.GetLeft().setRight(n)
	n.setLeft(other.GetLeft())

	// Make other point to this, and vice versa
	other.setLeft(n)
	n.setRight(other)
}

func (n *dancingLinkNode) InsertUpOf(other DancingLinkNode) {
	// Before:
	// ... <-> [other-up] <-> [other] <-> ...
	// After:
	// ... <-> [other-up] <-> [this] <-> [other] <-> ...

	// Make up neighbor of other point to this, and vice versa
	other.GetUp().setDown(n)
	n.setUp(other.GetUp())

	// Make other point to this, and vice versa
	other.setUp(n)
	n.setDown(other)
}

func (n *dancingLinkNode) RemoveHorizontal() {
	// Before:
	// ... <-> [left] <-> [this] <-> [right] <-> ...
	// After:
	// ... <-> [left] <-> [right] <-> ...
	// With the references in this still intact:
	// [left] <- [this] -> [right]
	n.GetRight().setLeft(n.GetLeft())
	n.GetLeft().setRight(n.GetRight())
}

func (n *dancingLinkNode) RemoveVertical() {
	// Before:
	// ... <-> [up] <-> [this] <-> [down] <-> ...
	// After:
	// ... <-> [up] <-> [down] <-> ...
	// With the references in this still intact:
	// [up] <- [this] -> [down]
	n.GetUp().setDown(n.GetDown())
	n.GetDown().setUp(n.GetUp())
}

func (n *dancingLinkNode) ReinsertHorizontal() {
	// Before:
	// ... <-> [left] <-> [right] <-> ...
	// After:
	// ... <-> [left] <-> [this] <-> [right] <-> ...
	n.GetLeft().setRight(n)
	n.GetRight().setLeft(n)
}

func (n *dancingLinkNode) ReinsertVertical() {
	// Before:
	// ... <-> [up] <-> [down] <-> ...
	// After:
	// ... <-> [up] <-> [this] <-> [down] <-> ...
	n.GetUp().setDown(n)
	n.GetDown().setUp(n)
}

func (n *dancingLinkNode) GetHeader() DancingLinkHeader {
	return n.header
}

func (n *dancingLinkNode) GetLeft() DancingLinkNode {
	return n.left
}

func (n *dancingLinkNode) GetRight() DancingLinkNode {
	return n.right
}

func (n *dancingLinkNode) GetUp() DancingLinkNode {
	return n.up
}

func (n *dancingLinkNode) GetDown() DancingLinkNode {
	return n.down
}

func (n *dancingLinkNode) SetData(data interface{}) {
	n.data = data
}

func (n *dancingLinkNode) GetData() interface{} {
	return n.data
}

/**
 * Sets the left neighbor of this node in the horizontal doubly linked list.
 */
func (n *dancingLinkNode) setLeft(o DancingLinkNode) {
	n.left = o
}

/**
 * Sets the right neighbor of this node in the horizontal doubly linked list.
 */
func (n *dancingLinkNode) setRight(o DancingLinkNode) {
	n.right = o
}

/**
 * Sets the up neighbor of this node in the vertical doubly linked list.
 */
func (n *dancingLinkNode) setUp(o DancingLinkNode) {
	n.up = o
}

/**
 * Sets the down neighbor of this node in the vertical doubly linked list.
 */
func (n *dancingLinkNode) setDown(o DancingLinkNode) {
	n.down = o
}
