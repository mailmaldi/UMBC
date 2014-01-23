/**
 *
 */
package main.java.cz.cvut.fel.minimalSpanningTree.dataStructures;

import main.java.cz.cvut.fel.minimalSpanningTree.dataStructures.implementation.support.Edge;

/**
 * Interface for a Priority Queue
 *
 * @author Shaki
 * @since May 2009
 *
 */
public interface PriorityQueue {

    /**
     * Indicates if the priority queue is empty or not
     * @return true if the queue is empty
     */
    public boolean isEmpty();

    /**
     * Returns the size of the priority queue
     * @return current size of the queue
     */
    public int getSize();

    /**
     * Adds edge to the priority queue
     * @param e edge to be added
     */
    public void insert(Edge e);

    /**
     * Removes an item of highest priority from the queue
     * (in my case it's the edge with lowest weight)
     * @return edge with the lowest weight
     */
    public Edge extractMin();

}
