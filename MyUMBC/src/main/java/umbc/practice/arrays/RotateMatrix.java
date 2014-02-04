package umbc.practice.arrays;

import umbc.practice.Utils;

public class RotateMatrix
{
    // Given a N x N matrix, rotate it 90 degrees to the right or left, or 180
    // degrees
    // do it in place

    public static int[][] rotate90Left(int[][] arr)
    {
        if (arr.length == 0 || arr.length != arr[0].length)
            throw new RuntimeException("Its not a square matrix");

        int N = arr.length;

        // number of rings is floor ( N /2 )
        for (int i = 0; i < N / 2; i++)
        {
            // to rotate, we must rotate the N-1 values of each ring
            // we start j from i since start of the ring moves diagonally
            // inside,
            // also end of j moves 1 step inside for each i
            for (int j = i; j < (N - 1) - i; j++)
            {
                int temp = arr[i][j];
                arr[i][j] = arr[j][(N - 1) - i];
                arr[j][(N - 1) - i] = arr[(N - 1) - i][(N - 1) - j];
                arr[(N - 1) - i][(N - 1) - j] = arr[(N - 1) - j][i];
                arr[(N - 1) - j][i] = temp;
            }
        }

        return arr;
    }

    public static int[][] rotate90Right(int[][] arr)
    {
        if (arr.length == 0 || arr.length != arr[0].length)
            throw new RuntimeException("Its not a square matrix");

        int N = arr.length;

        // number of rings is floor ( N /2 )
        for (int i = 0; i < N / 2; i++)
        {
            // to rotate, we must rotate the N-1 values of each ring
            // we start j from i since start of the ring moves diagonally
            // inside,
            // also end of j moves 1 step inside for each i
            for (int j = i; j < (N - 1) - i; j++)
            {
                int temp = arr[(N - 1) - j][i];
                arr[(N - 1) - j][i] = arr[(N - 1) - i][(N - 1) - j];
                arr[(N - 1) - i][(N - 1) - j] = arr[j][(N - 1) - i];
                arr[j][(N - 1) - i] = arr[i][j];
                arr[i][j] = temp;
            }
        }

        return arr;
    }

    public static int[][] rotate180(int[][] arr)
    {
        if (arr.length == 0 || arr[0].length == 0)
            throw new RuntimeException("Its an empty matrix");

        int M = arr.length;
        int N = arr[0].length;

        if (M % 2 != 0)
        {
            for (int i = 0; i < N / 2; i++)
            {
                int temp = arr[M / 2][i];
                arr[M / 2][i] = arr[M / 2][(N - 1) - i];
                arr[M / 2][(N - 1) - i] = temp;
            }
        }

        for (int i = 0; i < M / 2; i++)
        {
            for (int j = 0; j < N; j++)
            {
                int temp = arr[i][j];
                arr[i][j] = arr[(M - 1) - i][(N - 1) - j];
                arr[(M - 1) - i][(N - 1) - j] = temp;
            }
        }

        return arr;

    }

    public static int[][] rotateCopy(int[][] arr)
    {
        if (arr.length == 0 || arr.length != arr[0].length)
            throw new RuntimeException("Its not a square matrix");

        int N = arr.length;

        int[][] returnArr = new int[N][N];

        for (int i = 0; i < N; i++)
        {
            for (int j = 0; j < N; j++)
            {
                returnArr[j][(N - 1) - i] = arr[i][j];
            }
        }

        return returnArr;
    }

    public static void main(String[] args)
    {
        // testMatrix(0);
        testMatrix(1);
        testMatrix(2);
        testMatrix(3);
        testMatrix(4);
        testMatrix(5);
        testMatrix(6);
        testMatrix(7);

    }

    public static void testMatrix(int N)
    {
        int[][] arr = Utils.createMatrix(N);

        System.out.println("INPUT:");
        Utils.printMarix(arr);

        rotate90Left(arr);
        System.out.println("OUTPUT LEFT:");
        Utils.printMarix(arr);

        rotate90Right(arr);
        System.out.println("OUTPUT RIGHT:");
        Utils.printMarix(arr);

        rotate180(arr);
        System.out.println("OUTPUT ROTATE:");
        Utils.printMarix(arr);

        System.out.println("OUTPUT COPY RIGHT:");
        Utils.printMarix(rotateCopy(arr));

    }
}
