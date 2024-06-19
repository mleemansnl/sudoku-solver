package libdlx_test

import (
	"testing"

	"github.com/mleemansnl/sudoku-solver/src/go/libdlx"
	"github.com/stretchr/testify/suite"
)

type DancingLinkNodeTestSuite struct {
	suite.Suite
}

func TestDancingLinkNodeTestSuite(t *testing.T) {
	suite.Run(t, new(DancingLinkNodeTestSuite))
}

func (suite *DancingLinkNodeTestSuite) TestConstructor() {
	node := libdlx.NewDancingLinkNode(nil)

	suite.Equal(node.GetLeft(), node)
}
