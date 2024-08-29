package com.sudokusolver.libdlx

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class DancingLinkNodeTest {

    /**
     * Test: constructor sets-up doubly-linked lists initial state correctly
     */
    @Test
    fun testConstructor() {
       val node = DancingLinkNode(null)

        assertEquals(node, node.left)
        assertEquals(node, node.right)
        assertEquals(node, node.up)
        assertEquals(node, node.down)
    }

    /**
     * Test: Test correctness of insertLeftOf, removeHorizontal, and
     * reinsertHorizontal methods
     */
    @Test
    fun testHorizontalList() {
        val node1 = DancingLinkNode(null)
        val node2 = DancingLinkNode(null)
        val node3 = DancingLinkNode(null)

        node2.insertLeftOf(node1)
        node3.insertLeftOf(node1)

        assertEquals(node2, node1.right)
        assertEquals(node3, node2.right)
        assertEquals(node1, node3.right)

        assertEquals(node3, node1.left)
        assertEquals(node1, node2.left)
        assertEquals(node2, node3.left)

        node2.removeHorizontal()

        assertEquals(node3, node1.right)
        assertEquals(node3, node2.right)
        assertEquals(node1, node3.right)

        assertEquals(node3, node1.left)
        assertEquals(node1, node2.left)
        assertEquals(node1, node3.left)

        node2.reinsertHorizontal()

        assertEquals(node2, node1.right)
        assertEquals(node3, node2.right)
        assertEquals(node1, node3.right)

        assertEquals(node3, node1.left)
        assertEquals(node1, node2.left)
        assertEquals(node2, node3.left)
    }

    /**
     * Test: Test correctness of makeRow convenience method
     */
    @Test
    fun testHorizontalListConstructor() {
        val node1 = DancingLinkNode(null)
        val node2 = DancingLinkNode(null)
        val node3 = DancingLinkNode(null)

        DancingLinkNode.makeRow(node1, node2, node3)

        assertEquals(node2, node1.right)
        assertEquals(node3, node2.right)
        assertEquals(node1, node3.right)

        assertEquals(node3, node1.left)
        assertEquals(node1, node2.left)
        assertEquals(node2, node3.left)
    }

    /**
     * Test: Test correctness of insertUpOf, removeVertical, and reinsertVertical
     * methods
     */
    @Test
    fun testVerticalList() {
        val node1 = DancingLinkNode(null)
        val node2 = DancingLinkNode(null)
        val node3 = DancingLinkNode(null)

        node2.insertUpOf(node1)
        node3.insertUpOf(node1)

        assertEquals(node2, node1.down)
        assertEquals(node3, node2.down)
        assertEquals(node1, node3.down)

        assertEquals(node3, node1.up)
        assertEquals(node1, node2.up)
        assertEquals(node2, node3.up)

        node2.removeVertical()

        assertEquals(node3, node1.down)
        assertEquals(node3, node2.down)
        assertEquals(node1, node3.down)

        assertEquals(node3, node1.up)
        assertEquals(node1, node2.up)
        assertEquals(node1, node3.up)

        node2.reinsertVertical()

        assertEquals(node2, node1.down)
        assertEquals(node3, node2.down)
        assertEquals(node1, node3.down)

        assertEquals(node3, node1.up)
        assertEquals(node1, node2.up)
        assertEquals(node2, node3.up)
    }

    @Test
    fun testHeaderConstructor() {
        val header = DancingLinkHeader()
        val node1 = header
        val node2 = header.newNode()
        val node3 = header.newNode()

        assertEquals(node2, node1.down)
        assertEquals(node3, node2.down)
        assertEquals(node1, node3.down)

        assertEquals(node3, node1.up)
        assertEquals(node1, node2.up)
        assertEquals(node2, node3.up)

        node2.removeVertical()

        assertEquals(node3, node1.down)
        assertEquals(node3, node2.down)
        assertEquals(node1, node3.down)

        assertEquals(node3, node1.up)
        assertEquals(node1, node2.up)
        assertEquals(node1, node3.up)

        node2.reinsertVertical()

        assertEquals(node2, node1.down)
        assertEquals(node3, node2.down)
        assertEquals(node1, node3.down)

        assertEquals(node3, node1.up)
        assertEquals(node1, node2.up)
        assertEquals(node2, node3.up)
    }
}