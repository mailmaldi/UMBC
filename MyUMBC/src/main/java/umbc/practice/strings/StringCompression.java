package umbc.practice.strings;

public class StringCompression
{
    public static String compress(String string)
    {
        if (string == null || string.isEmpty())
            return string;

        int compressed_length = getCompressedLength(string);
        if (string.length() <= compressed_length)
        {
            return string;
        }

        StringBuilder sb = new StringBuilder();

        // Iterate thru string
        // keep tab of current character and its sequence length
        // if current char changes, then reset seq length as 1 , and
        // append to sb curr char and old seq length

        char current_char = string.charAt(0);
        int current_seq = 1;
        for (int i = 1; i < string.length(); i++)
        {
            char ch = string.charAt(i);
            if (ch == current_char)
            {
                current_seq++;
            }
            else
            {
                sb.append(current_char).append(current_seq);
                current_seq = 1;
                current_char = ch;
            }
        }
        sb.append(current_char).append(current_seq);

        return sb.toString();
    }

    public static String compressWithoutBuilder(String string)
    {
        if (string == null || string.isEmpty())
            return string;

        int compressed_length = getCompressedLength(string);
        if (string.length() <= compressed_length)
        {
            return string;
        }

        char[] array = new char[compressed_length];

        char current_char = string.charAt(0);
        int current_seq = 1;
        int j = 0;
        for (int i = 1; i < string.length(); i++)
        {
            char ch = string.charAt(i);
            if (ch == current_char)
            {
                current_seq++;
            }
            else
            {
                array[j++] = current_char;
                String temp = String.valueOf(current_seq);
                for (int k = 0; k < temp.length(); k++)
                {
                    array[j++] = temp.charAt(k);
                }

                current_seq = 1;
                current_char = ch;
            }
        }
        array[j++] = current_char;
        String temp = String.valueOf(current_seq);
        for (int k = 0; k < temp.length(); k++)
        {
            array[j++] = temp.charAt(k);
        }

        return new String(array);

    }

    public static int getCompressedLength(String string)
    {
        if (string == null || string.isEmpty())
            return 0;
        char current_char = string.charAt(0);
        int current_seq = 1;
        int compressed_length = 0;
        for (int i = 1; i < string.length(); i++)
        {
            char ch = string.charAt(i);
            if (ch == current_char)
            {
                current_seq++;
            }
            else
            {
                compressed_length += 1 + String.valueOf(current_seq).length();
                current_seq = 1;
                current_char = ch;
            }
        }
        compressed_length += 1 + String.valueOf(current_seq).length();

        return compressed_length;
    }

    public static void main(String[] args)
    {
        testCompress("");

        testCompress(" ");

        testCompress("a");

        testCompress("abcd");

        testCompress("abbbcd");

        testCompress("abbbbbbbcd");
        
        testCompress("abbbbbbbcdefggggggggggggggggggggggggghhhhhhhhh");

    }

    public static void testCompress(String input)
    {
        System.out.println("input  = '" + input + "'");
        System.out.println("output = '" + compress(input) + "'");
        System.out.println("output = '" + compressWithoutBuilder(input) + "'");
    }

}
