package com.sudokusolver.libdlx;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

public class DancingLinkNodeTest {

    /**
     * Test: constructor sets-up doubly-linked lists initial state correctly
     */
    @Test
    void testConstructor() {
        DancingLinkNode node = new DancingLinkNode(null);

        assertEquals(node, node.getLeft());
        assertEquals(node, node.getRight());
        assertEquals(node, node.getUp());
        assertEquals(node, node.getDown());
    }

    /**
     * Test: Test correctness of insertLeftOf, removeHorizontal, and
     * reinsertHorizontal methods
     */
    @Test
    void testHorizontalList() {
        DancingLinkNode node1 = new DancingLinkNode(null);
        DancingLinkNode node2 = new DancingLinkNode(null);
        DancingLinkNode node3 = new DancingLinkNode(null);

        node2.insertLeftOf(node1);
        node3.insertLeftOf(node1);

        assertEquals(node2, node1.getRight());
        assertEquals(node3, node2.getRight());
        assertEquals(node1, node3.getRight());

        assertEquals(node3, node1.getLeft());
        assertEquals(node1, node2.getLeft());
        assertEquals(node2, node3.getLeft());

        node2.removeHorizontal();

        assertEquals(node3, node1.getRight());
        assertEquals(node3, node2.getRight());
        assertEquals(node1, node3.getRight());

        assertEquals(node3, node1.getLeft());
        assertEquals(node1, node2.getLeft());
        assertEquals(node1, node3.getLeft());

        node2.reinsertHorizontal();

        assertEquals(node2, node1.getRight());
        assertEquals(node3, node2.getRight());
        assertEquals(node1, node3.getRight());

        assertEquals(node3, node1.getLeft());
        assertEquals(node1, node2.getLeft());
        assertEquals(node2, node3.getLeft());
    }

    /**
     * Test: Test correctness of makeRow convenience method
     */
    @Test
    void testHorizontalListConstructor() {
        DancingLinkNode node1 = new DancingLinkNode(null);
        DancingLinkNode node2 = new DancingLinkNode(null);
        DancingLinkNode node3 = new DancingLinkNode(null);

        DancingLinkNode.makeRow(node1, node2, node3);

        assertEquals(node2, node1.getRight());
        assertEquals(node3, node2.getRight());
        assertEquals(node1, node3.getRight());

        assertEquals(node3, node1.getLeft());
        assertEquals(node1, node2.getLeft());
        assertEquals(node2, node3.getLeft());
    }

    /**
     * Test: Test correctness of insertUpOf, removeVertical, and reinsertVertical
     * methods
     */
    @Test
    void testVerticalList() {
        DancingLinkNode node1 = new DancingLinkNode(null);
        DancingLinkNode node2 = new DancingLinkNode(null);
        DancingLinkNode node3 = new DancingLinkNode(null);

        node2.insertUpOf(node1);
        node3.insertUpOf(node1);

        assertEquals(node2, node1.getDown());
        assertEquals(node3, node2.getDown());
        assertEquals(node1, node3.getDown());

        assertEquals(node3, node1.getUp());
        assertEquals(node1, node2.getUp());
        assertEquals(node2, node3.getUp());

        node2.removeVertical();

        assertEquals(node3, node1.getDown());
        assertEquals(node3, node2.getDown());
        assertEquals(node1, node3.getDown());

        assertEquals(node3, node1.getUp());
        assertEquals(node1, node2.getUp());
        assertEquals(node1, node3.getUp());

        node2.reinsertVertical();

        assertEquals(node2, node1.getDown());
        assertEquals(node3, node2.getDown());
        assertEquals(node1, node3.getDown());

        assertEquals(node3, node1.getUp());
        assertEquals(node1, node2.getUp());
        assertEquals(node2, node3.getUp());
    }

    @Test
    void testHeaderConstructor() {
        DancingLinkHeader header = new DancingLinkHeader();
        DancingLinkNode node1 = header;
        DancingLinkNode node2 = header.newNode();
        DancingLinkNode node3 = header.newNode();

        assertEquals(node2, node1.getDown());
        assertEquals(node3, node2.getDown());
        assertEquals(node1, node3.getDown());

        assertEquals(node3, node1.getUp());
        assertEquals(node1, node2.getUp());
        assertEquals(node2, node3.getUp());

        node2.removeVertical();

        assertEquals(node3, node1.getDown());
        assertEquals(node3, node2.getDown());
        assertEquals(node1, node3.getDown());

        assertEquals(node3, node1.getUp());
        assertEquals(node1, node2.getUp());
        assertEquals(node1, node3.getUp());

        node2.reinsertVertical();

        assertEquals(node2, node1.getDown());
        assertEquals(node3, node2.getDown());
        assertEquals(node1, node3.getDown());

        assertEquals(node3, node1.getUp());
        assertEquals(node1, node2.getUp());
        assertEquals(node2, node3.getUp());
    }
}
