package umbc.practice.arrays;

import umbc.practice.Utils;

public class ArrayRotate
{

    public static void rorateRightArray(int[] array, int k)
    {
        k = k % array.length;

        // to rotate k right,
        // reverse last k elements
        // reverse first len -k elements
        // reverse all elements

        ArrayReverse.reverseArray(array, (array.length - 1) - (k - 1),
                array.length - 1);

        ArrayReverse.reverseArray(array, 0, (array.length - 1) - k);

        ArrayReverse.reverseArray(array, 0, array.length - 1);
    }

    public static void main(String[] args)
    {
        int[] array =
        { 1, 2, 3, 4, 5, 6, 7, 8, 9 };

        Utils.printArray(array);

        rorateRightArray(array, 1);

        Utils.printArray(array);
    }
}
