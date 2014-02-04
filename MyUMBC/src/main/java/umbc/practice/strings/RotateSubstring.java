package umbc.practice.strings;

public class RotateSubstring
{

    public static boolean isRotation(String str1, String str2)
    {
        if (str1.length() != str2.length())
            return false;

        return isSubString(str1 + str1, str2);

    }

    public static void main(String[] args)
    {
        String str1 = "watermelon";
        String str2 = "melonwater";

        System.out.println(isSubString("hello", "hello"));
        System.out.println(isSubString("helle", "hellow"));

        System.out.println(isRotation(str1, str2));
        
        str1 ="watermelno";
        System.out.println(isRotation(str1, str2));
    }

    public static boolean isSubString(String str1, String str2)
    {
        return str1.contains(str2);
    }
}
