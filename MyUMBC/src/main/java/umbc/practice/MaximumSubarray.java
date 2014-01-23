package main.java.umbc.practice;

public class MaximumSubarray
{
	public static void main(String... args)
	{
		// declare and initialize an array from which we need o find the
		// contiguous
		// subarray with largest sum
		int[] array = { -1, -5, 8, -1, 7, 6, -4, 4 };

		// pass the array to method which will return the largest sum within an
		// array
		int result = scanMaxSum(array);

		// print the sum
		System.out.println(result);
	}

	// this method will take the array and return the largest sum of contiguous subarray
	public static int scanMaxSum(int[] array)
	{
		int startIndex = 0, endIndex = 0;
		// check if the array is null or does not contain any value. in this case return -1
		if (array == null || array.length == 0)
		{
			return -1;

		}

		// if array contains only one element that will be the only largest value
		else if (array.length == 1)
		{
			return array[0];
		}
		else
		{
			// variable to hold the current max sum while iterating through the array
			// by default it will contain the value of the first element in the array
			int currentMaxSum = array[0];

			// variable to hold the current max sum while iterating through the array
			// by default it will contain the value of the first element in the array
			int maxSum = array[0];

			// for loop to iterate through the array, starts with the second element in the array

			for (int i = 1; i < array.length; i++)
			{
				// if the current element in array is greater then the sum of elements till
				// that position then that will be largest value otherwise the sum of
				// all elements till that position is the largest sum. keep this value in
				// in the currentMaxSum variable
				// currentMaxSum = Math.max(array[i], currentMaxSum + array[i]);
				currentMaxSum = currentMaxSum + array[i];
				if (array[i] > currentMaxSum)
				{
					startIndex = i;
					currentMaxSum = array[i];
				}

				// this will check if the largest value/sum till this position in array
				// is greater then or less then any previous contiguous sub array
				// maxSum = Math.max(currentMaxSum, maxSum);
				if (currentMaxSum > maxSum)
				{
					maxSum = currentMaxSum;
					endIndex = i;
				}
			}
			// return the max sum of contiguous subarray in the array
			System.out.println("MaxSum= " + maxSum + " start= " + startIndex + " end= " + endIndex);
			return maxSum;
		}
	}
}
