package libdlx_test

import (
	"testing"

	"github.com/mleemansnl/sudoku-solver/src/go/libdlx"
	"github.com/stretchr/testify/suite"
)

// Test Suite: DancingLinkNode and DancingLinkHeader
type DancingLinkNodeTestSuite struct {
	suite.Suite
}

// Go run entry to testify test suite
func TestDancingLinkNodeTestSuite(t *testing.T) {
	suite.Run(t, new(DancingLinkNodeTestSuite))
}

/**
 * Test: constructor sets-up doubly-linked lists initial state correctly
 */
func (suite *DancingLinkNodeTestSuite) TestConstructor() {
	node := libdlx.NewDancingLinkNode(nil)

	suite.Equal(node, node.GetLeft())
	suite.Equal(node, node.GetRight())
	suite.Equal(node, node.GetUp())
	suite.Equal(node, node.GetDown())
}

/**
 * Test: Test correctness of insertLeftOf, removeHorizontal, and
 * reinsertHorizontal methods
 */
func (suite *DancingLinkNodeTestSuite) TestHorizontalList() {
	node1 := libdlx.NewDancingLinkNode(nil)
	node2 := libdlx.NewDancingLinkNode(nil)
	node3 := libdlx.NewDancingLinkNode(nil)

	node2.InsertLeftOf(node1)
	node3.InsertLeftOf(node1)

	suite.Equal(node2, node1.GetRight())
	suite.Equal(node3, node2.GetRight())
	suite.Equal(node1, node3.GetRight())

	suite.Equal(node3, node1.GetLeft())
	suite.Equal(node1, node2.GetLeft())
	suite.Equal(node2, node3.GetLeft())

	node2.RemoveHorizontal()

	suite.Equal(node3, node1.GetRight())
	suite.Equal(node3, node2.GetRight())
	suite.Equal(node1, node3.GetRight())

	suite.Equal(node3, node1.GetLeft())
	suite.Equal(node1, node2.GetLeft())
	suite.Equal(node1, node3.GetLeft())

	node2.ReinsertHorizontal()

	suite.Equal(node2, node1.GetRight())
	suite.Equal(node3, node2.GetRight())
	suite.Equal(node1, node3.GetRight())

	suite.Equal(node3, node1.GetLeft())
	suite.Equal(node1, node2.GetLeft())
	suite.Equal(node2, node3.GetLeft())
}

/**
 * Test: Test correctness of makeRow convenience method
 */
func (suite *DancingLinkNodeTestSuite) TestHorizontalListConstructor() {
	node1 := libdlx.NewDancingLinkNode(nil)
	node2 := libdlx.NewDancingLinkNode(nil)
	node3 := libdlx.NewDancingLinkNode(nil)

	libdlx.MakeRow(node1, node2, node3)

	suite.Equal(node2, node1.GetRight())
	suite.Equal(node3, node2.GetRight())
	suite.Equal(node1, node3.GetRight())

	suite.Equal(node3, node1.GetLeft())
	suite.Equal(node1, node2.GetLeft())
	suite.Equal(node2, node3.GetLeft())
}

/**
 * Test: Test correctness of insertUpOf, removeVertical, and reinsertVertical
 * methods
 */
func (suite *DancingLinkNodeTestSuite) TestVerticalList() {
	node1 := libdlx.NewDancingLinkNode(nil)
	node2 := libdlx.NewDancingLinkNode(nil)
	node3 := libdlx.NewDancingLinkNode(nil)

	node2.InsertUpOf(node1)
	node3.InsertUpOf(node1)

	suite.Equal(node2, node1.GetDown())
	suite.Equal(node3, node2.GetDown())
	suite.Equal(node1, node3.GetDown())

	suite.Equal(node3, node1.GetUp())
	suite.Equal(node1, node2.GetUp())
	suite.Equal(node2, node3.GetUp())

	node2.RemoveVertical()

	suite.Equal(node3, node1.GetDown())
	suite.Equal(node3, node2.GetDown())
	suite.Equal(node1, node3.GetDown())

	suite.Equal(node3, node1.GetUp())
	suite.Equal(node1, node2.GetUp())
	suite.Equal(node1, node3.GetUp())

	node2.ReinsertVertical()

	suite.Equal(node2, node1.GetDown())
	suite.Equal(node3, node2.GetDown())
	suite.Equal(node1, node3.GetDown())

	suite.Equal(node3, node1.GetUp())
	suite.Equal(node1, node2.GetUp())
	suite.Equal(node2, node3.GetUp())
}

func (suite *DancingLinkNodeTestSuite) TestHeaderConstructor() {
	header := libdlx.NewDancingLinkHeader()
	var node1 libdlx.DancingLinkNode = header
	node2 := header.NewNode()
	node3 := header.NewNode()

	suite.Equal(node2, node1.GetDown())
	suite.Equal(node3, node2.GetDown())
	suite.Equal(node1, node3.GetDown())

	suite.Equal(node3, node1.GetUp())
	suite.Equal(node1, node2.GetUp())
	suite.Equal(node2, node3.GetUp())

	node2.RemoveVertical()

	suite.Equal(node3, node1.GetDown())
	suite.Equal(node3, node2.GetDown())
	suite.Equal(node1, node3.GetDown())

	suite.Equal(node3, node1.GetUp())
	suite.Equal(node1, node2.GetUp())
	suite.Equal(node1, node3.GetUp())

	node2.ReinsertVertical()

	suite.Equal(node2, node1.GetDown())
	suite.Equal(node3, node2.GetDown())
	suite.Equal(node1, node3.GetDown())

	suite.Equal(node3, node1.GetUp())
	suite.Equal(node1, node2.GetUp())
	suite.Equal(node2, node3.GetUp())
}
