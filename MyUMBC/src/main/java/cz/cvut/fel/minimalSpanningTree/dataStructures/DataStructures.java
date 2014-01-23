/**
 *
 */
package main.java.cz.cvut.fel.minimalSpanningTree.dataStructures;

import java.util.Vector;

import main.java.cz.cvut.fel.minimalSpanningTree.dataStructures.implementation.support.Edge;
import main.java.cz.cvut.fel.minimalSpanningTree.dataStructures.implementation.support.PointDT;

/**
 * ADTI - abstract data type interface for graphs and digraphs. Graph is
 * represented as a digraph with anti-parallel arcs, if edge u->v (from vertex u
 * to vertex v) exists, then edge v->u exists too.
 *
 * @author Shaki
 * @since October 2008
 */
public interface DataStructures {

    /**
     * add vertex
     *
     * @param i index/label of vertex
     * @param x x-coordinate of vertex
     * @param y y-coordinate of vertex
     */
    public void addVertex(int i, int x, int y);

    /**
     * removes all vertices to save heap space
     */
    public void removeAllVertices();

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
    public void addEdge(int sv, int dv, double el) throws Exception;

    /**
     * remove edge (undirected)
     *
     * Source vertex is always vertex with the lower index.
     *
     * @param sv source vertex
     * @param dv destination vertex
     */
    public void removeEdge(int sv, int dv);

    /**
     * gets number of vertices
     *
     * @return number of vertices
     */
    public int getNumVertices();

    /**
     * gets number of edges
     *
     * @return number of edges
     */
    public int getNumEdges();

    /**
     * counts number of edges
     *
     * @return number of edges
     */
    public int countNumEdges();

    /**
     * sets the number of vertices
     *
     * @param numVertices the number of vertices to set (from input file)
     */
    public void setNumVertices(int numVertices);

    /**
     * sets the number of edges
     *
     * @param numEdges the number of edges to set (from input file)
     */
    public void setNumEdges(int numEdges);

    /**
     * get edge
     *
     * Source vertex is always vertex with the lower index.
     * Parameter edge length must be always specified, because there can be
     * more edges from source vertex to destination.
     *
     * @param sv source vertex
     * @param dv destination vertex
     * @param el edge length
     * @return Edge object that contains edge source, destination and length
     */
    public Edge getEdge(int sv, int dv);

    /**
     * get minimal edge
     *
     * Source vertex is always vertex with the lower index.
     *
     * @param sv source vertex
     * @param dv destination vertex
     * @return Edge minimal edge from all edges connecting source vertex with
     * destination
     */
    //public Edge getMinEdge(int sv, int dv);

    /**
     * get all vertices
     * (index, x-coordinate, y-coordinate)
     *
     * @return all existing vertices in one vector
     */
    public Vector<PointDT> getAllVertices();

    /**
     * get all edges (source vertex, destination vertex, length)
     *
     * @return all existing edges in one vector
     */
    public Vector<Edge> getAllEdges();

    /**
     * get all edges (source vertex, destination vertex, length) adjacent
     * to specified vertex
     *
     * @param v vertex, which edges we want to get
     * @return all edges adjacent to the vertex v
     */
    public Vector<Edge> getAllEdgesAdjacentToVertex(int v);

    /**
     * degree of the vertex (number of edges incidental with vertex)
     *
     * @param v index of the vertex
     * @return degree of the vertex (number of edges incidental with vertex)
     */
    public int vertexDegree(int v);

    /**
     * get all edges that has flag used set to false
     *
     * @return edges with flag used false
     */
    public Vector<Edge> getNotUsedEdges();

    /**
     * set edges as used
     *
     * @param sv source vertex
     * @param dv destination vertex
     * @param el length
     */
    public void setUsedEdge(int sv, int dv, double el);

    // output methods for printing/viewing the graph

    /**
     * Prints graph representation. Is used by 2 representations of graph.
     * GraphAdjMatrix uses it for printing graph represented by adjacency
     * matrix, GraphAdjList prints graph represented by adjacency list.
     */
    @Override
    public String toString();

}
