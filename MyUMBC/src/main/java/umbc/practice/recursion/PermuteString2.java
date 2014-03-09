package umbc.practice.recursion;

import java.util.ArrayList;
import java.util.List;

public class PermuteString2
{
    public static void main(String[] args)
    {
        List<String> list = new ArrayList<String>();
        String string = new String("abcd");
        permuteString2("", string, list);
        for (String temp : list)
        {
            System.out.println(temp);
        }
    }

    private static void permuteString2(String builder, String remaining)
    {
        if (remaining.length() == 0)
        {
            System.out.println(builder);
            return;
        }

        for (int i = 0; i < remaining.length(); i++)
        {
            permuteString2(builder + remaining.charAt(i),
                    remaining.substring(0, i) + remaining.substring(i + 1));
        }
    }

    private static void permuteString2(String builder, String remaining,
            List<String> list)
    {

        if (remaining.length() == 0)
        {
            list.add(builder);
            return;
        }

        for (int i = 0; i < remaining.length(); i++)
        {
            permuteString2(builder + remaining.charAt(i),
                    remaining.substring(0, i) + remaining.substring(i + 1),
                    list);
        }
    }

}
