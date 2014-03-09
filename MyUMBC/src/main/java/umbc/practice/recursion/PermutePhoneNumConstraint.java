package umbc.practice.recursion;

import java.util.Scanner;

/*
 * Print all valid phone numbers of length n subject to following constraints: 

 1.If a number contains a 4, it should start with 4 
 2.No two consecutive digits can be same 
 3.Three digits (e.g. 7,2,9) will be entirely disallowed, take as input

 http://www.careercup.com/question?id=15190680
 */
public class PermutePhoneNumConstraint
{
    public static void main(String[] args)
    {
        int[] disallowed = new int[]
        { 2, 7, 9 };
        int[] allowed = new int[]
        { 0, 1, 3, 4, 5, 6, 8 };

        Scanner scan = new Scanner(System.in);
        System.out.println("Enter num digits: ");
        int N = scan.nextInt();
        N = (int) Math.abs(N);
        scan.close();
        // System.out.println(N);

        // printPhoneNumbers(0, N, N, allowed, 0);
        printPhoneNumbersString("", N, allowed);

    }

    private static void printPhoneNumbers(int prefix, int remainingDigits,
            final int digits, int[] allowed, int msb)
    {
        if (remainingDigits == 0)
        {
            System.out.println(prefix);
            return;
        }

        for (int i = 0; i < allowed.length; i++)
        {
            int digit = allowed[i];

            if (remainingDigits == digits)
                msb = digit;

            // dont allow msb to be 0?

            if (msb != 0
                    && !((remainingDigits < digits) && (digit == prefix % 10))
                    && !((digit == 4) && (prefix % 10 == 4)))

                printPhoneNumbers(prefix * 10 + digit, remainingDigits - 1,
                        digits, allowed, msb);
        }
    }

    private static void printPhoneNumbersString(String builder,
            final int length, int[] allowed)
    {
        if (builder.length() == length)
        {
            System.out.println(builder);
            return;
        }

        for (int i = 0; i < allowed.length; i++)
        {
            int digit = allowed[i];
            if (builder.length() > 0)
            {
                if (digit == 4 && builder.charAt(0) != '4')
                    continue;
                if (digit == builder.charAt(builder.length() - 1) - '0')
                    continue;
            }
            printPhoneNumbersString(builder + digit, length, allowed);
        }

    }

}