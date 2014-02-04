package umbc.practice;

import java.util.ArrayList;

public class Utils
{

    public static void printMarix(int[][] arr)
    {

        // use foreach loop as below to avoid IndexOutOfBoundException
        // need to check matrix != null if implements as a method
        // for each row in the matrix
        for (int[] row : arr)
        {
            // for each number in the row
            for (int j : row)
            {
                System.out.printf("%3d ", j);
            }
            System.out.println("");
        }
        System.out.println();
    }

    public static int[][] createMatrix(int N)
    {
        if (N <= 0)
            return new int[][] {};
        int[][] arr = new int[N][N];
        for (int i = 0; i < N; i++)
            for (int j = 0; j < N; j++)
                arr[i][j] = i * N + j + 1;
        return arr;
    }

    public static int[][] createMatrix(int M, int N)
    {
        if (N <= 0 || M <= 0)
            return new int[][] {};
        int[][] arr = new int[M][N];
        for (int i = 0; i < M; i++)
            for (int j = 0; j < N; j++)
                arr[i][j] = i * N + j + 1;
        return arr;
    }

    public static void printArray(int[] array)
    {
        for (int i = 0; i < array.length; i++)
            System.out.print(array[i] + " ");
        System.out.println();
    }

}
