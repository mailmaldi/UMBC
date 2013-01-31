package com.umbc.practice;

import java.util.TreeSet;

public class QuickSelect
{

	public static void main(String[] args)
	{
		int a[] = { 9, 8, 7, 6, 5, 4, 3, 2, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 6, 5, 4 };
		Integer[] b = selectK(a, 5);
		for (Integer integer : b)
		{
			System.out.print(integer + " ");
		}
	}

	public static void addElementToTreeSet(TreeSet<Integer> tSet, int kSize, int dataElement)
	{
		tSet.add(dataElement);
		if (tSet.size() > kSize)
			tSet.remove(tSet.first());
	}

	public static Integer[] selectK(int[] dataArray, int k)
	{
		TreeSet<Integer> ts = new TreeSet<Integer>();
		for (int i : dataArray)
		{
			addElementToTreeSet(ts, k, i);
		}
		return ts.toArray(new Integer[k]);
	}

}
