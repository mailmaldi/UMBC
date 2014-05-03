package umbc.practice;

import java.math.BigInteger;
import java.util.HashMap;

public class RandomTest
{

    public static void main(String[] args)
    {
        System.out.println(Integer.MAX_VALUE);
        // for (int i = 0; i < 50; i++)
        // System.out.println(i + "=" + factorial(i));

        System.out.println(isPowerOfTwo(4));

        System.out.println(checkAnagrams("A man’s rag", "anagrams"));

        runningAverage();

    }

    public static int factorial(int num)
    {
        if (num < 0)
            return -1;
        if (num == 0 || num == 1)
            return 1;
        return num * factorial(num - 1);
    }

    public static boolean isPowerOfTwo(int x)
    {
        return ((x > 0) && ((x & (x - 1)) == 0));
    }

    public static boolean isPowerOfTwoDivide(int x)
    {
        while ((x > 1) && ((x % 2) == 0))
        {
            x /= 2;
        }
        return (x == 1);
    }

    public static int sumIntegers(int a, int b)
    {
        long sum = (long) a + (long) b;
        if (sum != (int) sum)
            throw new ArithmeticException("Boundary overflow exception");
        return (int) sum;
    }

    public static int sumIntegers(int... args)
    {
        long sum = 0;
        for (int i : args)
        {
            sum += i;
        }
        if (sum < Integer.MIN_VALUE || sum > Integer.MAX_VALUE)
            throw new ArithmeticException("Boundary overflow exception");
        return (int) sum;
    }

    public static boolean checkAnagrams(String str1, String str2)
    {
        if (str1 == null || str2 == null)
            return false;
        HashMap<Character, Integer> str1hash = new HashMap<Character, Integer>();

        int str1Length = 0;
        for (int i = 0; i < str1.length(); i++)
        {
            Character ch = str1.charAt(i);
            // ignore whitespace,punctuations
            if (Character.isLetter(ch))
            {
                ch = Character.toLowerCase(ch);
                str1hash.put(ch,
                        (str1hash.containsKey(ch)) ? str1hash.get(ch) + 1 : 1);
                str1Length++;
            }
        }

        for (int i = 0; i < str2.length(); i++)
        {
            Character ch = str2.charAt(i);
            if (Character.isLetter(ch))
            {
                ch = Character.toLowerCase(ch);

                if (!str1hash.containsKey(ch) || str1hash.get(ch) <= 0
                        || str1Length <= 0)
                    return false;

                str1hash.put(ch, str1hash.get(ch) - 1);
                str1Length--;
            }
        }
        // one final check , since what if str1 was longer?
        // this way we dont have to iterate over the str1hash again
        if (str1Length != 0)
            return false;

        return true;
    }

    public static void runningAverage()
    {
        BigInteger bint = BigInteger.valueOf(Long.MAX_VALUE);
        System.out.println(Long.MAX_VALUE);
        bint = bint.add(bint);
        System.out.println(bint);
    }
}
