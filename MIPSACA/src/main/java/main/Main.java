package main;

import instructions.HLT;
import instructions.Instruction;
import memory.DataMemoryFileParser;
import program.ProgramManager;
import program.ProgramParser;
import registers.RegisterFileParser;
import results.ResultsManager;
import stages.CPU;
import stages.DecodeStage;
import stages.ExStage;
import stages.FetchStage;
import stages.WriteBackStage;
import config.ConfigParser;

public class Main
{
    public static void main(String[] args) throws Exception
    {
        for (int i = 0; i < args.length; i++)
            System.out.print(args[i] + " ");
        System.out.println();

        ProgramParser.parse(args[0]);
        ProgramManager.instance.dumpProgram();

        DataMemoryFileParser.parseMemoryFile(args[1]);

        RegisterFileParser.parseRegister(args[2]);

        ConfigParser.parseConfigFile(args[3]);

        CPU.CLOCK = 0;
        CPU.PROGRAM_COUNTER = 0;

        WriteBackStage writeBack = WriteBackStage.getInstance();
        ExStage ex = ExStage.getInstance();
        DecodeStage decode = DecodeStage.getInstance();
        FetchStage fetch = FetchStage.getInstance();

        /*
         * Instruction test =
         * ProgramManager.instance.getInstructionAtAddress(0);
         * 
         * test.entryCycle[0] = 1; test.entryCycle[1] = 2; test.entryCycle[2] =
         * 3; test.entryCycle[3] = 5;
         * 
         * test.exitCycle[0] = 5; test.exitCycle[1] = 6; test.exitCycle[2] = 7;
         * test.exitCycle[3] = 8;
         * 
         * test.RAW = true; test.getDestinationRegister().setDestination(1000);
         * for (SourceObject reg : test.getSourceRegister()) {
         * reg.setSource(RegisterManager.instance.getRegisterValue("R4")); }
         * 
         * ex.acceptInstruction(test);
         */

        try
        {

            while (CPU.CLOCK < 1000)
            {

                writeBack.execute();
                ex.execute();
                decode.execute();
                fetch.execute();

                Instruction next = new HLT();
                if (fetch.checkIfFree(next))
                {
                    next = ProgramManager.instance
                            .getInstructionAtAddress(CPU.PROGRAM_COUNTER);
                    if (fetch.acceptInstruction(next))
                        CPU.PROGRAM_COUNTER++;
                }

                CPU.CLOCK++;

                // if (ResultsManager.instance.isHALT())
                // break;

            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        finally
        {
            System.out.println("Results");
            ResultsManager.instance.printResults();
        }

    }
}
