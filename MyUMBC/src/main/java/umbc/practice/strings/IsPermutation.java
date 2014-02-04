package umbc.practice.strings;

import java.util.HashMap;

public class IsPermutation
{

    public static boolean isPermutationBySort(String str1, String str2)
    {
        if (str1.length() != str2.length())
            return false;
        char[] str1_chars = str1.toCharArray();
        char[] str2_chars = str2.toCharArray();
        java.util.Arrays.sort(str1_chars);
        java.util.Arrays.sort(str2_chars);

        // System.out.println(str1_chars);
        // System.out.println(str2_chars);

        return java.util.Arrays.equals(str1_chars, str2_chars);

    }

    public static boolean isPermutationByCount(String str1, String str2)
    {
        if (str1.length() != str2.length())
            return false;
        // if ASCII , then use int[] str1_charcount = int[256]
        HashMap<Character, Integer> str1_charcount = new HashMap<Character, Integer>();
        for (int i = 0; i < str1.length(); i++)
        {
            Character ch = str1.charAt(i);
            Integer val = str1_charcount.get(ch);
            str1_charcount.put(ch, (val == null) ? 1 : ++val);
        }

        for (int i = 0; i < str2.length(); i++)
        {
            Character ch = str2.charAt(i);
            Integer val = str1_charcount.get(ch);
            if (val == null || val <= 0)
                return false;
            str1_charcount.put(ch, --val);
        }

        return true;
    }

    public static void main(String[] args)
    {
        String str1 = "";
        String str2 = "";

        testPermutation(str1, str2);

        str1 = "abcdef";
        str2 = "abcdefg";
        testPermutation(str1, str2);

        str1 = "gabcdef";
        str2 = "abcdefg";
        testPermutation(str1, str2);
    }

    public static void testPermutation(String str1, String str2)
    {
        System.out.println(str1);
        System.out.println(str2);

        boolean isPermutation = true;

        // 1st check lengths, obv quick check

        // method 1 - create map of 1st string with Char -> count and iterate
        // over 2nd, checking if char exists in map (if not then no), and
        // decrement count, if count goes -ve then no
        isPermutation = isPermutationByCount(str1, str2);
        System.out.println("IS Permutation Count? " + isPermutation);

        // method 2 - sort both char arrays and check if same
        isPermutation = isPermutationBySort(str1, str2);
        System.out.println("IS Permutation Sort? " + isPermutation);
    }

}
