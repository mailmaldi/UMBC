package com.umbc.courses.aca.projects.mips.results;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.TreeMap;

import com.umbc.courses.aca.projects.mips.Utils.Constants;
import com.umbc.courses.aca.projects.mips.cache.DCacheManager;
import com.umbc.courses.aca.projects.mips.cache.ICacheManager;
import com.umbc.courses.aca.projects.mips.instructions.Instruction;
import com.umbc.courses.aca.projects.mips.program.ProgramManager;
import com.umbc.courses.aca.projects.mips.stages.StageType;

public class ResultsManager
{

    public static final ResultsManager          instance       = new ResultsManager();

    private final TreeMap<Integer, Instruction> instructionMap = new TreeMap<Integer, Instruction>();

    private BufferedWriter                      resultsWriter  = null;

    private boolean                             HALT           = false;

    private ResultsManager()
    {
    }

    // call this only once in ur program
    public void setResultsPath(String path) throws IOException
    {
        if (resultsWriter != null)
            return;
        resultsWriter = new BufferedWriter(new FileWriter(new File(path)));
    }

    public void printResults()
    {

        System.out.println(String.format(
                Constants.instructionOutputFormatString, "Instruction", "FT",
                "ID", "EX", "WB", "RAW", "WAR", "WAW", "Struct"));
        for (int key : instructionMap.keySet())
        {
            Instruction inst = instructionMap.get(key);
            // System.out.format("%-3s ", key);
            // System.out.println(inst.debugString());
            System.out.println(inst.getOutputString());
        }
        System.out.println(ICacheManager.instance.getICacheStatistics());
        System.out.println(DCacheManager.instance.getDCacheStatistics());
    }

    public void writeResults()
    {
        try
        {
            resultsWriter.write(String.format(
                    Constants.instructionOutputFormatString, "Instruction",
                    "FT", "ID", "EX", "WB", "RAW", "WAR", "WAW", "Struct"));
            resultsWriter.newLine();
            for (int key : instructionMap.keySet())
            {
                Instruction inst = instructionMap.get(key);
                resultsWriter.write(inst.getOutputString());
                resultsWriter.newLine();
            }
            resultsWriter.write(ICacheManager.instance.getICacheStatistics());
            resultsWriter.newLine();
            resultsWriter.write(DCacheManager.instance.getDCacheStatistics());
            resultsWriter.newLine();
            resultsWriter.flush();
            resultsWriter.close();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    public void addInstruction(Instruction instruction)
    {
        int key = instruction.getEntryCycleForStage(StageType.IFSTAGE.getId());
        instructionMap.put(key, instruction);
    }

    public void testPrintWithDummyData() throws Exception
    {
        int count = 0;
        for (Integer address : ProgramManager.instance.InstructionList.keySet())
        {
            Instruction inst = ProgramManager.instance
                    .getInstructionAtAddress(address);
            inst.setEntryCycleForStage(0, count++);
            inst.setExitCycleForStage(0, count);
            inst.setStruct(count % 2 == 0);
            addInstruction(inst);
        }

        printResults();

    }

    public boolean isHALT()
    {
        return HALT;
    }

    public void setHALT(boolean halt)
    {
        this.HALT = halt;
    }
}
