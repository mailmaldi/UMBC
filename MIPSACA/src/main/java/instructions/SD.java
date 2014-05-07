package instructions;

public class SD extends StoreInstruction
{

    public SD(String sourceLabel, String sourceLabel2, int immediate)
    {
        super(sourceLabel, sourceLabel2, immediate);
        this.setInstructionType(InstructionType.MEMORY_FPREG);
    }

    public SD(SD obj)
    {
        super(obj);
    }

    @Override
    public String toString()
    {
        return "SD " + super.toString();
    }
}
