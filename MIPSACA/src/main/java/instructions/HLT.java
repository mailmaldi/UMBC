package instructions;

import java.util.List;

public class HLT extends Instruction
{
    public HLT()
    {
        super();
        this.setInstructionType(InstructionType.HALT);
    }

    public HLT(HLT obj)
    {
        super(obj);
    }

    @Override
    public List<SourceObject> getSourceRegister()
    {
        return null;
    }

    @Override
    public WriteBackObject getDestinationRegister()
    {
        return null;
    }

    @Override
    public String toString()
    {
        return "HLT";
    }

    @Override
    public void executeInstruction()
    {
    }

}
