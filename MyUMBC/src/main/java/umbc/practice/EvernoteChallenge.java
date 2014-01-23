package umbc.practice;

public class EvernoteChallenge
{
	// https://evernotechallenge.interviewstreet.com/challenges/dashboard/#problem/500d1c6f1d7ea

	public static void main(String[] args)
	{
		int array[] = { 4, 5, 2, 2, 3 };
		MultiplyExceptSelf(array);
		MultiplyExceptSelf(5);
	}

	public static void MultiplyExceptSelf(int input[])
	{

		int n = input.length;
		int numZeros = 0;
		int product = 1;

		for (int i = 0; i < n; i++)
		{
			if (input[i] == 0)
				numZeros++;
			else
				product *= input[i];
		}

		for (int i = 0; i < n; i++)
		{
			if (input[i] == 0 && numZeros == 1)
				System.out.println(product);
			else if ((numZeros > 1) || (input[i] != 0 && numZeros >= 1))
				System.out.println(0);
			else
				System.out.println(product / input[i]);
		}
	}

	public static void MultiplyExceptSelf(int N)
	{
		int input[] = { 1, 2, 3, 4, 5 };
		int ret[] = { 1, 1, 1, 1, 1 };
		int n = input.length, left = 1, right = 1;

		for (int i = 0; i < n; i++)
		{
			ret[i] = ret[i] * left;
			ret[n - 1 - i] = ret[n - 1 - i] * right;
			left *= input[i];
			right *= input[n - i - 1];
		}

		for (int i = 0; i < n; i++)
			System.out.print(ret[i] + "\t");
	}

}
