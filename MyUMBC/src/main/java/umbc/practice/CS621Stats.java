package main.java.umbc.practice;

import java.util.ArrayList;
import java.util.List;

public class CS621Stats
{

	/**
	 * @param args
	 */
	public static void main(String[] args)
	{
		int N = 3000;
		int multiplier = 2;
		float seedValue = 1.5f;

		float[] myDataList = new float[multiplier * N];

		// 0 0,1 1 2,3 2 4,5 3 6,7
		for (int i = 0; i < N; i++)
			for (int j = 0; j < multiplier; j++)
				myDataList[(i * multiplier) + j] = (seedValue + i + 1 + j);

		for (int i = 0; i <= multiplier * N - 1; i++)
			System.out.print(myDataList[i] + " ");
		System.out.println();

		System.out.println("min : " + (seedValue + multiplier-1));
		System.out.println("max : " + (N + seedValue + multiplier-1));
		System.out.println("mean : " + mean(myDataList));
		System.out.println("median : " + median(myDataList));

	}

	public static float median(float[] m)
	{
		int middle = m.length / 2;
		if (m.length % 2 == 1)
		{
			return m[middle];
		}
		else
		{
			return (float) ((m[middle - 1] + m[middle]) / 2.0);
		}
	}

	public static float mean(float[] m)
	{
		double sum = 0;
		for (int i = 0; i < m.length; i++)
		{
			sum += m[i];
		}
		return (float) (sum / m.length);
	}

}
