package umbc.palantir;

import java.util.Collections;
import java.util.LinkedList;
import java.util.Scanner;

public class Solution
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

		int adjacencyMatrix[][] = new int[N * N][N * N];
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
					adjacencyMatrix[sinkPossible.i * N + sinkPossible.j][i * N + j] = 1;
				}

			}
		}// end populating adjacency matrix

		// for (Position position : sinks)
		// {
		// System.out.println("Sink=" + position.i + " " + position.j);
		// }

		// for (int i = 0; i < N * N; i++)
		// {
		// for (int j = 0; j < N * N; j++)
		// System.out.print(adjacencyMatrix[i][j] + " ");
		// System.out.println();
		// }
		// System.out.println("Num Sinks: " + sinks.size());

		LinkedList<Integer> decreasing = new LinkedList<Integer>();
		for (Position position : sinks)
		{
			LinkedList<Integer> queue = new LinkedList<Integer>();
			int count = 0;
			queue.add(position.i * N + position.j);
			while (!queue.isEmpty())
			{
				int pos = queue.removeFirst();
				// System.out.print(pos + " ");
				count++;
				// pop from queue and add if edge exists & increase count
				for (int k = 0; k < N * N; k++)
				{
					if (adjacencyMatrix[pos][k] == 1)
					{
						queue.add(k);
						// System.out.print(k + " ");
					}
				}
			}
			// System.out.println(count);
			decreasing.add(count);
		}
		Collections.sort(decreasing);
		for (int i = decreasing.size() - 1; i >= 0; i--)
		{
			System.out.print(decreasing.get(i) + " ");
		}

	}
}
