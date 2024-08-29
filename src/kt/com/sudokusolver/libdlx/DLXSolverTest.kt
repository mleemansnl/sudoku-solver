package com.sudokusolver.libdlx

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Test

public class DLXSolverTest {

    /**
     * Test: Solver can solve basic exact cover example problem.
     */
    @Test
    fun testBasic() {
        // Solve the Basic Example from https://en.wikipedia.org/wiki/Exact_cover

        // Define matrix
        val matrix = DancingLinksMatrix()

        // These headers represent covering X = {1,2,3,4}
        val header1 = matrix.newHeader()
        val header2 = matrix.newHeader()
        val header3 = matrix.newHeader()
        val header4 = matrix.newHeader()

        // These rows represent subcollections S = {O,P,E} , where:
        // O = {1,3}
        val node_o1 = header1.newNode()
        val node_o3 = header3.newNode()
        DancingLinkNode.makeRow(node_o1, node_o3)

        // P = {1,2,3}
        val node_p1 = header1.newNode()
        val node_p2 = header2.newNode()
        val node_p3 = header3.newNode()
        DancingLinkNode.makeRow(node_p1, node_p2, node_p3)

        // E = {2,4}
        val node_e2 = header2.newNode()
        val node_e4 = header4.newNode()
        DancingLinkNode.makeRow(node_e2, node_e4)

        // Solve for the above matrix
        val solver = DLXSolver(matrix)
        val result = solver.solve()

        // Check solution
        assertNotNull(result)
        if (result != null) {
            assertEquals(2, result.size)
            assertEquals(node_o1, result[1])
            assertEquals(node_e4, result[0])
        }
    }
}
