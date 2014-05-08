package com.umbc.courses.aca.projects.mips.memory;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

import com.umbc.courses.aca.projects.mips.results.ResultsManager;

// NOTE I break out of parsing after encountering the first empty line or file finishes
public class DataMemoryFileParser
{
    public static void parseMemoryFile(String fileName) throws Exception
    {
        BufferedReader bfread = null;
        int count = 0;
        try
        {

            bfread = new BufferedReader(new FileReader(new File(fileName)));

            String line = null;
            count = 0;
            int initialAddress = 0x100;

            while ((line = bfread.readLine()) != null)
            {
                line = line.trim();
                if (line.length() == 0)
                    break; // break on the first empty line
                int value = Integer.parseInt(line, 2);

                DataMemoryManager.instance.setValueToAddress(initialAddress,
                        value);
                initialAddress += 4;

                count++;
            }
            System.out.println("Total Number of memory locations = " + count);
        }
        catch (Exception e)
        {
            String message = "Invalid Data Memory file: " + e.getMessage()
                    + " Line [" + count + "]";
            System.err.println(message);
            ResultsManager.instance.writeLine(message);
            throw e;
        }
        finally
        {
            if (bfread != null)
                bfread.close();
        }

    }
}
