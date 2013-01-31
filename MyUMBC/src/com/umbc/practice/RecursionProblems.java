package com.umbc.practice;

public class RecursionProblems
{

	public static int[] allFactorials(int n)
	{ /* Wrapper routine */
		int[] results = new int[n == 0 ? 1 : n];
		doAllFactorials(n, results, 0);
		return results;
	}

	public static int doAllFactorials(int n, int[] results, int level)
	{
		if (n > 1)
		{ /* Recursive case */
			results[level] = n * doAllFactorials(n - 1, results, level + 1);
			return results[level];
		}
		else
		{ /* Base case */
			results[level] = 1;
			return 1;
		}
	}

	static final int NOT_IN_ARRAY = -1;

	static final int ARRAY_UNORDERED = -2;

	static final int LIMITS_REVERSED = -3;

	public static int binarySearch(int[] array, int lower, int upper, int target)
	{
		int mid, range;
		range = upper - lower;

		if (range < 0)
		{
			return LIMITS_REVERSED;
		}
		else if (range == 0 && array[lower] != target)
		{
			return NOT_IN_ARRAY;
		}
		if (array[lower] > array[upper])
			return ARRAY_UNORDERED;

		mid = ((range) / 2) + lower;
		if (target == array[mid])
		{
			return mid;
		}
		else if (target < array[mid])
		{
			return binarySearch(array, lower, mid - 1, target);
		}
		else
		{
			return binarySearch(array, mid + 1, upper, target);
		}
	}

	public static int iterBinarySearch(int[] array, int lower, int upper, int target)
	{
		int center, range;
		if (lower > upper)
			return LIMITS_REVERSED;
		while (true)
		{
			range = upper - lower;
			if (range == 0 && array[lower] != target)
				return NOT_IN_ARRAY;
			if (array[lower] > array[upper])
				return ARRAY_UNORDERED;
			center = ((range) / 2) + lower;
			if (target == array[center])
			{
				return center;
			}
			else if (target < array[center])
			{
				upper = center - 1;
			}
			else
			{
				lower = center + 1;
			}
		}
	}

	public static void permute(String str)
	{
		int length = str.length();
		boolean[] used = new boolean[length];
		StringBuffer out = new StringBuffer();
		char[] in = str.toCharArray();
		doPermute(in, out, used, length, 0);
	}

	public static void doPermute(char[] in, StringBuffer out, boolean[] used, int length, int level)
	{
		if (level == length)
		{
			System.out.println(out.toString());
			return;
		}
		for (int i = 0; i < length; ++i)
		{
			if (used[i])
				continue;
			out.append(in[i]);
			used[i] = true;
			doPermute(in, out, used, length, level + 1);
			used[i] = false;
			out.setLength(out.length() - 1);
		}
	}

	public static void main(String[] args)
	{
		permute("abcd");

	}
}
