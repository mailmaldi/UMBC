package main;

import java.util.Map;

import memory.DataMemoryFileParser;
import memory.DataMemoryManager;
import registers.RegisterFileParser;
import registers.RegisterManager;
import config.ConfigManager;
import config.ConfigParser;

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

        DataMemoryFileParser.parseMemoryFile(args[1]);
        DataMemoryManager.instance.dumpAllMemory();

        ConfigParser.parseConfigFile(args[3]);

        ConfigManager.instance.dumpConfiguration();

        Map<Integer, Integer> map = DataMemoryManager.instance
                .getMemoryBlockOfAddress(289, 16);
        for (Integer key : map.keySet())
            System.out.println(key + " " + map.get(key));

    }
}
