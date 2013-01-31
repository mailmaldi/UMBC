package com.umbc.practice;

import java.util.ArrayList;
import java.util.List;

public class EfficientPrime
{

	// http://stackoverflow.com/questions/9625663/calculating-and-printing-the-nth-prime-number
	// http://en.wikipedia.org/wiki/Sieve_of_Eratosthenes
	public static int nthPrime(int n)
	{
		if (n < 2)
			return 2;
		if (n == 2)
			return 3;
		int limit, root, count = 1;
		limit = (int) (n * (Math.log(n) + Math.log(Math.log(n)))) + 3;
		root = (int) Math.sqrt(limit) + 1;
		limit = (limit - 1) / 2;
		root = root / 2 - 1;
		boolean[] sieve = new boolean[limit];
		for (int i = 0; i < root; ++i)
		{
			if (!sieve[i])
			{
				++count;
				for (int j = 2 * i * (i + 3) + 3, p = 2 * i + 3; j < limit; j += p)
				{
					sieve[j] = true;
				}
			}
		}
		int p;
		for (p = root; count < n; ++p)
		{
			if (!sieve[p])
			{
				++count;
			}
		}
		return 2 * p + 1;
	}

	// generate first N prime numbers
	public static List<Integer> generateNPrimes(int n)
	{
		List<Integer> primeList = new ArrayList<Integer>();
		primeList.add(2);
		if (n < 2)
			return primeList;

		int size = primeList.size();
		int toCheck = 3;
		while (size < n)
		{
			if (isPrime(toCheck, primeList))
			{
				primeList.add(toCheck);
				size++;
			}
			toCheck += 2;
		}

		return primeList;
		// return primeList.toArray(new Integer[primeList.size()]);
	}

	private static boolean isPrime(int toCheck, List<Integer> primeList)
	{
		boolean isPrime = true;
		int root = (int) Math.sqrt(toCheck) + 1;

		for (Integer i : primeList)
		{
			if (i > root)
				break;
			if (toCheck % i == 0)
				return false;
		}

		return isPrime;
	}

	public static List<Integer> simpleSieve(int N)
	{
		List<Integer> list = new ArrayList<Integer>();
		boolean[] isPrime = new boolean[N + 1];
		for (int i = 2; i <= N; i++)
		{
			isPrime[i] = true;
		}

		// mark non-primes <= N using Sieve of Eratosthenes
		for (int i = 2; i * i <= N; i++)
		{

			// if i is prime, then mark multiples of i as nonprime
			// suffices to consider mutiples i, i+1, ..., N/i
			if (isPrime[i])
			{
				for (int j = i; i * j <= N; j++)
				{
					isPrime[i * j] = false;
				}
			}
		}

		for (int i = 2; i <= N; i++)
		{
			if (isPrime[i])
				list.add(i);
		}
		return list;
	}

	public static void main(String[] args)
	{
		List<Integer> primeList = generateNPrimes(100);
		printList(primeList);
		primeList = simpleSieve(545);
		printList(primeList);
	}

	public static void printList(List<Integer> list)
	{
		for (Integer integer : list)
		{
			System.out.print(integer + " ");
		}
		System.out.println();

	}

}
