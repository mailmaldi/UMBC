package instruction;

import java.util.List;

public class HLT extends Instruction
{
    public HLT()
    {
        super();
    }

    public HLT(HLT obj)
    {
        super();
        setPrintableInstruction(obj.printableInstruction);
    }

    @Override
    public List<String> getSourceRegister()
    {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public String getDestinationRegister()
    {
        // TODO Auto-generated method stub
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
        // Do nothing here

    }

    @Override
    public void decodeInstruction()
    {
        // TODO Auto-generated method stub

    }

    @Override
    public void writeBackResult()
    {
        // TODO Auto-generated method stub

    }

}
