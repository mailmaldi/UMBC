package com.umbc.courses.aca.projects.mips.Utils;

public class Utils
{

    public static boolean isPowerOf2(int val)
    {
        return (val > 0) && (val & (val - 1)) == 0;
    }

    public static int getDCacheTag(int address)
    {
        int baseAddress = address >> 5;
        baseAddress = baseAddress << 5;
        return baseAddress;
    }

    public static int getDCacheSet(int address)
    {
        int baseAddress = address >> 4;
        baseAddress = baseAddress & 0b1;
        return baseAddress;
    }

    public static boolean areDCacheAddressesSameBlock(int address1, int address2)
    {
        if ((getDCacheSet(address1) == getDCacheSet(address2))
                && (getDCacheTag(address1) == getDCacheTag(address2)))
            return true;
        else
            return false;
    }
}
