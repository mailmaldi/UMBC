package com.umbc.courses.aca.projects.mips.instructions;

public class SW extends StoreInstruction
{

    public SW(String sourceLabel, String sourceLabel2, int immediate)
    {
        super(sourceLabel, sourceLabel2, immediate);
        this.setInstructionType(InstructionType.MEMORY_REG);
    }

    public SW(SW obj)
    {
        super(obj);
    }

    @Override
    public String toString()
    {
        return "SW " + super.toString();
    }
}
