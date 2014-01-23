/**
 *
 */
package main.java.cz.cvut.fel.minimalSpanningTree.dataStructures.implementation;

import java.util.Iterator;
import java.util.Vector;

import main.java.cz.cvut.fel.minimalSpanningTree.dataStructures.DataStructures;
import main.java.cz.cvut.fel.minimalSpanningTree.dataStructures.implementation.support.Edge;
import main.java.cz.cvut.fel.minimalSpanningTree.dataStructures.implementation.support.PointDT;

import org.apache.log4j.Logger;

/**
 * Adjacency list representation of a graph
 * advanced version (quicker)
 *
 * @extends GraphADT
 * @author Shaki
 * @since October 2008
 *
 */
public class GraphAdjListAdv implements DataStructures {
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
    public GraphAdjListAdv() {
        setNumVertices(0);
        setNumEdges(0);
    }

    /**
     * copy constructor - copies a graph to a new list
     * @param G Graph to be copied
     */
    public GraphAdjListAdv(GraphAdjListAdv G) {
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
     * adds an edge 2-3 (3.0) to matrix[2][3] and matrix[3][2]
     *
     * @param sv source vertex
     * @param dv destination vertex
     * @param el edge length
     */
    @Override
    public void addEdge(int sv, int dv, double el) {

        /**
         * prevents the addition of the same edge (duplication)
         */
        Edge e = new Edge(sv,dv,el);

        if ( this.list.elementAt(sv).contains(e) ) {
            logger.debug("Duplicated edged not added");
        }
        else
            this.list.elementAt(sv).addElement(e);

        if ( this.list.elementAt(dv).contains(e) ) {
            logger.debug("Duplicated edged not added");
        }
        else
            this.list.elementAt(dv).addElement(e);
    }

    /**
     * remove edge (undirected)
     *
     * removes an edge 2-3 (3.0) from matrix[2][3] and matrix[3][2]
     *
     * Source vertex is always vertex with the lower index.
     *
     * @param sv source vertex
     * @param dv destination vertex
     */
    @Override
    public void removeEdge(int sv, int dv) {

        Edge hlpEdge = new Edge(sv,dv,0);

        /**
         * searches for the edge and removes it from adjacency list
         */
        Iterator<Edge> i = this.list.elementAt(sv).listIterator();
        while (i.hasNext()) {
            Edge tmp = i.next();
            if ( hlpEdge.getDestination() == tmp.getDestination() )
                this.list.elementAt(sv).remove(i);
        }

        Iterator<Edge> j = this.list.elementAt(dv).listIterator();
        while (j.hasNext()) {
            Edge tmp = j.next();
            if ( hlpEdge.getSource() == tmp.getSource() )
                this.list.elementAt(dv).remove(i);
        }

/*
        for ( int i=0; i<this.list.elementAt(sv).size(); i++) {
            if ( hlpEdge.getDestination() == this.list.elementAt(sv).elementAt(i).getDestination() )
                this.list.elementAt(sv).remove(i);
        }
        for ( int i=0; i<this.list.elementAt(dv).size(); i++) {
            if ( hlpEdge.getSource() == this.list.elementAt(dv).elementAt(i).getSource() )
                this.list.elementAt(dv).remove(i);
        }
*/
    }

    // methods to access graph properties

    /**
     * number of edges
     *
     * counts the number of edges in the graph:
     * we count all entries in array Vector<Edge> for each item in
     * Vector<Vector<Edge>>
     *
     * final result is divided by 2, because each edge is in
     * matrix two times
     *      *
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

        return ne/2;
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

        int index = this.list.elementAt(sv).indexOf(hlpEdge);

        if (index == -1)
            return null;

        return this.list.elementAt(sv).elementAt(index);
    }

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

        for (int i=0; i<this.list.size(); i++) {
            Iterator<Edge> j = this.list.elementAt(i).listIterator();
            while (j.hasNext()) {
                Edge tmp = j.next();
                if (tmp.getDestination() > i)
                    edges.add(tmp);
            }
        }
        /*
            for (int j=0; j<this.list.elementAt(i).size(); j++)
                if (this.list.elementAt(i).elementAt(j).getDestination() > i)
                    edges.add(this.list.elementAt(i).elementAt(j));
*/
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
         * add all edges adjacent to vertex v
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
         * compute all edges adjacent to vertex v
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
                //if (temp.elementAt(j).getDestination() > i)
                    out.append(temp.elementAt(j).getSource() + "-"+temp.elementAt(j).getDestination() + "(" +
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

        Edge hlpEdge = new Edge(sv,dv,el);

        int index = this.list.elementAt(sv).indexOf(hlpEdge);
        this.list.elementAt(sv).elementAt(index).setUsed(true);

        index = this.list.elementAt(dv).indexOf(hlpEdge);
        this.list.elementAt(dv).elementAt(index).setUsed(true);

    }

} // end class GraphAdjList
