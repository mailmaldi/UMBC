package com.umbc.courses.aca.projects.mips.stages;

import com.umbc.courses.aca.projects.mips.functionalUnits.FetchUnit;
import com.umbc.courses.aca.projects.mips.instructions.Instruction;

public class FetchStage extends Stage
{

    private static volatile FetchStage instance;

    public static FetchStage getInstance()
    {
        if (null == instance)
            synchronized (FetchStage.class)
            {
                if (null == instance)
                    instance = new FetchStage();
            }
        return instance;
    }

    private FetchUnit fetch;

    private FetchStage()
    {
        super();
        fetch = FetchUnit.getInstance();
        this.stageType = StageType.IFSTAGE;
    }

    @Override
    public void execute() throws Exception
    {
        fetch.executeUnit();
    }

    @Override
    public boolean checkIfFree(Instruction instruction) throws Exception
    {
        return fetch.checkIfFree(instruction);
    }

    @Override
    public boolean acceptInstruction(Instruction instruction) throws Exception
    {
        if (!fetch.checkIfFree(instruction))
            throw new Exception("FetchStage: Illegal state exception "
                    + instruction.toString());

        fetch.acceptInstruction(instruction);
        return true;
    }

    public void flushStage() throws Exception
    {
        fetch.flushUnit();
    }

}
