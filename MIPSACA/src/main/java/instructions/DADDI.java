package instructions;

public class DADDI extends TwoRegImmediateInstruction
{

    public DADDI(String sourceLabel, String destinationLabel, int immediate)
    {
        super(sourceLabel, destinationLabel, immediate);
        this.setFunctionalUnitType(EXFunctionalUnitType.IU);
        this.setInstructionType(InstructionType.ARITHMETIC_IMM);
    }

    public DADDI(DADDI obj)
    {
        super(obj);
    }

    @Override
    public String toString()
    {
        return "DADDI " + super.toString();
    }

    @Override
    public void executeInstruction()
    {
        dest.setDestination(src1.getSource() + immediate);
    }

}
