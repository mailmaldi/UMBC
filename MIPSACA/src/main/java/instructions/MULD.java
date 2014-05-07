package instructions;

public class MULD extends ThreeRegInstruction
{
    public MULD(String sourceLabel1, String sourceLabel2,
            String destinationLabel)
    {
        super(sourceLabel1, sourceLabel2, destinationLabel);
        this.setFunctionalUnitType(EXFunctionalUnitType.FPMUL);
        this.setInstructionType(InstructionType.ARITHMETIC_FPREG);
    }

    public MULD(MULD obj)
    {
        super(obj);
    }

    @Override
    public String toString()
    {
        return "MULD " + super.toString();
    }

    @Override
    public void executeInstruction()
    {
        dest.setDestination(src1.getSource() * src2.getSource());
    }

}
