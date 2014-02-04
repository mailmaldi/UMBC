package umbc.practice.arrays;

import umbc.practice.Utils;

public class ZeroMatrix
{

    public static void main(String[] args)
    {

        int[][] arr = Utils.createMatrix(10, 7);
        arr[3][4] = 0;
        arr[4][3] = 0;
        arr[6][0] = 0;
        arr[0][4] = 0;

        Utils.printMarix(arr);
        zeroMatrix(arr);
        Utils.printMarix(arr);
    }

    public static int[][] zeroMatrixAdditionalArray(int[][] arr)
    {
        if (arr.length <= 0 || arr[0].length <= 0)
            return new int[][] {};

        int M = arr.length;
        int N = arr[0].length;

        boolean[] zeroRows = new boolean[M];
        boolean[] zeroCols = new boolean[N];

        for (int i = 0; i < M; i++)
            for (int j = 0; j < N; j++)
                if (arr[i][j] == 0)
                {
                    zeroRows[i] = true;
                    zeroCols[j] = true;
                }

        for (int i = 0; i < M; i++)
            if (zeroRows[i] == true)
                zeroRow(arr, i);

        for (int j = 0; j < N; j++)
            if (zeroCols[j] == true)
                zeroCol(arr, j);

        return arr;
    }

    public static int[][] zeroMatrix(int[][] arr)
    {
        if (arr.length <= 0 || arr[0].length <= 0)
            return new int[][] {};

        int M = arr.length;
        int N = arr[0].length;

        boolean zeroFirstRow = false;
        boolean zeroFirstCol = false;

        for (int i = 0; i < M; i++)
            if (arr[i][0] == 0)
                zeroFirstCol = true;

        for (int j = 0; j < N; j++)
            if (arr[0][j] == 0)
                zeroFirstRow = true;

        for (int i = 1; i < M; i++)
            for (int j = 1; j < N; j++)
                if (arr[i][j] == 0)
                {
                    arr[i][0] = 0;
                    arr[0][j] = 0;
                }

        for (int i = 1; i < M; i++)
            if (arr[i][0] == 0)
                zeroRow(arr, i);

        for (int j = 1; j < N; j++)
            if (arr[0][j] == 0)
                zeroCol(arr, j);

        if (zeroFirstRow)
            zeroRow(arr, 0);

        if (zeroFirstCol)
            zeroCol(arr, 0);

        return arr;
    }

    public static void zeroRow(int[][] arr, int row)
    {
        for (int j = 0; j < arr[0].length; j++)
            arr[row][j] = 0;
    }

    public static void zeroCol(int[][] arr, int col)
    {
        for (int i = 0; i < arr.length; i++)
            arr[i][col] = 0;
    }
}
