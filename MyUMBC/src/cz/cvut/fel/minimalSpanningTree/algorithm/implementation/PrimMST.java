/**
 *
 */
package cz.cvut.fel.minimalSpanningTree.algorithm.implementation;

import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;
import java.util.Vector;

import org.apache.log4j.Logger;

import cz.cvut.fel.minimalSpanningTree.core.CMSC641HW8;
import cz.cvut.fel.minimalSpanningTree.dataStructures.DataStructures;
import cz.cvut.fel.minimalSpanningTree.dataStructures.implementation.support.Edge;
import cz.cvut.fel.minimalSpanningTree.dataStructures.implementation.support.EdgeComparator;

/**
 * Prim's algorithm
 * 
 * @author Shaki
 * @since November 2008
 */
public class PrimMST
{
	static Logger logger;

	// private representations

	/**
	 * Maximum possible weight of the edge. Used for the initialization of the weights, all values set to this max value.
	 */
	private static final double MAX_WEIGHT = 1000000;

	/**
	 * Array of edges, which form the MST of the graph
	 */
	private Edge[] mst;

	/**
	 * Array of edges, each item is the shortest edge connecting each non-tree vertex to the tree
	 */
	private Edge[] fringe; // fringe = okraj

	private TreeMap<Edge, Integer> fringeList; // pre variantu so spojovym zoznamom

	/**
	 * Array of weights (of edges), each item is the length of the shortest edge connecting each non-tree vertex to the tree (weight of the edge going from the vertex to the
	 * shortest vertex, which is part of the MST)
	 */
	private double[] weights;

	private Edge[] weightsList; // pre variantu so spojovym zoznamom

	/**
	 * Graph representation on which we are searching for MST
	 */
	private DataStructures g;

	// constructors and methods

	/**
	 * constructor for generic graph representation
	 * 
	 * @param G
	 *            Graph
	 */
	public PrimMST(DataStructures G)
	{
		this.g = G;
	}

	/**
	 * Prim's algorithm
	 * 
	 * Move a minimal edge from the fringe to the tree. Visit the vertex that it leads to, and put onto the fringe any edges that lead from that vertex to an non-tree vertex,
	 * replacing the longer edge when two edges on the fringe point to the same vertex.
	 * 
	 * @param rep
	 *            graph type representation
	 * 
	 * @return minimal spanning tree - edges that form it
	 */
	@SuppressWarnings("boxing")
	public Edge[] PrimMSTalg(String rep)
	{
		CMSC641HW8.updateHeapSize();
		logger = Logger.getLogger(this.getClass());

		/**
		 * data structures initialization
		 */
		this.weights = new double[this.g.getNumVertices()];
		this.weightsList = new Edge[this.g.getNumEdges()];
		this.mst = new Edge[this.g.getNumVertices()];
		this.fringe = new Edge[this.g.getNumVertices()];
		this.fringeList = new TreeMap<Edge, Integer>(new EdgeComparator());
		CMSC641HW8.updateHeapSize();
		Edge tmpMin = new Edge(0, 0, MAX_WEIGHT);

		for (int i = 0; i < this.g.getNumVertices(); i++)
		{
			this.weights[i] = MAX_WEIGHT;
			this.weightsList[i] = tmpMin;
			this.mst[i] = null;
		}
		CMSC641HW8.updateHeapSize();
		this.mst[0] = tmpMin;

		int min = -1;

		/**
		 * algorithm
		 */
		// matrix
		if ((rep.compareTo("m") == 0) || (rep.compareTo("m2") == 0))
		{
			for (int i = 0; min != 0; i = min)
			{
				Edge e;
				min = 0;

				for (int j = 1; j < this.g.getNumVertices(); j++)
				{
					if (this.mst[j] == null)
					{
						double hlp = 0.0;
						e = this.g.getEdge(i, j);
						/**
						 * if edge exists
						 */
						if (e != null)
							if ((hlp = e.getLength()) < this.weights[j])
							{
								this.weights[j] = hlp;
								this.fringe[j] = e;
							}
						/**
						 * new minimum edge found
						 */
						if (this.weights[j] < this.weights[min])
							min = j;
					}
				}

				/**
				 * add the minimum edge to the MST tree
				 */
				if (min != 0)
					this.mst[min] = this.fringe[min];
			}
		}

		// for list
		if ((rep.compareTo("l") == 0) || (rep.compareTo("l2") == 0))
		{
			CMSC641HW8.updateHeapSize();
			for (int i = 0; min != 0; i = min)
			{
				min = 0;

				Vector<Edge> tmpE = this.g.getAllEdgesAdjacentToVertex(i);
				CMSC641HW8.updateHeapSize();
				Iterator<Edge> j = tmpE.listIterator();
				while (j.hasNext())
				{
					Edge tmp = j.next();

					int u; // pomocny uzol, aby som vedel, ktory uzol != i
					if (tmp.getSource() == i)
						u = tmp.getDestination();
					else
						u = tmp.getSource();
					/*
					 * if ( this.mst[u] == null) { if ( tmp.getLength() < this.weights[u]) { this.weights[u] = tmp.getLength(); this.fringe[u] = tmp; }
					 */
					CMSC641HW8.updateHeapSize();
					if (this.mst[u] == null)
						if (tmp.getLength() < this.weightsList[u].getLength())
						{
							// zmazem z treemap staru hranu s vacsou dlzkou
							this.fringeList.remove(this.weightsList[u]);
							// System.out.println("Mazem " + this.weightsList[u].getLength() + " " + this.weightsList[u]);
							// pridam do treemap novu hranu s mensou dlzkou
							Integer nI = new Integer(u);
							this.fringeList.put(tmp, nI);
							// System.out.println("Pridavam " + tmp.getLength());
							// upravim aj v prehlade dlzok do jednotlivych uzlov
							this.weightsList[u] = tmp;
						}
				}

				Map.Entry<Edge, Integer> mapMin = this.fringeList.firstEntry();

				if (mapMin != null)
				{
					min = mapMin.getValue();
					this.fringeList.remove(mapMin.getKey());

					/**
					 * add the minimum edge to the MST tree
					 */
					this.mst[min] = mapMin.getKey();
				}
				else
				{
					min = 0;
					// System.out.println("NULL NULL NULL NEMAM MINIMUM");
				}
				CMSC641HW8.updateHeapSize();
			}
		}
		/*
		 * for (int xx=0; xx<this.g.getNumVertices();xx++) if (this.mst[xx] == null) System.out.println("MST NULL pre uzol c. " + xx);
		 */
		this.mst[0] = null;
		CMSC641HW8.updateHeapSize();
		return this.mst;
	}

}
