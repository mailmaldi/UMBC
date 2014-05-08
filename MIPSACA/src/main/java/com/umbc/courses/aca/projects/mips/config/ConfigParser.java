package com.umbc.courses.aca.projects.mips.config;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

public class ConfigParser
{

    public static void parseConfigFile(String fileName) throws Exception
    {
        BufferedReader bfread = null;
        try
        {

            bfread = new BufferedReader(new FileReader(new File(fileName)));

            String line = null;
            int count = 0;

            while ((line = bfread.readLine()) != null)
            {
                line = line.trim();
                if (line.length() == 0)
                    continue;
                count++;

                parseConfigurationLine(line);

            }
            System.out.println("Total Number of Config Elements = " + count);
        }
        finally
        {
            if (bfread != null)
                bfread.close();
        }

    }

    private static void parseConfigurationLine(String line) throws Exception
    {
        String s[], s1[];
        line = line.trim();
        line = line.toLowerCase();

        s = line.split(":");

        String configItem = s[0].trim().toLowerCase();

        switch (configItem)
        {
            case "fp adder":
                s1 = s[1].split(",");
                ConfigManager.instance.FPAdderLatency = parseConfigInteger(s1[0]);
                ConfigManager.instance.FPAdderPipelined = parseConfigBoolean(s1[1]);
                break;

            case "fp multiplier":
                s1 = s[1].split(",");
                ConfigManager.instance.FPMultLatency = parseConfigInteger(s1[0]);
                ConfigManager.instance.FPMultPipelined = parseConfigBoolean(s1[1]);
                break;

            case "fp divider":
                s1 = s[1].split(",");
                ConfigManager.instance.FPDivideLatency = parseConfigInteger(s1[0]);
                ConfigManager.instance.FPDividerPipelined = parseConfigBoolean(s1[1]);
                break;

            case "main memory":
                ConfigManager.instance.MemoryLatency = parseConfigInteger(s[1]);
                break;

            case "i-cache":
                ConfigManager.instance.ICacheLatency = parseConfigInteger(s[1]);
                break;

            case "d-cache":
                ConfigManager.instance.DCacheLatency = parseConfigInteger(s[1]);
                break;
        }

    }

    private static int parseConfigInteger(String string) throws Exception
    {
        int val = Integer.parseInt(string.trim());
        if (val <= 0)
            throw new Exception("Invalid Cycle Count: " + val);
        return val;
    }

    private static boolean parseConfigBoolean(String string) throws Exception
    {
        String str = string.trim().toLowerCase();
        if (!("yes".equals(str) || "no".equals(str)))
            throw new Exception("Invalid config element: " + str);
        return "yes".equals(str);
    }
}