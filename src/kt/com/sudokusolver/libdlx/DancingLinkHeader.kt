package com.sudokusolver.libdlx

class DancingLinkHeader: DancingLinkNode() {

    // Counter for the number of nodes in this header's vertical list.
    var count: Int = 0
        private set

    init {
        this.header = this
    }

    /**
     * Instantiates a new DancingLinkNode and inserts it into the vertical list of
     * this header.
     *
     * This header will own the memory to the new node and a weak reference is
     * returned for further setup of the new node.
     *
     * @post the new node is added to the end of the vertical list (this.up == new
     *       node)
     * @post count is increased to reflect this header containing an additional node
     */
    fun newNode() : DancingLinkNode {
        var node = DancingLinkNode(this)
        node.insertUpOf(this)
        this.incCount()
        return node
    }

    /**
     * Increases the vertical list counter
     */
    fun incCount() {
        this.count++
    }

    /**
     * Decreases the vertical list counter
     */
    fun decCount() {
        this.count--
    }
}