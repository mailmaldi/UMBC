package main;

import memory.DataMemoryFileParser;
import pipestages.CPU;
import program.Program;
import registers.RegisterFileParser;
import config.ConfigParser;

public class Main
{
    public static void main(String[] args) throws Exception
    {
        for (int i = 0; i < args.length; i++)
            System.out.print(args[i] + " ");
        System.out.println();

        Program.instance.parse(args[0]);

        DataMemoryFileParser.parseMemoryFile(args[1]);

        RegisterFileParser.parseRegister(args[2]);

        ConfigParser.parseConfigFile(args[3]);

        CPU.CLOCK = 0;
        CPU.PROGRAM_COUNTER = 0;

        while (CPU.CLOCK < 100)
        {

            CPU.CLOCK++;

            CPU.PROGRAM_COUNTER++;
        }
    }
}
