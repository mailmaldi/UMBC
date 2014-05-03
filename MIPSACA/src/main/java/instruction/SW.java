package instruction;

public class SW extends TwoRegImmediateInstruction
{

    public SW(String sourceLabel, String destinationLabel, int immediate)
    {
        super(sourceLabel, destinationLabel, immediate);
    }

    public SW(SW obj)
    {
        super(obj);
    }

    @Override
    public String toString()
    {
        return "SW" + dest.getDestinationLabel() + ", " + immediate + "("
                + src1.getSourceLabel() + ")";
    }

    @Override
    public void executeInstruction()
    {
    }

}
