package umbc.practice.strings;

import java.util.HashSet;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class UniqueChars
{
    private static final Logger logger = LoggerFactory
                                               .getLogger(UniqueChars.class);

    public static boolean hasUniqueCharsSet(String string)
    {
        boolean hasUniqueChars = true;

        HashSet<Character> set = new HashSet<Character>();

        for (int i = 0; i < string.length(); i++)
        {
            if (set.contains(string.charAt(i)))
            {
                hasUniqueChars = false;
                break;
            }
            set.add(string.charAt(i));
        }

        logger.info("String '{}' HASHSET UNIQUE? {} ", string, hasUniqueChars);
        return hasUniqueChars;

    }

    public static boolean hasUniqueCharsArray(String string)
    {
        boolean hasUniqueChars = true;

        boolean charsArray[] = new boolean[128];

        for (int i = 0; i < string.length(); i++)
        {
            if (charsArray[string.charAt(i)] == true)
            {
                hasUniqueChars = false;
                break;
            }
            charsArray[string.charAt(i)] = true;
        }

        logger.info("String '{}' BOOLARRAY UNIQUE? {} ", string, hasUniqueChars);
        return hasUniqueChars;

    }

    public static void hasUniqueChars(String string)
    {
        hasUniqueCharsSet(string);
        hasUniqueCharsArray(string);
    }

    public static void main(String[] args)
    {
        String string = "";
        hasUniqueChars(string);

        string = "   ";
        hasUniqueChars(string);

        string = "abcdef ah";
        hasUniqueChars(string);

        string = "abcdef ah";
        hasUniqueChars(string);

    }

}
