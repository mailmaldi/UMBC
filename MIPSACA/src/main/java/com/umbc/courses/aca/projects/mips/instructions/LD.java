package com.umbc.courses.aca.projects.mips.instructions;

public class LD extends LoadInstruction
{
    public LD(String sourceLabel, String destinationLabel, int immediate)
    {
        super(sourceLabel, destinationLabel, immediate);
        this.setInstructionType(InstructionType.MEMORY_FPREG);
    }

    public LD(LD obj)
    {
        super(obj);
    }

    @Override
    public String toString()
    {
        return "LD " + super.toString();
    }
}
