package com.umbc.courses.aca.projects.mips.instructions;

public class ADDD extends ThreeRegInstruction
{

    public ADDD(String sourceLabel1, String sourceLabel2,
            String destinationLabel)
    {
        super(sourceLabel1, sourceLabel2, destinationLabel);
        this.setFunctionalUnitType(EXFunctionalUnitType.FPADD);
        this.setInstructionType(InstructionType.ARITHMETIC_FPREG);
    }

    public ADDD(ADDD obj)
    {
        super(obj);
    }

    @Override
    public String toString()
    {
        return "ADDD " + super.toString();
    }

    @Override
    public void executeInstruction()
    {
        dest.setDestination(src1.getSource() + src2.getSource());
    }

}
