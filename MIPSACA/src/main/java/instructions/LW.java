package instructions;

public class LW extends LoadInstruction
{
    public LW(String sourceLabel, String destinationLabel, int immediate)
    {
        super(sourceLabel, destinationLabel, immediate);
        this.setInstructionType(InstructionType.MEMORY_REG);
    }

    public LW(LW obj)
    {
        super(obj);
    }

    @Override
    public String toString()
    {
        return "LW " + super.toString();
    }
}
