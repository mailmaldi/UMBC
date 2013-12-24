package com.umbc.practice;

public class PowerOfInteger
{
	private static int ipow(int x, int n)
	{
		int result = 1;
		while (n != 0)
		{
			if ((n & 1) == 1) // exp % 2 == 1
				result *= x;
			n >>= 1; // exp = exp / 2
			x *= x;
		}

		return result;
	}

	private static double power(double x, int n)
	{
		if (n == 0)
			return 1;

		double d = power(x, n / 2);

		if (n % 2 == 0)
			return d * d;
		else
			return x * d * d;
	}

	public static void main(String[] args)
	{
		System.out.println(ipow(2, 5));
	}

}
