from typing import Self


class DancingLinkNode:
    """
    The DancingLinkNode represents a single node in the Dancing Links datastructure.

    The Dancing Links data structure (ref: https://en.wikipedia.org/wiki/Dancing_Links)
    is an two-dimensional circular doubly linked list with efficient remove/reinsert
    operations. It is used in the Alogrithm X implementation of dlx::Solver .

    Nodes are typically created via DancingLinkHeader::newNode() instead of
    instantiated directly. By creating nodes via DancingLinkHeader, the header can
    own the memory for its nodes. See also DancingLinkHeader.

    Each node has weak references its four neighbors: left, right, up, down. Via the
    remove and reinsert operators, a node can be removed and reinserted into it's
    horizontal (left-right) or vertical (up-down) lists in O(1) time.

    A node also can contain a data element of any type. This data element can be
    used as meta-data for bookkeeping when modelling exact cover problems.
    """

    def __init__(self, header: "DancingLinkHeader") -> None:
        """
        Constructs a new DancingLinkNode with a reference to the container header node.

        Args:
         - header (DancingLinkHeader): A reference to the header which will act as
                                       a container for this DancingLinkNode.
        """

        # References to the four neighbors in the two-dimensional doubly linked list.
        self.up = self
        self.down = self
        self.left = self
        self.right = self

        self.header = header
        """Reference to the header acting as container for this node."""

        self.data = None
        """
        Data associated with this node.
        This data element can be used as meta-data for bookkeeping.
        """

    def insertLeftOf(self, other: Self) -> None:
        """
        Inserts this node to the left of the given other node.

        If the other node is the root of a list, then this node will end up
        effectively at the end of that list.

        Args:
         - other (DancingLinkNode): the node to which insert this left of.

        Postconditions:
         - other.left == this && this.right == other
        """
        # Before:
        # ... <-> [other-left] <-> [other] <-> ...
        # After:
        # ... <-> [other-left] <-> [this] <-> [other] <-> ...

        # Make left neighbor of other point to this, and vice versa
        other.left.right = self
        self.left = other.left

        # Make other point to this, and vice versa
        other.left = self
        self.right = other

    def insertUpOf(self, other: Self) -> None:
        """
        Inserts this node up of the given other node.

        If the other node is the root of a list, then this node will end up
        effectively at the end of that list.

        Args:
         - other (DancingLinkNode): the node to which insert this up of.

        Postconditions:
         - other.up == this && this.down == other
        """
        # Before:
        # ... <-> [other-up] <-> [other] <-> ...
        # After:
        # ... <-> [other-up] <-> [this] <-> [other] <-> ...

        # Make up neighbor of other point to this, and vice versa
        other.up.down = self
        self.up = other.up

        # Make other point to this, and vice versa
        other.up = self
        self.down = other

    def removeHorizontal(self) -> None:
        """
        Removes this node from the list of its left and right neighbors, but keeps a
        reference to its old neighbors.

        This method uses the Dancing Links property to remove itself from a list in
        O(1) while keeping a references to efficiently reinsert itself afterwards.

        This method is designed to facilitate in the Solver::_cover() action.
        This method's reverse is DancingLinkNode::reinsertHorizontal() .

        Postconditions:
         - the left neighbor point to the right neighbor, skipping this node.
         - this node keeps a reference to its left and right neighbors.
        """
        # Before:
        # ... <-> [left] <-> [this] <-> [right] <-> ...
        # After:
        # ... <-> [left] <-> [right] <-> ...
        # With the references in this still intact:
        # [left] <- [this] -> [right]
        self.right.left = self.left
        self.left.right = self.right

    def removeVertical(self) -> None:
        """
        Removes this node from the list of its up and down neighbors, but keeps a
        reference to its old neighbors.

        This method uses the Dancing Links property to remove itself from a list in
        O(1) while keeping a references to efficiently reinsert itself afterwards.

        This method is designed to facilitate in the Solver::_cover() action.
        This method's reverse is DancingLinkNode::reinsertVertical() .

        Postconditions:
         - the up neighbor poinst to the down neighbor, skipping this node
         - this node keeps a reference to its up and down neighbors.
        """
        # Before:
        # ... <-> [up] <-> [this] <-> [down] <-> ...
        # After:
        # ... <-> [up] <-> [down] <-> ...
        # With the references in this still intact:
        # [up] <- [this] -> [down]
        self.up.down = self.down
        self.down.up = self.up

    def reinsertHorizontal(self) -> None:
        """
        Reinserts this node to the list of its left and right neighbors.

        This method uses the Dancing Links property to reinsert itself to a list in
        O(1) based on its existing references kept after it removed itself from that
        list earlier.

        This method is designed to facilitate in the Solver::_uncover() action.
        This method's reverse is DancingLinkNode::removeHorizontal() .

        Postconditions:
         - the left neighbor and right neighbor again point to this node.
        """
        # Before:
        # ... <-> [left] <-> [right] <-> ...
        # After:
        # ... <-> [left] <-> [this] <-> [right] <-> ...
        self.left.right = self
        self.right.left = self

    def reinsertVertical(self) -> None:
        """
        Reinserts this node to the list of its up and down neighbors.

        This method uses the Dancing Links property to reinsert itself to a list in
        O(1) based on its existing references kept after it removed itself from that
        list earlier.

        This method is designed to facilitate in the Solver::_uncover() action.
        This method's reverse is DancingLinkNode::removeVertical() .

        Postconditions:
         - the up neighbor and down neighbor again point to this node.
        """
        # Before:
        # ... <-> [up] <-> [down] <-> ...
        # After:
        # ... <-> [up] <-> [this] <-> [down] <-> ...
        self.up.down = self
        self.down.up = self

    def getHeader(self) -> "DancingLinkHeader":
        """Returns the header acting as container for this node."""
        return self.header

    def getLeft(self) -> Self:
        """Returns the left neighbor of this node in the horizontal doubly linked list."""
        return self.left

    def getRight(self) -> Self:
        """Returns the right neighbor of this node in the horizontal doubly linked list."""
        return self.right

    def getUp(self) -> Self:
        """Returns the up neighbor of this node in the vertical doubly linked list."""
        return self.up

    def getDown(self) -> Self:
        """Returns the down neighbor of this node in the vertical doubly linked list."""
        return self.down

    def setData(self, data: object) -> None:
        """
        Sets the data element associated with this node. This data element can be
        used as meta-data for bookkeeping when modelling exact cover problems.
        """
        self.data = data

    def getData(self) -> object:
        """
        Returns the data element associated with this node. This data element can be
        used as meta-data for bookkeeping when modelling exact cover problems.
        """
        return self.data


from .DancingLinkHeader import DancingLinkHeader
