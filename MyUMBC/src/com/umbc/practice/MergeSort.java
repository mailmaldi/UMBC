package com.umbc.practice;

public class MergeSort
{

	private int[] numbers;

	private int[] helper;

	private int number;

	public void sort(int[] values)
	{
		this.numbers = values;
		number = values.length;
		this.helper = new int[number];
		mergesort(0, number - 1);
	}

	private void mergesort(int low, int high)
	{
		// Check if low is smaller then high, if not then the array is sorted
		if (low < high)
		{
			// Get the index of the element which is in the middle
			int middle = (low + high) / 2;
			// Sort the left side of the array
			mergesort(low, middle);
			// Sort the right side of the array
			mergesort(middle + 1, high);
			// Combine them both
			merge(low, middle, high);
		}
	}

	private void merge(int low, int middle, int high)
	{

		// Copy both parts into the helper array
		for (int i = low; i <= high; i++)
		{
			helper[i] = numbers[i];
		}

		int i = low;
		int j = middle + 1;
		int k = low;
		// Copy the smallest values from either the left or the right side back
		// to the original array
		while (i <= middle && j <= high)
		{
			if (helper[i] <= helper[j])
			{
				numbers[k] = helper[i];
				i++;
			}
			else
			{
				numbers[k] = helper[j];
				j++;
			}
			k++;
		}
		// Copy the rest of the left side of the array into the target array
		while (i <= middle)
		{
			numbers[k] = helper[i];
			k++;
			i++;
		}

	}

	public static int[] Iterative(int[] array)
	{
		for (int i = 1; i <= array.length / 2 + 1; i *= 2)
		{
			for (int j = i; j < array.length; j += 2 * i)
			{
				merge(array, j - i, j, Math.min(j + i, array.length));
			}
		}

		return array;
	}

	private static void merge(int[] array, int low, int middle, int high)
	{
		int[] helper = new int[array.length];

		// Copy both parts into the helper array
		for (int i = low; i <= high; i++)
		{
			helper[i] = array[i];
		}

		int i = low;
		int j = middle + 1;
		int k = low;
		// Copy the smallest values from either the left or the right side back
		// to the original array
		while (i <= middle && j <= high)
		{
			if (helper[i] <= helper[j])
			{
				array[k] = helper[i];
				i++;
			}
			else
			{
				array[k] = helper[j];
				j++;
			}
			k++;
		}
		// Copy the rest of the left side of the array into the target array
		while (i <= middle)
		{
			array[k] = helper[i];
			k++;
			i++;
		}

	}

	public static void myMergeSort(int array[], int start, int end)
	{
		int low = start;
		int high = end;
		if (low >= high)
		{
			return;
		}

		int middle = (low + high) / 2;
		myMergeSort(array, low, middle);
		myMergeSort(array, middle + 1, high);
		int end_low = middle;
		int start_high = middle + 1;
		while ((start <= end_low) && (start_high <= high))
		{
			if (array[low] < array[start_high])
			{
				low++;
			}
			else
			{
				int Temp = array[start_high];
				for (int k = start_high - 1; k >= low; k--)
				{
					array[k + 1] = array[k];
				}
				array[low] = Temp;
				low++;
				end_low++;
				start_high++;
			}
		}
	}

}
