package cz.cvut.fel.minimalSpanningTree.core;

import java.util.Random;

import cz.cvut.fel.minimalSpanningTree.algorithm.implementation.KruskalMST;
import cz.cvut.fel.minimalSpanningTree.algorithm.implementation.PrimMST;
import cz.cvut.fel.minimalSpanningTree.dataStructures.DataStructures;
import cz.cvut.fel.minimalSpanningTree.dataStructures.implementation.GraphAdjList;
import cz.cvut.fel.minimalSpanningTree.dataStructures.implementation.support.Edge;

public class CMSC641HW8
{
	// -XX:+PrintGCDetails

	private static long minHeapSize = 0;

	private static long maxHeapSize = 0;

	public static void main(String[] args) throws Exception
	{
		runSpaceAnalysis();
		// runTimeAnalysis();
	}

	public static void runSpaceAnalysis() throws Exception
	{
		int initial = 10;
		int last = 2000;
		for (int i = initial; i <= last;)
		{
			System.gc();
			Thread.sleep(10000);

			resetHeapSize();
			updateHeapSize();
			DataStructures G = getSparseGraph(i, 5); // getCircleGraph(i); //getFullGraph(i); //
			updateHeapSize();

			KruskalMST kMST = new KruskalMST(G);
			updateHeapSize();

			Edge[] mst = kMST.KruskalMSTalg();

			System.out.println(i + "," + getHeapSizeDiff());

			if (i < 10)
				i++;
			else if (i < 100)
				i = i + 10;
			else if (i < 1000)
				i = i + 100;
			else
				i = i + 250;
		}

	}

	public static void runTimeAnalysis() throws Exception
	{
		for (int i = 10; i <= 10000;)
		{
			DataStructures G = getSparseGraph(i, 1); // getCircleGraph(i); //getFullGraph(i); //
			runAnalysis(G);

			if (i < 10)
				i++;
			else if (i < 100)
				i = i + 10;
			else if (i < 1000)
				i = i + 100;
			else
				i = i + 250;
		}
	}

	public static void runAnalysis(DataStructures G)
	{

		KruskalMST kMST = new KruskalMST(G);
		long start = System.currentTimeMillis();
		Edge[] mst = kMST.KruskalMSTalg();
		long stop = System.currentTimeMillis();
		long kRT = stop - start;

		PrimMST pMST = new PrimMST(G);
		start = System.currentTimeMillis();
		mst = pMST.PrimMSTalg("l");
		stop = System.currentTimeMillis();
		long pRT = stop - start;

		System.out.println(G.getNumVertices() + "," + kRT + "," + pRT);

	}

	public static DataStructures getFullGraph(int num_vertices) throws Exception
	{
		Random random = new Random(System.currentTimeMillis());

		DataStructures G = new GraphAdjList();

		G.setNumVertices(num_vertices);
		G.setNumEdges(num_vertices * (num_vertices - 1) / 2);

		for (int i = 0; i < num_vertices; i++)
		{
			for (int j = i + 1; j < num_vertices; j++)
			{

				G.addEdge(i, j, 1); // TODO change 1 to an arbitrary weight random.nextInt(1000)
			}
		}
		return G;
	}

	public static DataStructures getCircleGraph(int num_vertices) throws Exception
	{
		Random random = new Random(System.currentTimeMillis());
		DataStructures G = new GraphAdjList();

		G.setNumVertices(num_vertices);
		G.setNumEdges(num_vertices);

		for (int i = 0; i < num_vertices; i++)
		{

			G.addEdge(i, (i + 1) % num_vertices, 1); // TODO change 1 to an arbitrary weight random.nextInt(1000)
		}

		return G;
	}

	public static DataStructures getSparseGraph(int num_vertices, int branching_factor) throws Exception
	{
		Random random = new Random(System.currentTimeMillis());
		DataStructures G = new GraphAdjList();

		G.setNumVertices(num_vertices);
		G.setNumEdges(num_vertices * branching_factor);
		int count = 0;

		for (int i = 0; i < num_vertices; i++)
		{
			for (int j = 0; j < branching_factor; j++)
			{
				try
				{
					count++;
					G.addEdge(i, (i + j + 1) % num_vertices, random.nextInt(10) + 1); // TODO change 1 to an arbitrary weight
				}
				catch (Exception e)
				{
					count--;
				}
			}
		}
		G.setNumEdges(count);
		return G;
	}

	public static int getRandom(Random random, int i, int range)
	{
		int rand = random.nextInt(range);
		// if rand is i or i+1 , then generate rand again
		while ((rand == i) || (rand >= range) || (rand == ((i + 1) % range)))
		{
			rand = random.nextInt(range);
		}
		return rand;
	}

	public static long getHeapSize()
	{

		// Getting the runtime reference from system
		Runtime runtime = Runtime.getRuntime();

		return ((runtime.totalMemory() - runtime.freeMemory()));
	}

	public static void printHeapSize()
	{
		System.out.println(getHeapSize());
	}

	public static void updateHeapSize()
	{
		long currentHeapSize = getHeapSize();
		if (currentHeapSize < minHeapSize || minHeapSize == 0)
			minHeapSize = currentHeapSize;
		if (currentHeapSize > maxHeapSize || maxHeapSize == 0)
			maxHeapSize = currentHeapSize;
	}

	public static long getHeapSizeDiff()
	{
		return (maxHeapSize - minHeapSize);
	}

	public static void printHeapSizeDiff()
	{
		System.out.println(getHeapSizeDiff());
	}

	private static void resetHeapSize()
	{
		minHeapSize = 0;
		maxHeapSize = 0;
	}

}
