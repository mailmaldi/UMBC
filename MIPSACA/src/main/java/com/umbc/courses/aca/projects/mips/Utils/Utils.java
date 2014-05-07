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
}
