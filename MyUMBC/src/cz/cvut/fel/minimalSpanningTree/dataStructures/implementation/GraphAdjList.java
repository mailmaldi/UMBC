/**
 *
 */
package cz.cvut.fel.minimalSpanningTree.dataStructures.implementation;

import java.util.Vector;

import org.apache.log4j.Logger;

import cz.cvut.fel.minimalSpanningTree.dataStructures.DataStructures;
import cz.cvut.fel.minimalSpanningTree.dataStructures.implementation.support.Edge;
import cz.cvut.fel.minimalSpanningTree.dataStructures.implementation.support.PointDT;

/**
 * Adjacency list representation of a graph
 * @extends GraphADT
 * @author Shaki
 * @since October 2008
 *
 */
public class GraphAdjList implements DataStructures {
    /**
     * log4j logging variable
     */
    static Logger logger = Logger.getLogger(GraphAdjList.class.getClass());
    // private representations

    /**
     * number of vertices
     */
    private int numVertices;

    /**
     * number of edges
     */
    private int numEdges;

    /**
     * Vector of vertices
     */
    private Vector<PointDT> vertices = new Vector<PointDT>();

    /**
     * Vector of vector of Edges
     */
    private Vector<Vector<Edge>> list = new Vector<Vector<Edge>>();

    // constructors

    /**
     * default constructor
     * creates an empty graph, no vertices and edges)
     */
    public GraphAdjList() {
        setNumVertices(0);
        setNumEdges(0);
    }

    /**
     * copy constructor - copies a graph to a new list
     * @param G Graph to be copied
     */
    public GraphAdjList(GraphAdjList G) {
        setNumVertices(G.getNumVertices());
        setNumEdges(G.getNumEdges());

        for ( int i=0; i<this.numVertices; i++)
            this.vertices.add(G.vertices.elementAt(i));

        for ( int i=0; i<this.numVertices; i++)
            this.list.add(G.list.elementAt(i));
    }

    // methods - for altering data graph structure

    /**
     * add vertex
     *
     * @param i index/label of vertex
     * @param x x-coordinate of vertex
     * @param y y-coordinate of vertex
     */
    @Override
    public void addVertex(int i, int x, int y) {

        /**
         * generator creates unique vertices, no need to check for
         * duplicate addition of vertex
         */
        PointDT p = new PointDT(i,x,y);
        this.vertices.add(p);
    }

    /**
     * removes all vertices to save heap space
     */
    public void removeAllVertices() {
        this.vertices.clear();
    }

    /**
     * add edge (undirected)
     *
     * Source vertex is always vertex with the lower index.
     *
     * @param sv source vertex
     * @param dv destination vertex
     * @param el edge length
     * @throws Exception 
     */
    @Override
    public void addEdge(int sv, int dv, double el) throws Exception {

        /**
         * checks if sv < dv, otherwise exchanges
         */
        if (sv > dv) {
            int tmp = sv;
            sv = dv;
            dv = tmp;
        }

        /**
         * prevents the addition of the same edge (duplication)
         */
        Edge e = new Edge(sv,dv,el);
        if ( this.list.elementAt(sv).contains(e) ) {
        	logger.debug("Duplicated edged not added");
        	throw new Exception();
        }
        else
            this.list.elementAt(sv).addElement(e);
    }

    /**
     * remove edge (undirected)
     *
     * Source vertex is always vertex with the lower index.
     *
     * @param sv source vertex
     * @param dv destination vertex
     */
    @Override
    public void removeEdge(int sv, int dv) {
        /**
         * checks if sv < dv, otherwise exchanges
         */
        if (sv > dv) {
            int tmp = sv;
            sv = dv;
            dv = tmp;
        }

        Edge hlpEdge = new Edge(sv,dv,0);

        /**
         * searches for the edge and removes it from adjacency list
         */
        for ( int i=0; i<this.list.elementAt(sv).size(); i++) {

            if ( hlpEdge.getDestination() == this.list.elementAt(sv).elementAt(i).getDestination() )
                this.list.elementAt(sv).remove(i);
        }
    }

    // methods to access graph properties

    /**
     * number of edges
     *
     * counts the number of edges in the graph:
     * we count all entries in array Vector<Edge> for each item in
     * Vector<Vector<Edge>>
     *
     * @return ne number of edges
     */
    @Override
    public int countNumEdges() {
        /**
         * number of edges
         */
        int ne = 0;

        for ( int i=0; i<getNumVertices(); i++ )
            ne += this.list.elementAt(i).size();

        return ne;
    }

    /**
     * get edge
     *
     * Source vertex is always vertex with the lower index.
     *
     * @param sv source vertex
     * @param dv destination vertex
     * @return Edge object that contains edge source, destination and length
     */
    public Edge getEdge(int sv, int dv) {

        /**
         * checks if sv < dv, otherwise exchanges
         */
        if (sv > dv) {
            int tmp = sv;
            sv = dv;
            dv = tmp;
        }

        Edge hlpEdge = new Edge(sv,dv,0);

        //if ( this.list.elementAt(sv).contains(hlpEdge) )
        //            return hlpEdge;

        int index = this.list.elementAt(sv).indexOf(hlpEdge);

        if (index == -1)
            return null;
        else
            return this.list.elementAt(sv).elementAt(index);
    }

