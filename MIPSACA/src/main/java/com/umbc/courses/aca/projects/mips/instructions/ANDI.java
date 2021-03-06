package com.umbc.courses.aca.projects.mips.instructions;

public class ANDI extends TwoRegImmediateInstruction
{

    public ANDI(String sourceLabel, String destinationLabel, int immediate)
    {
        super(sourceLabel, destinationLabel, immediate);
        this.setFunctionalUnitType(EXFunctionalUnitType.IU);
        this.setInstructionType(InstructionType.ARITHMETIC_IMM);
    }

    public ANDI(ANDI obj)
    {
        super(obj);
    }

    public int getImmediate()
    {
        return this.immediate;
    }

    @Override
    public String toString()
    {
        return "ANDI " + super.toString();
    }

    @Override
    public void executeInstruction()
    {
        dest.setDestination(src1.getSource() & immediate);
    }

}
