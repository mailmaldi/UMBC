package Utils;

public class Utils
{

    public static boolean isPowerOf2(int val)
    {
        return (val > 0) && (val & (val - 1)) == 0;
    }

    public static void main(String[] args)
    {
        int val = 256;
        int block = 4;

        for (int i = 0; i < 10; i++)
        {
            int temp = val - (val % block);
            System.out.println(val + " " + temp);
            val++;
        }
    }

}
