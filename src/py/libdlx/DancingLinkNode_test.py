from .DancingLinkNode import DancingLinkNode


class TestDancingLinkNode:

    def test_constructor(self) -> None:
        """Test: constructor sets-up doubly-linked lists initial state correctly"""
        node = DancingLinkNode(None)

        assert node == node.getLeft()
        assert node == node.getRight()
        assert node == node.getUp()
        assert node == node.getDown()
