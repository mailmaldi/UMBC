import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Scanner;

public class CopyOfSolution
{
	static class Position
	{
		public int i;

		public int j;

		public Position(int i, int j)
		{
			this.i = i;
			this.j = j;
		}
	}

	public static void main(String[] args)
	{

		Scanner sc = new Scanner(System.in);

		String line = sc.nextLine();
		int N = Integer.parseInt(line.trim());

		// int adjacencyMatrix[][] = new int[N * N][N * N];
		HashMap<Integer, LinkedList<Integer>> adjacenyList = new HashMap<Integer, LinkedList<Integer>>();

		int nodes[][] = new int[N][N];

		LinkedList<Position> sinks = new LinkedList<Position>();

		for (int lc = 0; lc < N; lc++)
		{
			line = sc.nextLine();
			String[] splits = line.split(" ");
			for (int i = 0; i < N; i++)
				nodes[lc][i] = Integer.parseInt(splits[i]);
		}

		// for (int i = 0; i < N; i++)
		// {
		// for (int j = 0; j < N; j++)
		// System.out.print(nodes[i][j] + " ");
		// System.out.println();
		// }

		for (int i = 0; i < N; i++)
		{
			for (int j = 0; j < N; j++)
			{
				int val = nodes[i][j];
				boolean isSink = true;
				int sink = val;
				Position sinkPossible = new Position(i, j);

				if (i > 0)
				{
					if (val > nodes[i - 1][j])
					{
						isSink = false;
						if (sink > nodes[i - 1][j])
						{
							sink = nodes[i - 1][j];
							sinkPossible = new Position(i - 1, j);
						}
					}
				}
				if (i < N - 1)
				{
					if (val > nodes[i + 1][j])
					{
						isSink = false;
						if (sink > nodes[i + 1][j])
						{
							sink = nodes[i + 1][j];
							sinkPossible = new Position(i + 1, j);
						}

					}
				}
				if (j > 0)
				{
					if (val > nodes[i][j - 1])
					{
						isSink = false;
						if (sink > nodes[i][j - 1])
						{
							sink = nodes[i][j - 1];
							sinkPossible = new Position(i, j - 1);
						}

					}
				}
				if (j < N - 1)
				{
					if (val > nodes[i][j + 1])
					{
						isSink = false;
						if (sink > nodes[i][j + 1])
						{
							sink = nodes[i][j + 1];
							sinkPossible = new Position(i, j + 1);
						}

					}
				}
				if (isSink == true)
				{
					sinks.add(new Position(i, j));
					// mark this value as a global sink
				}
				else
				{
					// in adj matrix edge from sinkPossible to i,j
					// adjacencyMatrix[sinkPossible.i * N + sinkPossible.j][i * N + j] = 1;
					LinkedList<Integer> nodeList = adjacenyList.get(sinkPossible.i * N + sinkPossible.j);
					if (nodeList == null)
						nodeList = new LinkedList<Integer>();
					nodeList.add(i * N + j);
					adjacenyList.put(sinkPossible.i * N + sinkPossible.j, nodeList);

				}

			}

		}// end populating adjacency matrix

		/*
		 * for (Position position : sinks) { System.out.println("Sink=" + position.i + " " + position.j + " Node=" + (position.i * N + position.j)); }
		 */

		// System.out.println("Num Sinks: " + sinks.size());
		sc.close();
		nodes = null;

		// for (Integer integer : adjacenyList.keySet())
		// {
		// System.out.print("Key=" + integer + "->");
		// for (Integer i : adjacenyList.get(integer))
		// System.out.print(" " + i);
		// System.out.println();
		// }

		LinkedList<Integer> decreasing = new LinkedList<Integer>();
		for (Position position : sinks)
		{
			LinkedList<Integer> queue = new LinkedList<Integer>();
			int count = 0;
			int sinkPosition = position.i * N + position.j;
			queue.add(sinkPosition);
			// System.out.print(sinkPosition + " ");
			while (!queue.isEmpty())
			{
				int pos = queue.removeFirst();
				count++;
				// pop from queue and add if edge exists & increase count
				LinkedList<Integer> list = adjacenyList.get(pos);
				if (list != null)
					for (Integer integer : list)
					{
						queue.add(integer);
						// System.out.print(integer + " ");
					}
			}
			// System.out.print(" " + count);
			decreasing.add(count);
		}
		// System.gc();
		// System.out.println("DONE:");
		Collections.sort(decreasing);
		StringBuffer sb = new StringBuffer();
		try
		{
			BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(System.out));
			for (int i = decreasing.size() - 1; i >= 0; i--)
			{
				bw.write(decreasing.get(i) + " ");
				bw.newLine();
				// sb.append(decreasing.get(i)).append(" ");
				// System.out.print(decreasing.get(i)+ " "); // problem with console printing on eclipse
			}
			bw.flush();
			bw.close();
		}
		catch (Exception e)
		{
		}

		// System.out.println(sb.toString());

	}
}
