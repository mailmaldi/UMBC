package umbc.practice;

import java.util.HashMap;

public class StringProblems
{
	public static Character firstNonRepeated(String str)
	{
		HashMap<Character, Object> charHash = new HashMap<Character, Object>();
		int i, length;
		Character c;
		Object seenOnce = new Object();
		Object seenTwice = new Object();
		length = str.length();
		// Scan str, building hash table
		for (i = 0; i < length; i++)
		{
			c = new Character(str.charAt(i));
			Object o = charHash.get(c);
			if (o == null)
			{
				charHash.put(c, seenOnce);
			}
			else if (o == seenOnce)
			{
				// Increment count corresponding to c
				charHash.put(c, seenTwice);
			}
		}
		// Search hashtable in order of str
		for (i = 0; i < length; i++)
		{
			c = new Character(str.charAt(i));
			if (charHash.get(c) == seenOnce)
			{
				return c;
			}
		}
		return null;
	}

	//Stupid thing, string is immutable in java
	public static String reverse(String str)
	{
		int length = str.length();
		int mid = length / 2;
		char[] chars = str.toCharArray();

		char temp;
		for (int i = 0; i < mid; i++)
		{
			temp = chars[i];
			chars[i] = chars[length - 1 - i];
			chars[length - 1 - i] = temp;
		}

		return new String(chars);
	}

	public static void main(String[] args)
	{
		//System.out.println(reverse("abcd"));
	}
}
