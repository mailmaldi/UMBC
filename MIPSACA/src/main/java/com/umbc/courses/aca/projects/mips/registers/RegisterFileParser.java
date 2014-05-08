package com.umbc.courses.aca.projects.mips.registers;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

import com.umbc.courses.aca.projects.mips.results.ResultsManager;

public class RegisterFileParser
{

    public static void parseRegister(String fileName) throws Exception
    {
        BufferedReader bfread = null;
        int count = 0;
        try
        {
            bfread = new BufferedReader(new FileReader(new File(fileName)));

            String line = null;
            count = 0;
            while ((line = bfread.readLine()) != null)
            {
                line = line.trim();
                if (line.length() == 0)
                    throw new Exception(
                            "Less than 32 Integer register data in reg.txt, count= "
                                    + count);
                int value = Integer.parseInt(line, 2);
                RegisterManager.instance.setRegisterValue("R" + count, value);

                count++;

                if (count == 32)
                    break;
            }

        }
        catch (Exception e)
        {
            String message = "Invalid register data file: " + e.getMessage()
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
