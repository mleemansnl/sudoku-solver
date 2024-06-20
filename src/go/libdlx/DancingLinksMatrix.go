package libdlx

/**
 * The DancingLinksMatrix represents a sparse matrix using the Dancing Links
 * datastructure.
 *
 * This sparse matrix is based on the Dancing Links data structure (ref:
 * https://en.wikipedia.org/wiki/Dancing_Links) and models an exact cover
 * problem which can be efficiently solved using the Alogrithm X implementation
 * of \link dlx::Solver .
 *
 * The matrix consists of a header row, starting at the root header node, and
 * multiple node rows covering some subset of header nodes. Each header
 * typically models some condition or element to be covered in an exact cover
 * problem. The node rows model the '1' in this sparse matrix, and are
 * represented by \link DancingLinkNode linked to specific header nodes.
 *
 * This matrix directly owns all header nodes in the header row. A new header
 * node is created and added to the header row via \link
 * DancingLinksMatrix::newHeader() .
 */
type DancingLinksMatrix interface {
	/**
	 * Instantiates a new DancingLinkHeader and inserts it into the header row
	 * horizontal list.
	 *
	 * This matrix will own the memory to the new header node and a weak reference
	 * is returned for further setup of the new header node.
	 *
	 * @post the new node is added to the end of the header row list (root.left ==
	 *       new node)
	 */
	NewHeader() DancingLinkHeader

	/**
	 * Get the root header node, which represents the start of the header row list.
	 */
	GetRoot() DancingLinkHeader
}

/**
 * Constructs a new 'empty' matrix consisting of only a root header node.
 */
func NewDancingLinksMatrix() DancingLinksMatrix {
	return &dancingLinksMatrix{
		root: NewDancingLinkHeader(),
	}
}

type dancingLinksMatrix struct {
	// Root header row node, representing the start of the header row list.
	root DancingLinkHeader
}

func (m *dancingLinksMatrix) NewHeader() DancingLinkHeader {
	header := NewDancingLinkHeader()
	header.InsertLeftOf(m.root)
	return header
}

func (m *dancingLinksMatrix) GetRoot() DancingLinkHeader {
	return m.root
}
