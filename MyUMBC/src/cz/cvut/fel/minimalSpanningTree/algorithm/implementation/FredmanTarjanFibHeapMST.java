/**
 *
 */
package cz.cvut.fel.minimalSpanningTree.algorithm.implementation;

import java.util.Vector;

import org.apache.log4j.Logger;

import cz.cvut.fel.minimalSpanningTree.dataStructures.DataStructures;
import cz.cvut.fel.minimalSpanningTree.dataStructures.implementation.FibonacciHeap;
import cz.cvut.fel.minimalSpanningTree.dataStructures.implementation.support.Edge;
import cz.cvut.fel.minimalSpanningTree.dataStructures.implementation.support.FibHeapNode;

/**
 * Fredman and Tarjan algorithm for computing minimal spanning tree.
 * Using Fibonacci heap
 *
 * @author Shaki
 * @since May 2009
 *
 */
public class FredmanTarjanFibHeapMST {
    /**
     * log4j logger variable
     */
    static Logger                logger;

    /**
     * Array of edges, which form the MST of the graph
     */
    private Edge[] mst;
    /**
     * Index for the array of mst edges
     */
    private int mstIndex = 0;

    /**
     * Graph representation on which we are searching for MST
     */
    private DataStructures       g;

    /**
     * Initial vertex (arbitrary)
     */
    private int                 v0;

    /**
     * Vertices of the graph
     */
    //private Vector<PointDT>      vertices;

    /**
     * Fibonacci heap
     */
    private FibonacciHeap<Edge>  FB = new FibonacciHeap<Edge>();

    /**
     * Used for mapping the vertices outside T to their active edges in the
     * fibonacci heap, initally all elements are undefined
     */
    //private Vector<Vector<Edge>> a = new Vector<Vector<Edge>>();

    //malo by mi stacit jednoduche pole, kde si budem pamatat predchodcu
    private FibHeapNode[] a;
    //private Edge[] a;

    // pamatam si posledny uzol pridany do MST a uzly, ktore uz maju MST
    /**
     * vertex is already in MST or isn't
     */
    private boolean[] vInMST;
    /**
     * last added vertex to MST
     */
    private int lastVertex;

    // constructors and methods

    /**
     * constructor
     *
     * @param G Graph
     */
    public FredmanTarjanFibHeapMST(DataStructures G) {
        this.g = G;
        this.a = new FibHeapNode[this.g.getNumVertices()];
        this.mst = new Edge[this.g.getNumVertices()-1];
        this.vInMST = new boolean[this.g.getNumVertices()];

    }

    /**
     * Computes minimal spanning tree
     *
     * @return minimal spanning tree - edges that form it
     */
    public Edge[] FredmanTarjanFibHeapMSTalg() {
        logger = Logger.getLogger(this.getClass());

        /**
         * Choose arbitrary vertex
         */
        //this.vertices = this.g.getAllVertices();
        //this.v0 = this.vertices.firstElement();
        this.v0 = 0;

        //prvy pridany vrchol do MST bude v0;
        //this.last = this.v0.getIndex();
        setLast(this.v0);
        this.vInMST[this.getLast()] = true;

        /**
         * 5. Insert all edges incident with v0 to FH and update A accordingly
         */
        Vector<Edge> adjEdges = this.g.getAllEdgesAdjacentToVertex(this.v0);

        for (Edge e : adjEdges) {
            this.FB.insert(e);
            //int v = (e.getSource() == this.v0)?e.getDestination():e.getSource();
            this.a[e.getDestination()] = e;
            // aby som vedel, ktora hrana je uz v FH
            e.setUsed(true);
        }

        /**
         * 6. While H is not empty:
         */
        while (!this.FB.isEmpty()) {

            /**
             * 7. (u,v)<-DeleteMin(H)
             */
            // do MST pridavam hranu uv

            Edge uv = (Edge) this.FB.extractMin();

            //logger.debug(this.FB.toString());

            // uzly u a v pridam do vInMST, ze uz maju MST a zapamatam si index posledne pridaneho
            if (this.vInMST[uv.getSource()])
                setLast(uv.getDestination());
            else
                setLast(uv.getSource());

            // update vInMST
            this.vInMST[getLast()] = true;

            /**
             * 8. T<-T+(u,v)
             */
            this.mst[getMstIndex()] = uv;
            setMstIndex(getMstIndex() + 1);

            // get all edges adjacent to the last vertex
            Vector<Edge> notMST = this.g.getAllEdgesAdjacentToVertex(getLast());

            /**
             * 9. For all edges vw such that w not in T
             */
            for (Edge e : notMST) {
                /**
                 * if is used => w is in T, skip
                 */
                if (!e.isUsed()) {
                    // destination of the working edge (the other vertex than lastVertex)
                    int target;
                    if (e.getSource() == getLast())
                        target = e.getDestination();
                    else
                        target = e.getSource();

                    /**
                     * 10. If there exists an active edge A(w)
                     */
                    if (this.a[target] != null) {

                        /**
                         * 11. If vw is lighter than A(w), Decrease A(w) to (v,w)
                         */
                        if (e.getLength() < this.a[target].getKey()) {
                            this.FB.decreaseKey(this.a[target], e);
                            e.setUsed(true);
                        }
                    }
                    /**
                     * 12. If there is no such edge
                     */
                    else {
                        /**
                         * 12. Insert(v,w) to H and set A(w)
                         */
                        this.FB.insert(e);
                        this.a[target] = e;
                        e.setUsed(true);
                    }
                } // end if (!e.isUsed())



            } // for (Edge e: notMST)
            //logger.info("Koniec notInMST for cyclu");

        }

        return this.mst;
    }

    /**
     * @param last the last to set
     */
    public void setLast(int last) {
        this.lastVertex = last;
    }

    /**
     * @return the last
     */
    public int getLast() {
        return lastVertex;
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
        return mstIndex;
    }
}
