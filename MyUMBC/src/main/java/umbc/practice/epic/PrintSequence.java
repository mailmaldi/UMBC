package umbc.practice.epic;

/*
 * print the sequences from the input given by the user separated by semicolon 
 eg: 4678912356012356 

 output: 4;6789;123;56;0123;56;
 */
public class PrintSequence
{

    public static void main(String[] args)
    {
        System.out.println(printSequence("46789123560123569"));
    }

    private static String printSequence(String input)
    {
        if (input == null || input.length() == 0)
            return null;

        StringBuffer sb = new StringBuffer();
        int previous = input.charAt(0) - '0';
        sb.append(previous);
        for (int i = 1; i < input.length(); i++)
        {
            int val = input.charAt(i) - '0';
            if (val == previous + 1)
                sb.append(val);
            else
                sb.append(';').append(val);
            previous = val;
        }
        return sb.toString();
    }
}
