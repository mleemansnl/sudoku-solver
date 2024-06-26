from .DancingLinkNode import DancingLinkNode


class DancingLinkHeader(DancingLinkNode):
    """
    The DancingLinkHeader represents a special header node in the Dancing Links
    datastructure.

    Headers in the Dancing Links data structure (ref:
    https://en.wikipedia.org/wiki/Dancing_Links) act as containers for the
    purpose of owning the memory of the list nodes.

    A header keeps tracks of the number of nodes in it's vertical circular doubly
    linked list. This count property is used the Alogrithm X implementation of

    dlx::Solver to optimize on which covering to select. Note that the
    usage of removeVertical/reinsertVertical intentionally do not update the
    count property directly.

    Headers are typically created via @link DancingLinksMatrix::newHeader()
    instead of instantiated directly. By creating nodes via DancingLinksMatrix,
    the matrix can own the memory for its header nodes.
    """

    def __init__(self):
        """Constructs a new 'empty' DancingLinkHeader."""
        DancingLinkNode.__init__(self, self)

        self.count = 0
        """Counter for the number of nodes in this header's vertical list."""

    def newNode(self) -> DancingLinkNode:
        """
        Instantiates a new DancingLinkNode and inserts it into the vertical list of
        this header.

        This header will own the memory to the new node and a weak reference is
        returned for further setup of the new node.

        Postconditions:
         - the new node is added to the end of the vertical list (this.up == new node)
         - count is increased to reflect this header containing an additional node
        """
        node = DancingLinkNode(self)
        node.insertUpOf(self)
        self.incCount()
        return node

    def getCount(self) -> int:
        """Returns the number of nodes in this header's vertical list."""
        return self.count

    def incCount(self) -> None:
        """Increases the vertical list counter"""
        self.count += 1

    def decCount(self) -> None:
        """Decreases the vertical list counter"""
        self.count -= 1
