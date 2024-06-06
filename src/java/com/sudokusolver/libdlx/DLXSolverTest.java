package com.sudokusolver.libdlx;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;

public class DLXSolverTest {

    /**
     * Test: Solver can solve basic exact cover example problem.
     */
    @Test
    void testBasic() {
        // Solve the Basic Example from https://en.wikipedia.org/wiki/Exact_cover

        // Define matrix
        DancingLinksMatrix matrix = new DancingLinksMatrix();

        // These headers represent covering X = {1,2,3,4}
        DancingLinkHeader header1 = matrix.newHeader();
        DancingLinkHeader header2 = matrix.newHeader();
        DancingLinkHeader header3 = matrix.newHeader();
        DancingLinkHeader header4 = matrix.newHeader();

        // These rows represent subcollections S = {O,P,E} , where:
        // O = {1,3}
        DancingLinkNode node_o1 = header1.newNode();
        DancingLinkNode node_o3 = header3.newNode();
        DancingLinkNode.makeRow(node_o1, node_o3);

        // P = {1,2,3}
        DancingLinkNode node_p1 = header1.newNode();
        DancingLinkNode node_p2 = header2.newNode();
        DancingLinkNode node_p3 = header3.newNode();
        DancingLinkNode.makeRow(node_p1, node_p2, node_p3);

        // E = {2,4}
        DancingLinkNode node_e2 = header2.newNode();
        DancingLinkNode node_e4 = header4.newNode();
        DancingLinkNode.makeRow(node_e2, node_e4);

        // Solve for the above matrix
        DLXSolver solver = new DLXSolver(matrix);
        Optional<List<DancingLinkNode>> result = solver.solve();

        // Check solution
        assertTrue(result.isPresent());
        if (result.isPresent()) {
            List<DancingLinkNode> solution = result.get();

            assertEquals(2, solution.size());
            assertEquals(node_o1, solution.get(1));
            assertEquals(node_e4, solution.get(0));
        }
    }
}
