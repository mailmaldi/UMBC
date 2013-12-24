/**
 *
 */
package cz.cvut.fel.minimalSpanningTree.algorithm.implementation;

import java.util.Arrays;
import java.util.Vector;

import org.apache.log4j.Logger;

import cz.cvut.fel.minimalSpanningTree.core.CMSC641HW8;
import cz.cvut.fel.minimalSpanningTree.core.UnionFind;
import cz.cvut.fel.minimalSpanningTree.dataStructures.DataStructures;
import cz.cvut.fel.minimalSpanningTree.dataStructures.implementation.support.Edge;

/**
 * Kruskal's algorithm
 * 
 * @author Shaki
 * @since November 2008
 */
public class KruskalMST
{
	static Logger logger;

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
	private int numEdg;

	/**
     *
     */
	private UnionFind uf;

	/**
	 * array of edges, every item in this array is edge object and has source, destination vertex and length of the edge
	 */
	private Edge[] edges;

	// constructors and methods

	/**
	 * constructor
	 * 
	 * @param G
	 *            Graph
	 */
	public KruskalMST(DataStructures G)
	{
		this.g = G;
		this.numVer = G.getNumVertices();
		this.numEdg = G.getNumEdges();
		this.mst = new Edge[this.numVer];
		this.uf = new UnionFind(this.numVer);
		this.edges = null;// new Edge[G.getNumEdges()];
	}

	/**
	 * Kruskal's algorithm
	 * 
	 * Builds MST one edge at a time. It finds an edge that connects two trees in a spreading forest of growing MST subtrees. We start with a degenerate forest of V single-vertex
	 * trees and perform the operation of combining two trees (using the shortest edge possible) until there is just one tree left: the MST
	 * 
	 * @return minimal spanning tree - edges that form it
	 */
	public Edge[] KruskalMSTalg()
	{
		CMSC641HW8.updateHeapSize();
		logger = Logger.getLogger(this.getClass());

		int v;
		int w;

		Vector<Edge> hlpEdg = this.g.getAllEdges();
		CMSC641HW8.updateHeapSize();

		/**
		 * Get all edges from the graph G to the array edges
		 */
		this.edges = new Edge[hlpEdg.size()];
		for (int i = 0; i < hlpEdg.size(); i++)
			this.edges[i] = hlpEdg.elementAt(i);
		CMSC641HW8.updateHeapSize();

		/**
		 * sort all edges, first in array will be the edge with the minimal length
		 */
		// logger.debug(hlpEdg.size());
		// for (int i = 0; i < hlpEdg.size(); i++)
		// logger.debug(this.edges[i].getLength() + " ; ");
		Arrays.sort(this.edges);
		CMSC641HW8.updateHeapSize();
		// for (int i = 0; i < hlpEdg.size(); i++)
		// logger.debug(this.edges[i].getLength() + " ; ");

		for (int i = 0, j = 1; i < this.edges.length && j < this.numVer; i++)
		{
			CMSC641HW8.updateHeapSize();
			v = this.edges[i].getSource();
			w = this.edges[i].getDestination();

			if (!this.uf.find(v, w))
			{
				this.uf.unite(v, w);
				this.mst[j++] = this.edges[i];
			}
		}
		CMSC641HW8.updateHeapSize();

		return this.mst;
	}
}
