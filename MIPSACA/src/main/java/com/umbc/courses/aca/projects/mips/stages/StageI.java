package com.umbc.courses.aca.projects.mips.stages;

import com.umbc.courses.aca.projects.mips.instructions.Instruction;

public interface StageI
{
    // A Stage executes once every clock cycle
    public void execute() throws Exception;

    // check if a stage is free to accept a new instruction
    public boolean checkIfFree() throws Exception;

    public boolean checkIfFree(Instruction instruction) throws Exception;

    // Put a new instruction in a stage - do this ifFree
    public boolean acceptInstruction(Instruction instruction) throws Exception;

}
