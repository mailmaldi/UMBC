package umbc.practice.arrays;

public class TransposeStrings
{
    public static void main(String[] args)
    {
        char[] s1 = "GUMM".toCharArray();
        char[] s2 = "MMUG".toCharArray();

        transpose(s1, s2);
    }

    public static void transpose(char[] s1, char[] s2)
    {
        printCharArray(s1);
        printCharArray(s2);
        System.out.println();
        // s2 is target array, s1 is the one being modified

        for (int i = 0; i < s1.length; i++)
        {
            if (s1[i] == s2[i])
            {
                continue;
            }
            int j = i + 1;
            for (j = i + 1; j < s1.length; j++)
            {
                // search in s1 , the position of current char in s2 and then
                // bring that char back swapping in s1
                if (s1[j] == s2[i])
                    break;
            }
            for (int k = j; k > i; k--)
            {
                char temp = s1[k];
                s1[k] = s1[k - 1];
                s1[k - 1] = temp;
                printCharArray(s1);
            }
        }

    }

    public static void printCharArray(char[] array)
    {
        for (int i = 0; i < array.length; i++)
            System.out.print(array[i]);
        System.out.println();
    }
}
