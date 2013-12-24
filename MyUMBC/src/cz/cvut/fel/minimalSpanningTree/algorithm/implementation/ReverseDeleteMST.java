/**
 *
 */
package cz.cvut.fel.minimalSpanningTree.algorithm.implementation;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Vector;

import cz.cvut.fel.minimalSpanningTree.core.UnionFind;
import cz.cvut.fel.minimalSpanningTree.dataStructures.DataStructures;
import cz.cvut.fel.minimalSpanningTree.dataStructures.implementation.support.Edge;

/**
 * Reverse-Delete algorithm
 * @author Shaki
 * @since November 2008
 */
public class ReverseDeleteMST {

    // private representations

    /**
     * Array of edges, which form the MST of the graph
     */
    private Edge[] mst;

    /**
     * Graph representation on which we are searching for MST
     */
    private DataStructures g;

    /**
     * number of vertices
     */
    private int numVer;

    /**
     * number of edges
     */
    private int numEdges;

    /**
     * array of edges, every item in this array is edge object and has source,
     * destination vertex and length of the edge
     */
    private Edge[] edges;

    // constructors and methods

    /**
     * constructor
     * @param G Graph
     */
    public ReverseDeleteMST(DataStructures G) {
        this.g = G;
    }

    /**
     * Reverse-delete algorithm
     *
     * used to obtain a minimum spanning tree from a given connected,
     * edge-weighted graph. If the graph is disconnected, this algorithm will
     * find a minimum spanning tree for each disconnected part of the graph.
     *
     * It is the reserve of Kruskal's algorithm. It starts with graph g, which
     * contains list of edges edges. It goes through edges in sorted from
     * maximal to minimal, checks if deleting current edge will further
     * disconnect the graph, if not, edge can be deleted, otherwise it is part
     * of the MST.
     *
     *D.  Reverse-delete algorithm:
    Idea:  Repeatedly delete largest-cost edge remaining, as long as it
        does not disconnect the graph.
    Runtime?  For each edge, run BFS/DFS starting from one endpoint to
        figure out if other endpoint can still be reached.  Requires
        \Omega(m * (m+n)) = \Omega(m^2).

     *
     * @return minimal spanning tree - edges that form it
     */
    public Edge[] ReverseDeleteMSTalg() {

        this.numVer = this.g.getNumVertices();
        this.numEdges = this.g.getNumEdges();
        this.mst = new Edge[this.numVer];
//        this.edges = new Edge[this.g.numEdges()];

        Vector<Edge> hlpEdg = this.g.getAllEdges();

        /**
         * Get all edges from the graph G to the array edges
         */
/*        for (int i=0; i<hlpEdg.size(); i++) {
            this.edges[i] = hlpEdg.elementAt(i);
        }
*/
        /**
         * sort all edges, first in array will be the edge with the minimum
         * length
         */
        //System.out.println("Reverse Delete before sort: " + this.g.numEdges());
        //for (int i=0; i<this.g.numEdges(); i++)
        //    System.out.print(hlpEdg.elementAt(i).getLength() + " ; ");

        java.util.Collections.sort(hlpEdg);

        //System.out.println("\nReverse Delete after sort: " + this.g.numEdges());
        //for (int i=0; i<this.g.numEdges(); i++)
        //    System.out.print(hlpEdg.elementAt(i).getLength() + " ; ");
        //System.out.println();

        // pomocna edge, budem si v nej pamatat najvacsiu hranu,
        // ktoru chcem mazat
        Edge e;
        int i = hlpEdg.size()-1;

        while ( i>=0 ) {

            // ulozim si hranu na zmazanie do pomocnej premennej
            e = hlpEdg.elementAt(i);

            /**
             * delete edge (in case we will need it, it's saved temporarily
             * in edge e
             */
            hlpEdg.removeElementAt(i);

            /**
             * check if deletion of edge e further disconnects the graph
             *
             */

            //BFS - zacnem zo startovacieho uzlu, frontu si spravim ako vector
            //integerov - cislo = index uzlu; ak je pociatocny uzol vacsi ako
            // cielovy, tak ich vymenim
            int startVertex = e.getSource();
            int endVertex = e.getDestination();
            boolean deleteOK = false;

            // vertexQueue, fronta uzlov na preskumanie, inicializujem startovacim
            Queue<Integer> vQ = new LinkedList<Integer>();
            // pole uz preskumanych uzlov
            Vector<Integer> examined = new Vector<Integer>();

            vQ.offer(startVertex);

            //pokial je vo fronte stale nejaky uzol na preskumanie
            //ak prejdem vsetky tak deleteOK ostane false, co je ok, lebo
            //som nenasiel cestu
            while (!vQ.isEmpty()) {

                // vyberiem (a zmazem) prvy uzol z fronty
                int tmpVertex = vQ.remove();

                // ak je to hladany uzol, skoncim uspesne - cesta existuje
                if (tmpVertex == endVertex) {
                    deleteOK = true;    // cesta existuje, mozem deletnut
                    break;
                }

                // skontrolujem, ci uz nebol preskumany, ak hej, idem na dalsi
                if (examined.contains(tmpVertex))
                    continue;

                // este nebol skumany, takze analyzujem
                // neni hladany => pridam do fronty vsetky uzly do ktorych sa
                // viem dostat z neho
                Vector<Edge> adjEdges = this.g.getAllEdgesAdjacentToVertex(tmpVertex);

                for (int j=0; j<adjEdges.size();j++)
                    // musim vynechat tu jednu hranu ktoru chcem zmazat, aby
                    // mi netvorila cestu ;-)
                    if (adjEdges.elementAt(j) != e)
                        // kludne mozem pridat aj uzly, kde som uz bol
                        // pretoze ich kontrolujem vzdy pri vyberani z fronty
                        // musim kontrolovat, ci nahodou tmpVertex neni
                        // destination danej hrany, vtedy mysim brat source
                        if (adjEdges.elementAt(j).getDestination() == tmpVertex)
                            vQ.offer(adjEdges.elementAt(j).getSource());
                        else
                            vQ.offer(adjEdges.elementAt(j).getDestination());

                // zapisem preskumany uzol do spracovanych
                examined.add(tmpVertex);
            }

            // IF neexistuje ina cesta medzi uzlami, nemozem hranu zmazat
            if (!deleteOK) hlpEdg.add(e);
            else
                this.g.removeEdge(e.getSource(), e.getDestination());

            i--;
        }

        for (i=0; i<hlpEdg.size();i++)
            this.mst[i] = hlpEdg.elementAt(i);

        return this.mst;
    }

}

