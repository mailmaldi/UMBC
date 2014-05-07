package com.umbc.courses.aca.projects.mips.instructions;

public class DSUBI extends TwoRegImmediateInstruction
{

    public DSUBI(String sourceLabel, String destinationLabel, int immediate)
    {
        super(sourceLabel, destinationLabel, immediate);
        this.setFunctionalUnitType(EXFunctionalUnitType.IU);
        this.setInstructionType(InstructionType.ARITHMETIC_IMM);
    }

    public DSUBI(DSUBI obj)
    {
        super(obj);
    }

    @Override
    public String toString()
    {
        return "DSUBI " + super.toString();
    }

    @Override
    public void executeInstruction()
    {
        dest.setDestination(src1.getSource() - immediate);
    }
}
