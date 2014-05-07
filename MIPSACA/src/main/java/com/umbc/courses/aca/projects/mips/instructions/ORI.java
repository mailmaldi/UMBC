package com.umbc.courses.aca.projects.mips.instructions;

public class ORI extends TwoRegImmediateInstruction
{

    public ORI(String sourceLabel, String destinationLabel, int immediate)
    {
        super(sourceLabel, destinationLabel, immediate);
        this.setFunctionalUnitType(EXFunctionalUnitType.IU);
        this.setInstructionType(InstructionType.ARITHMETIC_IMM);
    }

    public ORI(ORI obj)
    {
        super(obj);
    }

    @Override
    public String toString()
    {
        return "ORI " + super.toString();
    }

    @Override
    public void executeInstruction()
    {
        dest.setDestination(src1.getSource() | immediate);
    }

}
