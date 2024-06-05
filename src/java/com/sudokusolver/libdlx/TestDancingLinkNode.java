package java.com.sudokusolver.libdlx;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class TestDancingLinkNode {

    @Test
    public void testConstructor() throws Exception {
        DancingLinkNode node;

        assertEquals(node, node.getLeft());
        assertEquals(node, node.getRight());
        assertEquals(node, node.getUp());
        assertEquals(node, node.getDown());
    }
}
