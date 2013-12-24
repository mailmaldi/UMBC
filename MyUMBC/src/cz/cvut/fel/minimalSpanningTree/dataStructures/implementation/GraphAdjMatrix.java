/**
 *
 */
package cz.cvut.fel.minimalSpanningTree.dataStructures.implementation;

import java.util.Vector;

import cz.cvut.fel.minimalSpanningTree.dataStructures.DataStructures;
import cz.cvut.fel.minimalSpanningTree.dataStructures.implementation.support.Edge;
import cz.cvut.fel.minimalSpanningTree.dataStructures.implementation.support.PointDT;

/**
 * Adjacency matrix representation of a graph
 *
 * @extends DataStructures
 * @author Shaki
 * @since October 2008
 *
 */
public class GraphAdjMatrix implements DataStructures {

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
     * adjacency matrix of a graph
     */
    private Edge[][] matrix;

    // constructors

    /**
     * default constructor
     * creates an empty graph, no vertices and edges
     */
    public GraphAdjMatrix() {
        setNumVertices(0);
        setNumEdges(0);
    }

    /**
     * copy constructor - copies a graph to a new matrix
     * @param G Graph to be copied
     */
    public GraphAdjMatrix(GraphAdjMatrix G) {
        setNumVertices(G.getNumVertices());
        setNumEdges(G.getNumEdges());

        for (int i=0; i<this.numVertices; i++)
            for (int j=0; j<this.numVertices;j++)
                this.matrix[i][j] = G.matrix[i][j];

        for ( int i=0; i<this.numVertices; i++)
            this.vertices.add(G.vertices.elementAt(i));
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
     */
    public void addEdge(int sv, int dv, double el) {

        /**
         * checks if sv < dv, otherwise exchanges
         */
        if (sv > dv) {
            int tmp = sv;
            sv = dv;
            dv = tmp;
        }

        /**
         * prevents the addition of multiedge
         */
        Edge e = new Edge(sv,dv,el);
        //if ( this.matrix.elementAt(index).contains(e) )
        if ( this.matrix[sv][dv] != null )
            return;

        this.matrix[sv][dv] = e;
    }

    /**
     * remove edge (undirected)
     *
     * Source vertex is always vertex with the lower index.
     *
     * @param sv source vertex
     * @param dv destination vertex
     */
    public void removeEdge(int sv, int dv) {
        /**
         * checks if sv < dv, otherwise exchanges
         */
        if (sv > dv) {
            int tmp = sv;
            sv = dv;
            dv = tmp;
        }

        //only one edge from sv to dv exists
        this.matrix[sv][dv] = null;
    }

    /**
     * number of edges
     *
     * counts the number of edges in the graph:
     * we count all entries for each pairs of vertices in the
     * adjacency matrix
     *
     * @return ne number of edges
     */
    @Override
    public int countNumEdges() {
        int ne = 0;

        for (int i=0; i<this.numVertices; i++)
            for (int j=0; j<this.numVertices; j++)
                if (this.matrix[i][j] != null)
                    ne++;

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

        return this.matrix[sv][dv];
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

        /**
         * checks if sv < dv, otherwise exchanges
         */
        /*if (sv > dv) {
            int tmp = sv;
            sv = dv;
            dv = tmp;
        }

        return this.matrix[sv][dv];
    }
*/

    /**
     * get all vertices
     * (index, x-coordinate, y-coordinate)
     *
     * @return Vector<PointDT> all existing vertices in one vector
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
     * Source vertex is always vertex with the lower index
     *
     * @return Vector<Edge> all existing edges in one vector
     */
    public Vector<Edge> getAllEdges() {
        Vector<Edge> edges = new Vector<Edge>();

        for (int i=0; i<this.numVertices; i++)
            for (int j=0; j<this.numVertices; j++)
                if (this.matrix[i][j] != null)
                    edges.add(this.matrix[i][j]);

        return edges;
    }

    /**
     * get all edges (source vertex, destination vertex, length) adjacent
     * to specified vertex
     *
     * How to get all adjacent edges from the matrix:
     * - 1. in rows lower than the index of the vertex look in the column with
     * the index of the vertex
     * - 2. get all edges from the row with the index of the vertex
     *
     * @param v vertex, which edges we want to get
     * @return all edges adjacent to the vertex v
     */
    public Vector<Edge> getAllEdgesAdjacentToVertex(int v) {
        Vector<Edge> adjEdges = new Vector<Edge>();

        /**
         * add all edges ending in vertex v (we must check all the edges with
         * starting vertex name lower than our vertex)
         */
        for (int i=0; i<v; i++)
            if (this.matrix[i][v] != null)
                adjEdges.add(this.matrix[i][v]);

        /**
         * add all edges starting at vertex v
         */
        for (int i=0; i<this.numVertices; i++)
            if (this.matrix[v][i] != null)
                adjEdges.add(this.matrix[v][i]);

        return adjEdges;
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
            if (this.matrix[i][v] != null)
                degree++;

        /**
         * compute all edges starting at vertex v
         */
        for (int i=0; i<this.numVertices; i++)
            if (this.matrix[v][i] != null)
                degree++;

        return degree;
    }

    // default output readable by constructor

    /**
    * Prints adjacency matrix representation of a graph
    *
    * @return String graph as an adjacency matrix
    */
    @Override
    public String toString() {
        StringBuffer out = new StringBuffer();

        out.append(this.numVertices + "\n");

        for ( int i=0; i<this.numVertices; i++ )
            for ( int j=0; j<this.numVertices; j++ ) {
                out.append("| ");
                if ( this.matrix[i][j] != null ) {
                    out.append(i + "-" + j);
                    out.append("(" + this.matrix[i][j].getLength() + ")");
                    out.append(" | ");
                }
                else out.append(" No Edge | ");

                if (j ==  (this.numVertices-1) )
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

        this.matrix = new Edge[this.numVertices][this.numVertices];
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

    /* (non-Javadoc)
     * @see cz.cvut.fel.minimalSpanningTree.dataStructures.DataStructures#getNotUsedEdges()
     */
    @Override
    public Vector<Edge> getNotUsedEdges() {
        Vector<Edge> result = new Vector<Edge>();

        for (Edge edge : getAllEdges()) {
            if(!edge.isUsed()) {
                result.add(edge);
            }
        }

        return result;    }

    public void setUsedEdge(int sv, int dv, double el) {

        /**
         * checks if sv < dv, otherwise exchanges
         */
        if (sv > dv) {
            int tmp = sv;
            sv = dv;
            dv = tmp;
        }

        this.matrix[sv][dv].setUsed(true);
    }

}   // end of class GraphAdjMatrix
