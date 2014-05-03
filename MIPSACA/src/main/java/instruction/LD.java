package instruction;


public class LD extends TwoRegImmediateInstruction
{

    public LD(String sourceLabel, String destinationLabel, int immediate)
    {
        super(sourceLabel, destinationLabel, immediate);
    }

    public LD(LD obj)
    {
        super(obj);
    }

    @Override
    public String toString()
    {
        return "LD " + dest.getDestinationLabel() + ", " + immediate + "("
                + src1.getSourceLabel() + ")";
    }

    @Override
    public void executeInstruction()
    {
    }
}
