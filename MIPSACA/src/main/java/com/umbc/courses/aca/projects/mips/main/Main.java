package com.umbc.courses.aca.projects.mips.main;

import com.umbc.courses.aca.projects.mips.config.ConfigManager;
import com.umbc.courses.aca.projects.mips.config.ConfigParser;
import com.umbc.courses.aca.projects.mips.main.CPU.RUN;
import com.umbc.courses.aca.projects.mips.memory.DataMemoryFileParser;
import com.umbc.courses.aca.projects.mips.memory.DataMemoryManager;
import com.umbc.courses.aca.projects.mips.program.ProgramManager;
import com.umbc.courses.aca.projects.mips.program.ProgramParser;
import com.umbc.courses.aca.projects.mips.registers.RegisterFileParser;
import com.umbc.courses.aca.projects.mips.registers.RegisterManager;
import com.umbc.courses.aca.projects.mips.results.ResultsManager;

public class Main
{
    /**
     * 
     * @param args
     *            inst.txt data.txt reg.txt config.txt result.txt
     * @throws Exception
     */
    public static void main(String[] args) throws Exception
    {
        System.out.println(args.length);
        if (args.length < 5 || args.length > 6)
        {
            System.err
                    .println("Need correct arguments in order: <instructions> <memorydata> <registerdata> <configtext> <resultsfile> (optional)[PIPELINE]");
            System.exit(1);
        }
        if (args.length == 6)
        {
            String arg6 = args[5].trim().toUpperCase();

            try
            {
                CPU.RUN_TYPE = RUN.valueOf(arg6);
            }
            catch (IllegalArgumentException e)
            {
                System.err
                        .println("Value of 6th argument can be either MEMORY or PIPELINE, or do not provide for MEMORY as default");
                System.exit(1);
            }
            System.out.println(CPU.RUN_TYPE);
        }

        ProgramParser.parse(args[0]);
        ProgramManager.instance.dumpProgram();

        DataMemoryFileParser.parseMemoryFile(args[1]);
        DataMemoryManager.instance.dumpAllMemory();

        RegisterFileParser.parseRegister(args[2]);
        RegisterManager.instance.dumpAllRegisters();

        ConfigParser.parseConfigFile(args[3]);
        ConfigManager.instance.dumpConfiguration();

        ResultsManager.instance.setResultsPath(args[4]);

        /**
         * Initialize Global CLOCK and PC to 0
         */
        CPU.CLOCK = 0;
        CPU.PROGRAM_COUNTER = 0;

        Pipeline.getInstance().execute();

        Thread.sleep(1000L);
        System.out.println("Results");
        ResultsManager.instance.printResults();
        ResultsManager.instance.writeResults();

        // RegisterManager.instance.dumpAllRegisters();

    }
}
