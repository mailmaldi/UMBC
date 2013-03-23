package com.umbc.practice;

public class ArrayProblems
{

	public static int commonElementFromSorted(int[] a, int aSize, int[] b, int bSize)
	{
		int result = -1;
		if (a[0] > b[bSize - 1] || b[0] > a[aSize - 1])
			return result;

		int i = 0, j = 0;
		while (i < aSize && j < bSize)
		{
			if (a[i] == b[j])
				return a[i];
			else if (a[i] < b[j])
				i++;
			else
				j++;
		}
		return result;
	}

	public static void main(String[] args)
	{
		int[] a = { 1, 2, 3, 4, 5 };
		int[] b = { 0, 5, 7, 8, 9 };
		System.out.println(commonElementFromSorted(a, 5, b, 5));

	}

}
