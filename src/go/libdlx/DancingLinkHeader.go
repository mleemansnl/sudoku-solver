package libdlx

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
 *
 * \link dlx::Solver to optimize on which covering to select. Note that the
 * usage of removeVertical/reinsertVertical intentionally do not update the
 * count property directly.
 *
 * Headers are typically created via @link DancingLinksMatrix::newHeader()
 * instead of instantiated directly. By creating nodes via DancingLinksMatrix,
 * the matrix can own the memory for its header nodes.
 */
type DancingLinkHeader interface {
	// A DancingLinkHeader also embeds a DancingLinkNode
	DancingLinkNode

	/**
	 * Instantiates a new DancingLinkNode and inserts it into the vertical list of
	 * this header.
	 *
	 * This header will own the memory to the new node and a weak reference is
	 * returned for further setup of the new node.
	 *
	 * @post the new node is added to the end of the vertical list (this.up == new
	 *       node)
	 * @post count is increased to reflect this header containing an additional node
	 */
	NewNode() DancingLinkNode

	/**
	 * Returns the number of nodes in this header's vertical list.
	 */
	GetCount() int

	/**
	 * Increases the vertical list counter
	 */
	IncCount()

	/**
	 * Decreases the vertical list counter
	 */
	DecCount()
}

func NewDancingLinkHeader() DancingLinkHeader {
	header := &dancingLinkHeader{}
	header.header = header
	header.up = header
	header.down = header
	header.left = header
	header.right = header
	return header
}

/**
 * Implementation for the DancingLinkHeader interface
 */
type dancingLinkHeader struct {
	// A dancingLinkHeader also embeds a dancingLinkNode
	dancingLinkNode

	// Counter for the number of nodes in this header's vertical list.
	count int
}

func (h *dancingLinkHeader) NewNode() DancingLinkNode {
	node := NewDancingLinkNode(h)
	node.InsertUpOf(h)
	h.IncCount()
	return node
}

func (h *dancingLinkHeader) GetCount() int {
	return h.count
}

func (h *dancingLinkHeader) IncCount() {
	h.count++
}

func (h *dancingLinkHeader) DecCount() {
	h.count--
}
