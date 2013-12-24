/**
 *
 */
package cz.cvut.fel.minimalSpanningTree.algorithm.implementation;

import java.util.Vector;

import cz.cvut.fel.minimalSpanningTree.core.Constants;
import cz.cvut.fel.minimalSpanningTree.core.UnionFind;
import cz.cvut.fel.minimalSpanningTree.dataStructures.DataStructures;
import cz.cvut.fel.minimalSpanningTree.dataStructures.implementation.support.Edge;

/**
 * Boruvka's algorithm
 * @author Shaki
 * @since November 2008
 */
public class BoruvkaMST {

    // private representations
    /**
     * Array of edges, which form the MST of the graph
     */
    private Edge[] mst;

    /**
     * Edges not yet discarded and not yet in the MST
     */
    private Edge[] wannabes;

    /**
     * Each component's nearest neighbor with find component numbers as indices
     */
    private Edge[] neighbors;

    /**
     * Graph representation on which we are searching for MST
     */
    private DataStructures g;


    /**
     *
     */
    private UnionFind uf;


    // constructors and methods

    /**
     * constructor
     * @param G Graph
     */
    public BoruvkaMST(DataStructures G) {
        this.g = G;
    }

    /**
     * Boruvka's algorithm
     *
     *
     * @return minimal spanning tree - edges that form it
     */
    public Edge[] BoruvkaMSTalg() {
        Edge hlpEdge = new Edge(0,0,Constants.MAX_EDGE_LENGTH);
        this.uf = new UnionFind(this.g.getNumVertices());
        this.wannabes = new Edge[this.g.getNumEdges()];

        Vector<Edge> hlpEdg = this.g.getAllEdges();

        /**
         * Get all edges from the graph G to the array edges
         */
        for (int i=0; i<hlpEdg.size(); i++)
            this.wannabes[i] = hlpEdg.elementAt(i);

        this.neighbors = new Edge[this.g.getNumVertices()];
        this.mst = new Edge[this.g.getNumVertices()+1];

        /**
         * index, used to store those edges being saved for the next phase
         */
        int nxtPhase;
        int k=1;

        for (int i=this.g.getNumEdges(); i!=0; i=nxtPhase) {
            int l, m, n;

            for (int o=0; o<this.g.getNumVertices(); o++)
                this.neighbors[o] = hlpEdge;

            for (n=0, nxtPhase=0; n<i; n++) {
                Edge e = this.wannabes[n];
                l = this.uf.find(e.getSource());
                m = this.uf.find(e.getDestination());

                if ( l==m )
                    continue;
                if ( e.getLength() < this.neighbors[l].getLength() )
                    this.neighbors[l] = e;
                if ( e.getLength() < this.neighbors[m].getLength() )
                    this.neighbors[m] = e;

                this.wannabes[nxtPhase++] = e;
            }

            for (n=0; n<this.g.getNumVertices(); n++)
                if ( this.neighbors[n] != hlpEdge ) {
                    l = this.neighbors[n].getSource();
                    m = this.neighbors[n].getDestination();

                    if ( !this.uf.find(l,m) ) {
                        this.uf.unite(l,m);
                        this.mst[k++] = this.neighbors[n];
                    }
                }
        }

        return this.mst;
    }

}