    /**
     * get minimal edge - not needed anymore, since there is only one edge
     * in graph going from sv to dv
     *
     * Source vertex is always vertex with the lower index.
     *
     * @param sv source vertex
     * @param dv destination vertex
     * @return Edge minimal edge from all edges connecting source vertex with
     * destination
     */
/*
    public Edge getMinEdge(int sv, int dv) {

        Edge hlp = new Edge(sv,dv,Constants.MAX_EDGE_LENGTH);

        /**
         * checks if sv < dv, otherwise exchanges
         *//*
        if (sv > dv) {
            int tmp = sv;
            sv = dv;
            dv = tmp;
        }

        for (int i=0; i<this.list.elementAt(sv).size(); i++)
            if ( (this.list.elementAt(sv).elementAt(i).getDestination() == dv)) {
                hlp = this.list.elementAt(sv).elementAt(i);
                return hlp;
            }

        // edge not found
        return null;
    }
*/
    /**
     * get all vertices
     * (index, x-coordinate, y-coordinate)
     *
     * @return all existing vertices in one vector
     */
    public Vector<PointDT> getAllVertices() {
        Vector<PointDT> vertices = new Vector<PointDT>();

        for (int i=0; i<this.vertices.size(); i++)
            vertices.add(this.vertices.elementAt(i));

        return vertices;
    }

    /**
     * get all edges (source vertex, destination vertex, length)
     *
     * @return all existing edges in one vector
     */
    public Vector<Edge> getAllEdges() {
        Vector<Edge> edges = new Vector<Edge>();

        for (int i=0; i<this.list.size(); i++)
            edges.addAll(this.list.elementAt(i));

        return edges;
    }

    /**
     * get all edges (source vertex, destination vertex, length) adjacent
     * to specified vertex
     *
     * @param v vertex, which edges we want to get
     * @return all edges adjacent to the vertex v
     */
    public Vector<Edge> getAllEdgesAdjacentToVertex(int v) {
        Vector<Edge> edges = new Vector<Edge>();

        /**
         * add all edges ending in vertex v (we must check all the edges with
         * starting vertex name lower than our vertex)
         */
        for (int i=0; i<v; i++)
            for (int j=0; j<this.list.elementAt(i).size(); j++)
                if (this.list.elementAt(i).elementAt(j).getDestination() == v)
                    edges.add(this.list.elementAt(i).elementAt(j));

        /**
         * add all edges starting at vertex v
         */
        edges.addAll(this.list.elementAt(v));

        return edges;
    }

    /**
     * degree of the vertex (number of edges incidental with vertex)
     *
     * @param v index of the vertex
     * @return degree of the vertex (number of edges incidental with vertex)
     */
    public int vertexDegree(int v) {
        int degree = 0;

        /**
         * compute all edges ending in vertex v
         */
        for (int i=0; i<v; i++)
            for (int j=0; j<this.list.elementAt(i).size(); j++)
                if (this.list.elementAt(i).elementAt(j).getDestination() == v)
                    degree++;

        /**
         * compute all edges starting at vertex v
         */
        degree = degree + this.list.elementAt(v).size();

        return degree;
    }

    // default output readable by constructor

    /**
    * Prints adjacency list representation of a graph
    *
    * @return String graph as an adjacency list
     */
    @Override
    public String toString() {
        StringBuffer out = new StringBuffer();

        out.append(getNumVertices() + "\n");

        for ( int i=0; i<getNumVertices(); i++ ) {
            Vector<Edge> temp = this.list.elementAt(i);

            out.append("| ");
            for ( int j=0; j<temp.size(); j++ ) {
                    out.append(temp.elementAt(j).getDestination() + "(" +
                               temp.elementAt(j).getLength() + ") | ");
            }
            out.append("\n");
        }

        return out.toString();
    }

    // getters and setter for numVertices and numEdges

    /**
     * sets the number of vertices
     *
     * @param numVertices the number of vertices to set (from input file)
     */
    public void setNumVertices(int numVertices) {
        this.numVertices = numVertices;

        // inicializujem linked list
        for(int i=0; i<numVertices; i++)
            this.list.add(new Vector<Edge>());
    }

    /**
     * returns the number of vertices set at the beginning with the input file
     * parameter.
     *
     * @return the numVertices
     */
    public int getNumVertices() {
        return this.numVertices;
    }

    /**
     * sets the number of edges
     *
     * @param numEdges the number of edges to set (from input file)
     */
    public void setNumEdges(int numEdges) {
        this.numEdges = numEdges;
    }

    /**
     * returns the number of edges set at the beginning with the input file
     * parameter. If you need actual number of edges (computed from real edges
     * in graph), use method numEdges()
     *
     * @return the numEdges
     */
    public int getNumEdges() {
        return this.numEdges;
    }

    public Vector<Edge> getNotUsedEdges() {
        Vector<Edge> result = new Vector<Edge>();

        for (Edge edge : getAllEdges()) {
            if(!edge.isUsed()) {
                result.add(edge);
            }
        }

        return result;
    }

    public void setUsedEdge(int sv, int dv, double el) {

        /**
         * checks if sv < dv, otherwise exchanges
         */
        if (sv > dv) {
            int tmp = sv;
            sv = dv;
            dv = tmp;
        }

        Edge hlpEdge = new Edge(sv,dv,el);

        int index = this.list.elementAt(sv).indexOf(hlpEdge);

        this.list.elementAt(sv).elementAt(index).setUsed(true);
    }

}   // end class GraphAdjList
