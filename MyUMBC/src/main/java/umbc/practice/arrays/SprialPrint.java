package umbc.practice.arrays;

public class SprialPrint
{

    public static void printSprial(char[][] array)
    {
        if (array.length == 0 || array[0].length != array.length)
            return;

        int spirals = (int) Math.ceil((double) array.length / 2);

        for (int i = 0; i < spirals - 1; i++)
        {
            int elements_in_spiral = (array.length - 1 - 2 * i);
            for (int j = i; j < elements_in_spiral + i; j++)
                System.out.print(array[i][j] + " ");

            for (int j = i; j < elements_in_spiral + i; j++)
                System.out.print(array[j][(array.length - 1 - i)] + " ");

            for (int j = i; j < elements_in_spiral + i; j++)
                System.out
                        .print(array[(array.length - 1 - i)][(array.length - 1 - j)]
                                + " ");

            for (int j = i; j < elements_in_spiral + i; j++)
                System.out.print(array[(array.length - 1 - j)][i] + " ");
        }

        if (spirals % 2 == 1)
            System.out.print(array[spirals - 1][spirals - 1] + " ");

        System.out.println();
    }

    public static void main(String[] args)
    {
        char[][] matrix = new char[][]
        {
        { 'i', 'l', 'o', 'v', 'e' },
        { 'd', 'i', 'n', 't', 'e' },
        { 'n', 'e', 'w', 'e', 'p' },
        { 'a', 'i', 'v', 'r', 'i' },
        { 'm', 'a', 'x', 'e', 'c' } };

        printSprial(matrix);

        matrix = new char[][]
        {
        { 'i', 'l', 'o', 'v' },
        { 'm', 'i', 'n', 'e' },
        { 'a', 'e', 't', 'p' },
        { 'x', 'e', 'c', 'i' } };

        printSprial(matrix);
    }
}
