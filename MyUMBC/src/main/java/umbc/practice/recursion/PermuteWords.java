package umbc.practice.recursion;

import java.util.ArrayList;
import java.util.List;

public class PermuteWords
{
    public static void main(String[] args)
    {
        List<String> list = new ArrayList<String>();
        list.add("This");
        list.add("is");
        list.add("String");

        List<List<String>> lists = permuteWords(list);
        System.out.println(lists.size());
        for (List<String> temp : lists)
        {
            for (String string : temp)
            {
                System.out.print(string + " ");
            }
            System.out.println();
        }

    }

    public static List<List<String>> permuteWords(List<String> list)
    {
        List<List<String>> lists = new ArrayList<List<String>>();
        if (list.size() <= 1)
        {
            List<String> temp = new ArrayList<String>();
            temp.addAll(list);
            lists.add(temp);
            return lists;
        }

        String first = list.get(0);
        List<String> rest = new ArrayList<String>();
        rest.addAll(list.subList(1, list.size()));

        List<List<String>> permutations = permuteWords(rest);
        for (List<String> permutation : permutations)
        {
            interleave(lists, first, permutation);
        }

        return lists;
    }

    private static void interleave(List<List<String>> lists, String first,
            List<String> permutation)
    {
        for (int i = 0; i <= permutation.size(); i++)
        {
            List<String> temp = new ArrayList<String>();
            temp.addAll(permutation);
            temp.add(i, first);
            lists.add(temp);
        }
    }
}
