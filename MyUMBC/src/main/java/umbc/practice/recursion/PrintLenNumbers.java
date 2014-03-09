package umbc.practice.recursion;

public class PrintLenNumbers
{
    public static void main(String[] args)
    {
        printNumbers(2);
    }

    public static void printNumbers(int n)
    {
        int[] a =
        { 1, 2, 3, 4, 5, 6, 7, 8, 9 };

        subPrint(0, 0, n, a);

    }

    private static void subPrint(int preFix, int startIndex, int numberCount,
            int[] a)
    {
        if (numberCount == 0)
        {
            System.out.println(preFix);
        }

        for (int i = startIndex; i < a.length; i++)
        {
            int digit = a[i];
            subPrint(preFix * 10 + digit, digit, numberCount - 1, a);
        }
    }
}
