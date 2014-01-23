/**
 *
 */
package main.java.cz.cvut.fel.minimalSpanningTree.algorithm.implementation;

import java.util.Vector;

import main.java.cz.cvut.fel.minimalSpanningTree.dataStructures.DataStructures;
import main.java.cz.cvut.fel.minimalSpanningTree.dataStructures.implementation.BinaryHeap;
import main.java.cz.cvut.fel.minimalSpanningTree.dataStructures.implementation.MultiwayHeapPQ;
import main.java.cz.cvut.fel.minimalSpanningTree.dataStructures.implementation.support.Edge;

import org.apache.log4j.Logger;

/**
 * Prim's priority-first search algorithm
 * (Johnson's version of Prim's algorithm (d-heap priority queue))
 *
 * @author Shaki
 * @since May 2009
 */
public class PrimPfsMST {
    static Logger                logger;

    // private representations

    /**
     * Array of edges, which form the MST of the graph
     */
    private Edge[] mst;

    /**
     * Index for the mst array
     */
    private int mstIndex = 0;

    /**
     * Help array
     */
    private Edge[] a;

    /**
     * vertex in MST
     */
    private boolean[] vInMST;

    /**
     * Binary heap representation of priority queue
     * - fringe is v in Q where edge is not null
     * - not seen is v in Q where edge is null
     */
    private BinaryHeap bh;

    /**
     * Graph representation on which we are searching for MST
     */
    private DataStructures g;

    /**
     * Initial vertex, from which the algorithm will start
     */
    private int initVertex;

    /**
     * Index of the last computed vertex
     * Used for choosing source or destination vertex for working
     */
    private int lastVertex;

    // constructors and methods

    /**
     * Initialization
     * @param G Graph
     */
    public PrimPfsMST(DataStructures G) {
        this.g = G;

        // starting vertex will be the one with index 0
        setInitVertex(0);

        // initialize binary heap
        setBh(new BinaryHeap(this.g.getNumVertices()));

        // intialize mst, number of mst edges = (number of vertices -1)
        this.mst = new Edge[this.g.getNumVertices()-1];

        // initialize a
        this.a = new Edge[this.g.getNumVertices()];

        // initialize vInMST
        this.vInMST = new boolean[this.g.getNumVertices()];

        // insert all edges adjacent with starting vertex to the queue
        Vector<Edge> adjE = this.g.getAllEdgesAdjacentToVertex(0);
        for (Edge e : adjE) {
            this.bh.insert(e);
            this.a[e.getDestination()] = e;
            e.setUsed(true);
        }

        // set lastVertex to 0
        setLastVertex(0);
        this.vInMST[0] = true;
    }

    /**
     * Computes minimal spanning tree with Jarnik/Prim's algorithm on
     * priority queue implemented as binary heap
     *
     * @return mst array of edges forming minimal spanning tree
     */
    public Edge[] PrimPfsalg() {
        logger = Logger.getLogger(this.getClass());

        Edge actMinEdge;

        // kym viem vybrat minimalnu hranu z heap, tak vyberam :-)
        while ( (actMinEdge=this.bh.extractMin()) != null ) {

/* only for debug
            String s="[";
            for (int i=0; i<this.g.getNumVertices(); i++)
                if (this.a[i] == null)
                    s = s + "null, ";
                else
                    s = s + this.a[i].getLength() + ", ";
            logger.info(s);
*/
            // compute last vertex
            if ( this.vInMST[actMinEdge.getSource()] )
                setLastVertex(actMinEdge.getDestination());
            else
                setLastVertex(actMinEdge.getSource());

            // update vInMST
            this.vInMST[getLastVertex()] = true;

            this.mst[getMstIndex()] = actMinEdge;
            setMstIndex(getMstIndex() + 1);

            // get all edges adjacent to the last vertex
            Vector<Edge> adjE = this.g.getAllEdgesAdjacentToVertex(getLastVertex());

            for (Edge e : adjE) {
                if (!e.isUsed()) {
                    // so far edge is not in heap

                    // destination of the working edge (the other vertex than lastVertex)
                    int target;
                    if (e.getSource() == getLastVertex())
                        target = e.getDestination();
                    else
                        target = e.getSource();

                    // so far no edge to that vertex => automatically add
                    if (this.a[target] == null) {
                        //logger.info(this.bh.printHeap());
                        this.bh.insert(e);
                        this.a[target] = e;
                        e.setUsed(true);
                        //logger.info(this.bh.printHeap());
                    }
                    else {
                        // is working edge length < actual minimum edge length so far?
                        if (e.getLength() < this.a[target].getLength()) {
                            //logger.info(this.bh.printHeap());

                            // update edge in queue with new edge e
                            this.bh.decreaseKey(this.a[target], e);
                            e.setUsed(true);

                            //logger.info(this.bh.printHeap());
                        }
                    }
                } // end if (!e.isUsed())
            } // end for (Edge e : adjE)
        } // end while

        return this.mst;
    }

    /**
     * @param initVertex the initVertex to set
     */
    public void setInitVertex(int initVertex) {
        this.initVertex = initVertex;
    }

    /**
     * @return the initVertex
     */
    public int getInitVertex() {
        return this.initVertex;
    }

    /**
     * @param bh the bh to set
     */
    public void setBh(BinaryHeap bh) {
        this.bh = bh;
    }

    /**
     * @return the bh
     */
    public BinaryHeap getBh() {
        return this.bh;
    }

    /**
     * @param lastVertex the lastVertex to set
     */
    public void setLastVertex(int lastVertex) {
        this.lastVertex = lastVertex;
    }

    /**
     * @return the lastVertex
     */
    public int getLastVertex() {
        return this.lastVertex;
    }

    /**
     * @param mstIndex the mstIndex to set
     */
    public void setMstIndex(int mstIndex) {
        this.mstIndex = mstIndex;
    }

    /**
     * @return the mstIndex
     */
    public int getMstIndex() {
        return this.mstIndex;
    }

}

