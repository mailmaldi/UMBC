package umbc.practice.arrays;

import java.util.concurrent.atomic.AtomicInteger;

import umbc.practice.MutableInt;

/*
 * Find all paths from 0,0 to N-1,N-1 (optionally M-1, N-1), if only right & down are allowed
 */
public class MatrixAllPaths
{
    public static void main(String[] args)
    {
        int[][] arr =
        {
        { 1, 2, 3 },
        { 4, 5, 6 },
        { 7, 8, 9 } };
        numPaths(arr);
        // printPaths(arr);
        maxOfMinimums(arr);

        int[][] arr2 =
        {
        { 1, 2 },
        { 3, 4 } };
        numPaths(arr2);
        // printPaths(arr2);
        maxOfMinimums(arr2);
    }

    public static void numPaths(int[][] arr)
    {
        AtomicInteger temp = new AtomicInteger();
        MutableInt tot = new MutableInt();
        numPaths(arr, 0, 0, tot, temp);
        System.out.println("total paths: " + tot.value + " " + temp.get());
    }

    private static void numPaths(int[][] arr, int i, int j, MutableInt tot,
            AtomicInteger temp)
    {
        int M = arr.length;
        int N = arr[0].length;

        if (i == M - 1 && j == N - 1)
        {
            tot.value++;
            temp.incrementAndGet();
        }

        if (validMove(i + 1, j, M, N))
            numPaths(arr, i + 1, j, tot, temp);
        if (validMove(i, j + 1, M, N))
            numPaths(arr, i, j + 1, tot, temp);
    }

    private static boolean validMove(int i, int j, int M, int N)
    {
        return (i >= 0 && i <= M - 1 && j >= 0 && j <= N - 1) ? true : false;
    }

    public static void printPaths(int[][] arr)
    {
        printPaths(arr, 0, 0, "");
    }

    private static void printPaths(int[][] arr, int i, int j, String path)
    {
        int M = arr.length;
        int N = arr[0].length;

        if (i == M - 1 && j == N - 1)
            System.out.println(path + " " + arr[M - 1][N - 1]);

        if (validMove(i + 1, j, M, N))
            printPaths(arr, i + 1, j, path + " " + arr[i][j]);
        if (validMove(i, j + 1, M, N))
            printPaths(arr, i, j + 1, path + " " + arr[i][j]);

    }

    public static void maxOfMinimums(int[][] arr)
    {
        MutableInt max = new MutableInt(Integer.MIN_VALUE);
        maxOfMinimums(arr, 0, 0, max, Integer.MAX_VALUE);
        System.out.println("Max of minimums: " + max.value);
    }

    private static void maxOfMinimums(int[][] arr, int i, int j,
            MutableInt maxOfMins, int minOfPath)
    {
        int M = arr.length;
        int N = arr[0].length;

        minOfPath = (minOfPath < arr[i][j]) ? minOfPath : arr[i][j];

        if (i == M - 1 && j == N - 1)
            maxOfMins.value = (minOfPath > maxOfMins.value) ? minOfPath
                    : maxOfMins.value;

        if (validMove(i + 1, j, M, N))
            maxOfMinimums(arr, i + 1, j, maxOfMins, minOfPath);
        if (validMove(i, j + 1, M, N))
            maxOfMinimums(arr, i, j + 1, maxOfMins, minOfPath);
    }
}
