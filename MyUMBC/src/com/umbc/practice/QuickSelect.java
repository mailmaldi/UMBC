package com.umbc.practice;

import java.util.TreeSet;

public class QuickSelect
{

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

	public static int secondLargest(int[] array)
	{
		int num1, num2;

		if (array[0] > array[1])
		{
			num1 = array[0];
			num2 = array[1];
		}
		else
		{
			num1 = array[1];
			num2 = array[0];
		}

		for (int i = 2; i < array.length; i++)
		{
			if ((array[i] <= num1) && array[i] > num2)
			{
				num2 = array[i];
			}

			if (array[i] > num1)
			{
				num2 = num1;
				num1 = array[i];
			}
		}

		return num2;
	}

	public static int reverseInt(int input)
	{
		int reversedNum = 0;

		while (input != 0)
		{
			int last_digit = input % 10;
			reversedNum = reversedNum * 10 + last_digit;
			input = input / 10;
		}

		if (reversedNum > Integer.MAX_VALUE || reversedNum < Integer.MIN_VALUE)
		{
			throw new IllegalArgumentException();
		}
		return (int) reversedNum;
	}

	public static void main(String[] args)
	{
		// int a[] = { 9, 8, 7, 6, 5, 4, 3, 2, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 6, 5, 4 };
		// Integer[] b = selectK(a, 5);
		// for (Integer integer : b)
		// {
		// System.out.print(integer + " ");
		// }

		System.out.println(reverseInt(1234));
	}

}
