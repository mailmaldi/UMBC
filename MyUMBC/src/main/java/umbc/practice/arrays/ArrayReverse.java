package umbc.practice.arrays;

import umbc.practice.Utils;

public class ArrayReverse
{

    public static void reverseArray(int[] array, int i, int j)
    {
        if (i < 0 || (i > array.length - 1) || j < 0 || (j > array.length - 1))
            return;

        if (i > j)
        {
            int k = i;
            i = j;
            j = k;
        }

        int mid = (j - i + 1) / 2;
        for (int k = 0; k < mid; k++)
        {
            int temp = array[i + k];
            array[i + k] = array[j - k];
            array[j - k] = temp;
        }

    }

    public static void main(String[] args)
    {
        int[] array =
        { 1, 2, 3, 4, 5, 6, 7, 8, 9 };

        Utils.printArray(array);

        reverseArray(array, 8, 9);

        Utils.printArray(array);

    }

}
