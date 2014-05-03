package main;

import java.util.concurrent.atomic.AtomicInteger;

import config.ConfigManager;
import config.ConfigParser;
import memory.MemoryFileParser;
import memory.MemoryManager;
import registers.RegisterFileParser;
import registers.RegisterManager;

public class Main
{
    public static void main(String[] args) throws Exception
    {
        System.out.println("HELLO ");
        for (int i = 0; i < args.length; i++)
            System.out.print(args[i] + " ");
        System.out.println();

        RegisterFileParser.parseRegister(args[2]);

        RegisterManager.instance.setRegisterBusy("R0");
        RegisterManager.instance.dumpAllRegisters();

        MemoryFileParser.parseMemoryFile(args[1]);
        MemoryManager.instace.dumpAllMemory();

        ConfigParser.parseConfigFile(args[3]);

        ConfigManager.instance.dumpConfiguration();

    }
}
