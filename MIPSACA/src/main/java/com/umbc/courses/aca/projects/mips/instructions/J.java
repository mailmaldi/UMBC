package com.umbc.courses.aca.projects.mips.instructions;

import java.util.List;

public class J extends Instruction
{

    String destinationLabel;

    public J(String destinationLabel)
    {
        super();
        this.destinationLabel = destinationLabel;
        this.setInstructionType(InstructionType.JUMP);
    }

    public J(J obj)
    {
        super(obj);
        destinationLabel = obj.destinationLabel;
    }

    @Override
    public List<SourceObject> getSourceRegister()
    {
        return null;
    }

    @Override
    public WriteBackObject getDestinationRegister()
    {
        return null;
    }

    public String getDestinationLabel()
    {
        return destinationLabel;
    }

    @Override
    public String toString()
    {
        return "J " + destinationLabel;
    }

    @Override
    public void executeInstruction()
    {
    }
}
