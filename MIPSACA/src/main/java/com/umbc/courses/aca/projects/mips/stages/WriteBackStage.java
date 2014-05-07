package com.umbc.courses.aca.projects.mips.stages;

import com.umbc.courses.aca.projects.mips.functionalUnits.WriteBackUnit;
import com.umbc.courses.aca.projects.mips.instructions.Instruction;

public class WriteBackStage extends Stage
{

    private static volatile WriteBackStage instance;

    public static WriteBackStage getInstance()
    {

        if (null == instance)
            synchronized (WriteBackStage.class)
            {
                if (null == instance)
                    instance = new WriteBackStage();
            }

        return instance;
    }

    private WriteBackUnit writeBack;

    private WriteBackStage()
    {
        super();

        this.stageType = StageType.WBSTAGE;

        writeBack = WriteBackUnit.getInstance();
    }

    @Override
    public void execute() throws Exception
    {
        /*
         * System.out.println("------------------------------");
         * System.out.println("WRITEBACK - "); writeBack.dumpUnitDetails();
         * System.out.println("------------------------------");
         */

        writeBack.executeUnit();
    }

    @Override
    public boolean acceptInstruction(Instruction instruction) throws Exception
    {
        writeBack.acceptInstruction(instruction);
        return true;
    }

    @Override
    public boolean checkIfFree(Instruction instruction) throws Exception
    {
        return writeBack.checkIfFree(instruction);
    }
}
