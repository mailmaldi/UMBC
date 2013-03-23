package com.umbc.practice;

import java.util.LinkedList;

public class TempTest
{

	public static void main(String[] args)
	{
		// int x = 123;
		// boolean f = ((x > 0) && ((x & (x - 1)) == 0));
		// System.out.println(Integer.MAX_VALUE + " " + Integer.MIN_VALUE);
		// System.out.println(-4 % 2);
		// System.out.println(parity("helloo"));
		//
		// System.out.println();
		// System.out.println(hash(10));

		System.out.println();
		System.out.println(josephus(2, 2));

	}

	public static int josephus(int n, int k)
	{
		if (n == 1)
			return 1;
		else
			return ((josephus(n - 1, k) + k - 1) % n) + 1;
	}

	public static String parity(String msg)
	{
		String parityStr = null;
		int n = msg.hashCode();
		System.out.println(n);
		switch (n % 2)
		{
		case 0:
			parityStr = "even";
			break;
		case 1:
			parityStr = "odd";
			break;
		}
		return parityStr;
	}

	public static int zeroes(int n)
	{
		int r = -1;
		// 5 ^ k <= n
		int k = (int) (Math.log10(n) / Math.log10(5));
		return r;
	}

	public static final int hash(int a)
	{
		a ^= (a << 13);
		a ^= (a >>> 17);
		a ^= (a << 5);
		return a;
	}

}
