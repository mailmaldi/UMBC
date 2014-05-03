package instruction;

import java.util.List;

public class NOOP extends Instruction
{

    public NOOP()
    {
        super();
    }

    public NOOP(NOOP obj)
    {
        super(obj);
        setPrintableInstruction(obj.printableInstruction);
    }

    @Override
    public List<String> getSourceRegister()
    {
        // Do nothing here
        return null;
    }

    @Override
    public String getDestinationRegister()
    {
        // Do nothing here
        return null;
    }

    @Override
    public void executeInstruction()
    {
        // Do nothing here

    }

    @Override
    public void decodeInstruction()
    {
        // Do nothing here

    }

    @Override
    public void writeBackResult()
    {
        // Do nothing here

    }

    @Override
    public String toString()
    {
        return "NOOP";
    }

}
