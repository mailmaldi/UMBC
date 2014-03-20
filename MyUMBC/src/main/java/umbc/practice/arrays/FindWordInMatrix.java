package umbc.practice.arrays;

public class FindWordInMatrix
{
    public static void main(String[] args)
    {
        int[][] arr =
        {
        { 'a', 'b', 'c', 'd', 'e' },
        { 'f', 'g', 'h', 'i', 'j' },
        { 'a', 'b', 'c', 'd', 'e' },
        { 'f', 'g', 'h', 'i', 'j' } };
        String word1 = "abc", word2 = "gci", word3 = "je", word4 = "gcia";

        findword(arr, word1);
        findword(arr, word2);
        findword(arr, word3);
        findword(arr, word4);

    }

    public static void findword(int[][] arr, String word)
    {
        int M = arr.length;
        int N = arr[0].length;

        for (int i = 0; i < M; i++)
            for (int j = 0; j < N; j++)
                findword(arr, M, N, word, i, j);
    }

    private static void findword(int[][] arr, int M, int N, String word, int i,
            int j)
    {
        for (int di = -1; di <= 1; di++)
            for (int dj = -1; dj <= 1; dj++)
            {
                if (di == 0 && dj == 0)
                    continue;
                findword(arr, M, N, word, i, j, di, dj);
            }

    }

    private static void findword(int[][] arr, int M, int N, String word, int i,
            int j, int di, int dj)
    {
        int origx = i;
        int origy = j;
        for (int l = 0; l < word.length(); l++)
        {
            if (!valid(M, N, i, j) || (word.charAt(l) != arr[i][j]))
                return;
            i += di;
            j += dj;
        }
        // found so print
        System.out.println("Found word \"" + word + "\" starting at (" + origx
                + "," + origy + ") in direction (" + di + "," + dj + ")");
    }

    private static boolean valid(int M, int N, int i, int j)
    {
        return (i >= 0 && i < M && j >= 0 && j < N) ? true : false;
    }

}
