package umbc.practice.recursion;

public class PermuteNumbersOfLength
{
    public static void main(String[] args)
    {
        int N = 4;
        // permuteNumbers(N);

        int[] base =
        { 0, 1, 2, 3, 4, 5, 6, 7, 8, 9 };
        permutation(N, N, base);
    }

    public static void permuteNumbers(int n)
    {
        String digits = "0123456789";

        permuteNumbers("", n, digits);
    }

    private static void permuteNumbers(String build, int n, String digits)
    {
        if (build.length() == n)
            System.out.println(build);

        for (int i = 0; i < digits.length(); i++)
            permuteNumbers(build + digits.charAt(i), n, digits.substring(0, i)
                    + digits.substring(i + 1));
    }

    public static void permutation(int N, int length, int[] perm)
    {
        if (length == 0)
        {
            for (int i = 0; i < N; i++)
            {
                System.out.print(perm[i]);
            }
            System.out.println();
        }
        else
        {
            for (int count = N - length; count < perm.length; count++)
            {
                int temp = perm[0];
                perm[0] = perm[count];
                perm[count] = temp;
                permutation(N, length - 1, perm);
            }
        }
    }
}
