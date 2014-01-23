/**
 *
 */
package main.java.cz.cvut.fel.minimalSpanningTree.dataStructures.implementation;

import main.java.cz.cvut.fel.minimalSpanningTree.core.Application;
import main.java.cz.cvut.fel.minimalSpanningTree.dataStructures.PriorityQueue;
import main.java.cz.cvut.fel.minimalSpanningTree.dataStructures.implementation.support.Edge;

import org.apache.log4j.Logger;
import org.apache.log4j.xml.DOMConfigurator;

/**
 * Binary Heap implementation of Priority Queue
 * @author Shaki
 * @since May 2009
 *
 */
public class BinaryHeap implements PriorityQueue {
    /**
     * for logging purposes
     */
    static Logger logger = Logger.getLogger(BinaryHeap.class.getClass());

    /**
     * size of the Binary Heap
     */
    private int size;

    /**
     * Data (Edges) of the Binary Heap (mapped to array)
     */
    private Edge[] data;

    /**
     * Constructs the Binary Heap
     *
     * @param size size of the heap
     */
    public BinaryHeap(int size) {
        this.data = new Edge[size];
        setSize(0);
    }

    /* (non-Javadoc)
     * @see cz.cvut.fel.minimalSpanningTree.dataStructures.PriorityQueue#isEmpty()
     */
    @Override
    public boolean isEmpty() {
        return (getSize() == 0);
    }

    /* (non-Javadoc)
     * @see cz.cvut.fel.minimalSpanningTree.dataStructures.PriorityQueue#add(cz.cvut.fel.minimalSpanningTree.dataStructures.implementation.support.Edge)
     */
    @Override
    public void insert(Edge e) {

        /**
         * Rozne nazvy pre "upratanie haldy":
         * "sifting", "trickle", "heapify", "bubble" or "percolate"
         */
        if (getSize() == this.data.length)
            logger.error("Binary Heap is already full");
        else {
            setSize(getSize() + 1);
            setData(e,(getSize() - 1));
            heapifyUp(getSize() - 1);
        }
    }

    /* (non-Javadoc)
     * @see cz.cvut.fel.minimalSpanningTree.dataStructures.PriorityQueue#remove()
     */
    @Override
    public Edge extractMin() {
        Edge minE = null;

        if (isEmpty())
            logger.error("Binary Heap is empty, can't do remove");
        else {
            minE = getData(0);
            setData(getData(getSize()-1),0);
            setSize(getSize() - 1);
            if (getSize() > 0)
                heapifyDown(0);

        }

        return minE;
    }

    /**
     * Updates old edge in binary heap with the new one and places it in the
     * correct place
     *
     * @param oE oldEdge to be updated
     * @param nE newEdge to be set in place of the oldEdge
     */
    public void decreaseKey(Edge oE, Edge nE) {

        oE.setSource(nE.getSource());
        oE.setDestination(nE.getDestination());
        oE.setLength(nE.getLength());
        heapifyUp(getIndex(oE));
    }

    /**
     * Prints binary heap as a string
     * @return String representation of the Binary Heap
     */
    public String printHeap() {
        String heap = "";
        for (int i=0; i<getSize(); i++)
            heap = heap + " " + getData(i).getLength() + " ";

        return heap;
    }

    /**
     * Get index of item in the heap
     * @param e edge which index we want
     * @return index of the edge, or -1 if edge was not found in the heap
     */
    private int getIndex(Edge e) {
        for (int i=0; i<getSize(); i++)
            if ( e == getData(i) )
                return i;

        // item not in heap
        logger.error("Edge e: " + e.getSource() + "-" + e.getDestination() + "(" + e.getLength() + ") is not in the heap");
        return -1;
    }

    /**
     * @param size the size to set
     */
    public void setSize(int size) {
        this.size = size;
    }

    /**
     * @return the size
     */
    public int getSize() {
        return this.size;
    }

    /**
     * @param e edge to be added
     * @param nodeIndex index in the array
     */
    public void setData(Edge e, int nodeIndex) {
        this.data[nodeIndex] = e;
    }

    /**
     * @param nodeIndex index of the edge in the array i want to get
     * @return edge
     */
    public Edge getData(int nodeIndex) {
        return this.data[nodeIndex];
    }

