package main.java.umbc.practice;

import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

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

	// In place quicksort on array http://0code.blogspot.com/2012/03/quick-sort-bluej.html
	public static void quickSort(int[] arr, int left, int right)
	{
		if (left >= right)
			return;
		int pivot = (left + right) / 2;
		swap(arr, left, pivot);
		int last = left;
		// SAVING PIVOT ELEMENT AT LB LOCATION
		for (int i = left + 1; i <= right; i++)
		{
			if (arr[i] < arr[left])
				swap(arr, ++last, i);
		}
		// REPLACING PIVOT ELEMENT IN FINAL POSITION
		swap(arr, left, last);
		quickSort(arr, left, last - 1);
		quickSort(arr, last + 1, right);
	}

	// quickSelect for kth smallest element , non recursive, i.e. iterative
	public static int quickSelectIterative(int[] arr, int k, int left, int right)
	{
		if (arr == null || arr.length < k)
			return -1;

		int pivot = -1;
		int last = -1;
		while (left <= right)
		{
			pivot = (left + right) / 2;
			swap(arr, left, pivot);
			last = left;
			// SAVING PIVOT ELEMENT AT LB LOCATION
			for (int i = left + 1; i <= right; i++)
			{
				if (arr[i] < arr[left])
					swap(arr, ++last, i);
			}
			// REPLACING PIVOT ELEMENT IN FINAL POSITION
			swap(arr, left, last);
			if (k <= last - left) // k <= length(A1)
			{
				// return quickSelect(arr, k, left, last - 1); // return QuickSelect(A1, k)
				right = last - 1;
			}
			else if (k > (right - left + 1) - (right - last)) // k > length(A) - length(A2)
			{
				k = k - ((right - left + 1) - (right - last));
				left = last + 1;
				// return quickSelect(arr, k - ((right - left + 1) - (right - last)), last + 1, right); // return QuickSelect(A2, k - (length(A) - length(A2))
			}
			else
				return arr[last];
		}
		return arr[last];

	}

	// quickSelect for kth smallest element , http://pine.cs.yale.edu/pinewiki/QuickSelect
	public static int quickSelect(int[] arr, int k, int left, int right)
	{
		int pivot = (left + right) / 2;
		swap(arr, left, pivot);
		int last = left;
		// SAVING PIVOT ELEMENT AT LB LOCATION
		for (int i = left + 1; i <= right; i++)
		{
			if (arr[i] < arr[left])
				swap(arr, ++last, i);
		}
		// REPLACING PIVOT ELEMENT IN FINAL POSITION
		swap(arr, left, last);
		if (k <= last - left) // k <= length(A1)
			return quickSelect(arr, k, left, last - 1); // return QuickSelect(A1, k)
		else if (k > (right - left + 1) - (right - last)) // k > length(A) - length(A2)
			return quickSelect(arr, k - ((right - left + 1) - (right - last)), last + 1, right); // return QuickSelect(A2, k - (length(A) - length(A2))
		else
			return arr[last];
	}

	// quickSelect for kth largest element
	public static int quickSelectLargest(int[] arr, int k, int left, int right)
	{
		int pivot = (left + right) / 2;
		swap(arr, left, pivot);
		int last = left;
		// SAVING PIVOT ELEMENT AT LB LOCATION
		for (int i = left + 1; i <= right; i++)
		{
			if (arr[i] >= arr[left])
				swap(arr, ++last, i);
		}
		// REPLACING PIVOT ELEMENT IN FINAL POSITION
		swap(arr, left, last);
		if (k <= last - left) // k <= length(A1)
			return quickSelectLargest(arr, k, left, last - 1); // return QuickSelect(A1, k)
		else if (k > (right - left + 1) - (right - last)) // k > length(A) - length(A2)
			return quickSelectLargest(arr, k - ((right - left + 1) - (right - last)), last + 1, right); // return QuickSelect(A2, k - (length(A) - length(A2))
		else
			return arr[last];
	}

	// http://blog.teamleadnet.com/2012/07/quick-select-algorithm-find-kth-element.html
	public static int selectKth(int[] arr, int k)
	{
		if (arr == null || arr.length <= k)
			throw new Error();

		int left = 0, right = arr.length - 1;

		// if from == to we reached the kth element
		while (left < right)
		{
			int r = left, w = right;
			int mid = arr[(r + w) / 2];

			// stop if the reader and writer meets
			while (r < w)
			{

				if (arr[r] >= mid)
				{ // put the large values at the end
					int tmp = arr[w];
					arr[w] = arr[r];
					arr[r] = tmp;
					w--;
				}
				else
				{ // the value is smaller than the pivot, skip
					r++;
				}
			}

			// if we stepped up (r++) we need to step one down
			if (arr[r] > mid)
				r--;

			// the r pointer is on the end of the first k elements
			if (k <= r)
			{
				right = r;
			}
			else
			{
				left = r + 1;
			}
		}

		return arr[k];
	}

	private static void swap(int[] arr, int i, int j)
	{
		int temp = arr[i];
		arr[i] = arr[j];
		arr[j] = temp;
	}

	/**
	 * @param args
	 */
	public static void main(String[] args)
	{
		printGCStatus();

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
		System.out.println();
		System.out.println();
		System.out.println();

		int size = 100;
		int[] array = generateRandomArray(size);
		printArray(array);

		for (int k = 1; k <= 100; k++)
		{
			int recursive = quickSelect(array, k, 0, size - 1);
			int iterative = quickSelectIterative(array, k, 0, size - 1);
			// if (recursive != iterative)
			System.out.println(k + " smallest=" + recursive + " " + iterative);
			// System.out.print(quickSelectLargest(array, k, 0, size - 1) + " ");
		}

		quickSort(array, 0, size - 1);
		printArray(array);

	}

	public static int[] generateRandomArray(int n)
	{

		Random r = new Random();
		int[] ar = new int[n];
		for (int i = 0; i < n; i++)
			ar[i] = r.nextInt(100);

		return ar;

	}

	public static int sumArray(int[] array)
	{
		int sum = 0;
		for (int i = 0; i < array.length; i++)
			sum += array[i];
		return sum;
	}

	public static void printArray(int[] arr)
	{
		for (int i = 0; i < arr.length; i++)
			System.out.print(arr[i] + " ");
		System.out.println();
		System.out.println("sum=" + sumArray(arr));
	}

	public static void printGCStatus()
	{
		long freeMemory = Runtime.getRuntime().freeMemory(); 
		long totalMemory = Runtime.getRuntime().totalMemory(); 
		long maxMemory = Runtime.getRuntime().maxMemory(); 

		System.out.println("JVM freeMemory: " + freeMemory);
		System.out.println("JVM totalMemory also equals to initial heap size of JVM : " + totalMemory);
		System.out.println("JVM maxMemory also equals to maximum heap size of JVM: " + maxMemory);

	}
}
