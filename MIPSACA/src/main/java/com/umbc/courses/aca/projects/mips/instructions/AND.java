package com.umbc.courses.aca.projects.mips.instructions;

public class AND extends ThreeRegInstruction
{

    public AND(String sourceLabel1, String sourceLabel2, String destinationLabel)
    {
        super(sourceLabel1, sourceLabel2, destinationLabel);
        this.setFunctionalUnitType(EXFunctionalUnitType.IU);
        this.setInstructionType(InstructionType.ARITHMETIC_REG);
    }

    public AND(AND obj)
    {
        super(obj);
    }

    @Override
    public String toString()
    {
        return "AND " + super.toString();
    }

    @Override
    public void executeInstruction()
    {
        dest.setDestination(src1.getSource() & src2.getSource());
    }

}