    /**
     * getter for the left child index
     * @param nodeIndex index of the node we want to find his left child index
     * @return index of the left child in array
     */
    private int getLeftChildIndex(int nodeIndex) {
        return 2 * nodeIndex + 1;
    }

    /**
     * getter for the right child index
     * @param nodeIndex index of the node we want to find his right child index
     * @return index of the right child in array
     */
    private int getRightChildIndex(int nodeIndex) {
        return 2 * nodeIndex + 2;
    }

    /**
     * getter for the parent child index
     * @param nodeIndex index of the node we want to find his parent index
     * @return index of the parent in array
     */
    private int getParentIndex(int nodeIndex) {
        return (nodeIndex - 1) / 2;
    }

    /**
     * Restore the order in broken Binary Heap after insetion of new element
     * Shift up the new element, while heap property is broken.
     * Shifting is done as following: compare node's value with parent's value.
     * If they are in wrong order, swap them.
     *
     * @param nodeIndex node to be shifted
     */
    private void heapifyUp(int nodeIndex) {
        int parentIndex;
        Edge tmp;

        if (nodeIndex != 0) {
              parentIndex = getParentIndex(nodeIndex);
              if (getData(parentIndex).getLength() > getData(nodeIndex).getLength()) {
                    // swap edges
                    tmp = getData(parentIndex);
                    setData(getData(nodeIndex),parentIndex);
                    setData(tmp, nodeIndex);

                    heapifyUp(parentIndex);
              }

        }
    }


    /**
     * Removal operation uses the same idea as was used for insertion.
     * Root's value, which is minimal by the heap property,
     * is replaced by the last array's value.
     * Then new value is sifted down, until it takes right position.
     *
     * @param nodeIndex node to be shifted
     */
    private void heapifyDown(int nodeIndex) {
        int leftChildIndex, rightChildIndex, minIndex;
        Edge tmp;

        leftChildIndex = getLeftChildIndex(nodeIndex);
        rightChildIndex = getRightChildIndex(nodeIndex);

        if (rightChildIndex >= getSize()) {
            if (leftChildIndex >= getSize())
                return;
            minIndex = leftChildIndex;
        }
        else {
            if (getData(leftChildIndex).getLength() < getData(rightChildIndex).getLength())
                minIndex = leftChildIndex;
            else
                minIndex = rightChildIndex;
        }

        if (getData(nodeIndex).getLength() > getData(minIndex).getLength()) {
            // swap edges
            tmp = getData(minIndex);
            setData(getData(nodeIndex),minIndex);
            setData(tmp,nodeIndex);

            heapifyDown(minIndex);
        }
    }

    /**
     * for testing purposes
     * @param args no arguments
     */
    @SuppressWarnings("boxing")
    public static void main(String[] args) {

        // PropertyConfigurator.configure("log4j.properties");
        DOMConfigurator.configure("log4j.xml");
        logger = Logger.getLogger(Application.class.getName());
        logger.info("Binary Heap started.");

        // test haldy
        BinaryHeap bh = new BinaryHeap(15);
        Edge e;

        logger.info(bh.isEmpty());
        logger.info(bh.getSize());

        e = new Edge(0, 2, 9);
        bh.insert(e);

        e = new Edge(0, 2, 1);
        bh.insert(e);

        e = new Edge(0, 2, 8);
        bh.insert(e);

        e = new Edge(0, 2, 3);
        bh.insert(e);

        e = new Edge(0, 2, 4);
        bh.insert(e);

        e = new Edge(0, 2, 0);
        bh.insert(e);

        e = new Edge(0, 2, 7);
        bh.insert(e);

        e = new Edge(0, 2, 5);
        bh.insert(e);

        e = new Edge(0, 2, 6);
        bh.insert(e);

        logger.info(bh.isEmpty());
        logger.info(bh.getSize());
        logger.info(bh.printHeap());

        Edge nE = new Edge (3, 4, 1);
        bh.decreaseKey(e, nE);

        logger.info(bh.printHeap());

        logger.info(bh.extractMin().getLength());
        logger.info(bh.extractMin().getLength());
        logger.info(bh.extractMin().getLength());
        logger.info(bh.extractMin().getLength());
        logger.info(bh.extractMin().getLength());
        logger.info(bh.extractMin().getLength());
        logger.info(bh.extractMin().getLength());

        logger.info(bh.isEmpty());

    }
}
