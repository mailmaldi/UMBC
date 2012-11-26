package com.umbc.practice;

import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class QuickSortNaive
{

	public static List<Integer> quickSortNaive(List<Integer> inputList)
	{
		if (inputList.size() <= 1)
			return inputList;

		int pivot = inputList.remove(0);

		List<Integer> lessThan = new LinkedList<Integer>();
		List<Integer> greaterThan = new LinkedList<Integer>();

		for (Integer integer : inputList)
		{
			if (integer <= pivot)
				lessThan.add(integer);
			else if (integer > pivot)
				greaterThan.add(integer);
		}

		List<Integer> returnList = new LinkedList<Integer>();
		returnList.addAll(quickSortNaive(lessThan));
		returnList.add(pivot);
		returnList.addAll(quickSortNaive(greaterThan));

		return returnList;

	}

	/**
	 * @param args
	 */
	public static void main(String[] args)
	{
		List<Integer> inputList = new LinkedList<Integer>();
		inputList.addAll(Arrays.asList(1, 3, 2, 4, 3, 5, 4, 5, 6, 7, 8, 9, 10, 9, 8, 7, 6, 5, 4, 3, 2, 1));
		Collections.shuffle(inputList);

		for (Integer integer : inputList)
		{
			System.out.print(integer);
			System.out.print(" ");
		}
		System.out.println();
		System.out.println();

		List<Integer> outList = quickSortNaive(inputList);

		for (Integer integer : outList)
		{
			System.out.print(integer);
			System.out.print(" ");
		}

	}

}
