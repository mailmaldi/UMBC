package com.umbc.practice;

public class RandomTest
{

	public static void main(String[] args)
	{
		System.out.println(Integer.MAX_VALUE);
		for (int i = 0; i < 50; i++)
			System.out.println(i + "=" + factorial(i));

	}

	public static int factorial(int num)
	{
		if (num < 0)
			return -1;
		if (num == 0 || num == 1)
			return 1;
		return num * factorial(num - 1);
	}

}
