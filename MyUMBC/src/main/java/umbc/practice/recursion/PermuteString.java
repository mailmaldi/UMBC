package umbc.practice.recursion;

import java.util.ArrayList;
import java.util.List;

public class PermuteString
{
    public static void main(String[] args)
    {
        String str = "abc";
        permute(str);
    }

    private static void permute(String str)
    {
        List<String> permuteList = permuteHelper(str);
        System.out.println(permuteList.size());
        for (String string : permuteList)
        {
            System.out.println(string);
        }
    }

    private static List<String> permuteHelper(String str)
    {
        List<String> list = new ArrayList<String>();
        if (str.length() <= 1)
        {
            list.add(str);
            return list;
        }

        char first = str.charAt(0);
        String rest = str.substring(1);

        List<String> permutations = permuteHelper(rest);
        for (String string : permutations)
            interleave(list, first, string);

        return list;
    }

    private static void interleave(List<String> list, char ch, String string)
    {
        for (int i = 0; i <= string.length(); i++)
        {
            list.add(string.substring(0, i) + ch + string.substring(i));
        }

    }
}
