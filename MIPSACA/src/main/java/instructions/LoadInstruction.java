package instructions;

public abstract class LoadInstruction extends TwoRegImmediateInstruction
{

    public LoadInstruction(String sourceLabel, String destinationLabel,
            int immediate)
    {
        super(sourceLabel, destinationLabel, immediate);
        this.setFunctionalUnitType(EXFunctionalUnitType.IU);
    }

    public LoadInstruction(LoadInstruction li)
    {
        super(li);
    }

    @Override
    public void executeInstruction() throws Exception
    {
        this.setDestinationAddress(immediate + src1.getSource());
    }

    public String toString()
    {
        return dest.getDestinationLabel() + ", " + immediate + "("
                + src1.getSourceLabel() + ")";
    }
}
