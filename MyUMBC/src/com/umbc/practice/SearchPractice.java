package com.umbc.practice;

public class SearchPractice
{

	public static void main(String[] args)
	{
		int[] a = { 7, 8, 9, 1, 2, 3, 4, 5, 6 };
		System.out.println(search(a, 8));

	}

	public static int search(int a[], int l, int u, int x)
	{
		while (l <= u)
		{
			int m = (l + u) / 2;
			if (x == a[m])
				return m;
			if (x == a[l])
				return l;
			if (x == a[u])
				return u;
			else if (a[l] <= a[m])
			{
				if (x > a[m])
					l = m + 1;
				else if (x > a[l])
					u = m - 1;
				else
					l = m + 1;
			}
			else if (x < a[m])
				u = m - 1;
			else if (x < a[u])
				l = m + 1;
			else
				u = m - 1;
		}
		return -1;
	}

	public static int search(int a[], int x)
	{
		return search(a, 0, a.length - 1, x);
	}

	// simple bsearch
	public static int binarySearch(int[] a, int first, int last, int key)
	{
		int result = 0;

		if (first > last)
			result = -1;
		else
		{
			int mid = (first + last) / 2;

			if (key == a[mid])
				result = mid;
			else if (key < a[mid])
				result = search(a, first, mid - 1, key);
			else if (key > a[mid])
				result = search(a, mid + 1, last, key);
		}
		return result;
	}

	public static int binSearchIterative(int[] a, int lower, int upper, int key)
	{
		while (lower <= upper)
		{
			int mid = (lower + upper) / 2;

			if (key == a[mid])
				return mid;
			else if (key < a[mid])
				upper = mid - 1;
			else
				lower = mid + 1;
		}
		return -1;
	}

	// search in matrix , cracking pg 66
	boolean FindElem(int[][] mat, int elem, int M, int N)
	{
		int row = 0;
		int col = N - 1;
		while (row < M && col >= 0)
		{
			if (mat[row][col] == elem)
				return true;
			else if (mat[row][col] > elem)
				col--;
			else
				row++;
		}
		return false;
	}
}
