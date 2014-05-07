package com.umbc.courses.aca.projects.mips.stages;

public abstract class Stage implements StageI
{
    protected StageType stageType;

    public boolean checkIfFree() throws Exception
    {
        return checkIfFree(null); // will throw exception for ExStage if used
    }
}
