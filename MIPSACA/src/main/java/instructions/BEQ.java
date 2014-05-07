package instructions;

public class BEQ extends ConditionalBranchInstruction
{

    public BEQ(String sourceLabel1, String sourceLabel2, String destinationLabel)
    {
        super(sourceLabel1, sourceLabel2, destinationLabel);
    }

    public BEQ(BEQ obj)
    {
        super(obj);
    }

    @Override
    public String toString()
    {
        return "BEQ " + super.toString();
    }

    @Override
    public boolean shouldBranch()
    {
        return compareRegisters();
    }
}
