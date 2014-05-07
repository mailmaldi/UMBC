package instructions;

public class SUBD extends ThreeRegInstruction
{
    public SUBD(String sourceLabel1, String sourceLabel2,
            String destinationLabel)
    {
        super(sourceLabel1, sourceLabel2, destinationLabel);
        this.setFunctionalUnitType(EXFunctionalUnitType.FPADD);
        this.setInstructionType(InstructionType.ARITHMETIC_FPREG);
    }

    public SUBD(SUBD obj)
    {
        super(obj);
    }

    @Override
    public String toString()
    {
        return "SUBD " + super.toString();
    }

    @Override
    public void executeInstruction()
    {
        dest.setDestination(src1.getSource() - src2.getSource());
    }

}
