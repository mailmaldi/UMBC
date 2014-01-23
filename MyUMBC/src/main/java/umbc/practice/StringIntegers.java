package main.java.umbc.practice;

public class StringIntegers
{

	public static int getSum(String s)
	{
		int sum = 0;
		char[] chars = s.toCharArray();
		char ch;

		for (int i = 0; i < chars.length; i++)
		{
			ch = chars[i];
			int temp = 0;
			if (Character.isDigit(ch))
			{
				temp = temp * 10 + Character.getNumericValue(ch);

				while (true && i < chars.length - 1)
				{
					ch = chars[++i];
					if (Character.isDigit(ch))
					{
						temp = temp * 10 + Character.getNumericValue(ch);
					}
					else
						break;
				}
				sum += temp;
			}
		}
		return sum;
	}

	public static void main(String[] args)
	{
		// System.out.println(Character.getNumericValue('0'));
		String s = "a1a23b some4thing 7 and 20";
		System.out.println(getSum(s));
	}

}
