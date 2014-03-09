package umbc.practice.zillow;

import static org.junit.Assert.*;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

public class StringToLong
{

    /**
     * Initially I only checked for null, empty & illegal chars in the string,
     * then I remembered Long has a max & a min value, unlike Erlang, so we need
     * to check that as well. In absolute math, min is max + 1.
     * 
     * @param s
     *            string
     * @return long value of the string
     */
    public static long stringToLong(String s)
    {
        // check for null or 0 length
        if (s == null)
            throw new NumberFormatException("null string");
        if (s.length() == 0)
            throw new NumberFormatException("empty string");

        int length = s.length(); // length of string

        // valid long is either +ve or -ve
        // if we ever come across a non digit except the first char which can be
        // + or -, throw exception
        boolean negative = false; // to check if number is +ve or -ve
        long MAX = -Long.MAX_VALUE; // had to do all calculations in negative
                                    // because Abs(Min) > Abs(Max), issues at
                                    // boundaries.
        long result = 0; // to hold the parsed long

        int i = 0; // I initially used for loop and did the check of if(i==0)
                   // inside the loop, but that's 1 extra comparison every loop
                   // iteration, needless
        char first = s.charAt(0);
        if (!Character.isDigit(first))
        {
            if (first != '+' && first != '-')
                throw new NumberFormatException("illegal char");
            if (first == '-')
            {
                negative = true;
                MAX = Long.MIN_VALUE;
            }
            i++;
        }

        long MAXBY10 = MAX / 10;
        // if limit -32767, -32768, this becomes -3276 & -3276
        // if result is greater than MAXBY10, then the next digit will exceed
        // boundary anyway , eg 32770 and so forth
        // the only additional check we need to put in now is when numbers are
        // in the 3276x range
        for (; i < length; i++)
        {
            char ch = s.charAt(i);
            if (!Character.isDigit(ch))
                throw new NumberFormatException("illegal char");
            int digit = Character.digit(ch, 10);

            // we check boundary exceeding at the macro level
            if (result < MAXBY10)
                throw new NumberFormatException(
                        "Exceed boundary before adding digit");

            // use ch-'0' if we're sure about encoding
            result = result * 10; // at this point we need to check if the next
                                  // digit will make it overrun boundary
            // example +ve number and 32768 has current MAX = -32767 and result
            // here is -32760
            // cant do result - digit & check since that can overrun boundary ,
            // so we do
            if (MAX + digit > result)
                throw new NumberFormatException(
                        "Exceed boundary at adding digit");

            result = result - digit;
        }

        return (negative) ? result : -result;
    }

    public static void main(String[] args)
    {
        System.out.println(Long.MAX_VALUE + " " + Long.MIN_VALUE);
        System.out.println(stringToLong("9223372036854775807"));
        System.out.println(stringToLong("-9223372036854775808"));
    }

    @Rule
    public ExpectedException exception = ExpectedException.none();

    @Test
    public final void test1()
    {
        assertEquals(9223372036854775807L, stringToLong("9223372036854775807"));
    }

    @Test
    public final void test2()
    {
        assertEquals(-9223372036854775808L,
                stringToLong("-9223372036854775808"));
    }

    @Test
    public final void test3()
    {
        exception.expect(NumberFormatException.class);
        exception.expectMessage("null string");
        stringToLong(null);
    }

    @Test
    public final void test4()
    {
        exception.expect(NumberFormatException.class);
        exception.expectMessage("empty string");
        stringToLong("");
    }

    @Test
    public final void test5()
    {
        exception.expect(NumberFormatException.class);
        exception.expectMessage("Exceed boundary at adding digit");
        stringToLong("9223372036854775808");
    }

    @Test
    public final void test6()
    {
        exception.expect(NumberFormatException.class);
        exception.expectMessage("Exceed boundary at adding digit");
        stringToLong("-9223372036854775809");
    }

    @Test
    public final void test7()
    {
        exception.expect(NumberFormatException.class);
        exception.expectMessage("Exceed boundary at adding digit");
        stringToLong("92233720368547758081");
    }

    @Test
    public final void test8()
    {
        exception.expect(NumberFormatException.class);
        exception.expectMessage("Exceed boundary at adding digit");
        stringToLong("-92233720368547758091");
    }

    @Test
    public final void test9()
    {
        exception.expect(NumberFormatException.class);
        exception.expectMessage("Exceed boundary before adding digit");
        stringToLong("9223372036854775817");
    }

    @Test
    public final void test10()
    {
        exception.expect(NumberFormatException.class);
        exception.expectMessage("Exceed boundary before adding digit");
        stringToLong("9923372036854775807"); // 99 instead of 92
    }

    @Test
    public final void test11()
    {
        exception.expect(NumberFormatException.class);
        exception.expectMessage("illegal char");
        stringToLong("++1234");
    }

    @Test
    public final void test12()
    {
        exception.expect(NumberFormatException.class);
        exception.expectMessage("illegal char");
        stringToLong("12+34");
    }

    @Test
    public final void test13()
    {
        exception.expect(NumberFormatException.class);
        exception.expectMessage("illegal char");
        stringToLong("abcd");
    }
}
