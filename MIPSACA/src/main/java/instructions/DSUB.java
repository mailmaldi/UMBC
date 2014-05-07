package instructions;

public class DSUB extends ThreeRegInstruction
{
    public DSUB(String sourceLabel1, String sourceLabel2,
            String destinationLabel)
    {
        super(sourceLabel1, sourceLabel2, destinationLabel);
        this.setFunctionalUnitType(EXFunctionalUnitType.IU);
        this.setInstructionType(InstructionType.ARITHMETIC_REG);
    }

    public DSUB(DSUB obj)
    {
        super(obj);
    }

    @Override
    public String toString()
    {
        return "DSUB " + super.toString();
    }

    @Override
    public void executeInstruction()
    {
        dest.setDestination(src1.getSource() - src2.getSource());
    }

}
