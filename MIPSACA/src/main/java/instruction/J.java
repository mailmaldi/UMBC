package instruction;

import java.util.List;

public class J extends Instruction
{

    String destinationLabel;

    public J(String destinationLabel)
    {
        super();
        this.destinationLabel = destinationLabel;
    }

    @Override
    public List<String> getSourceRegister()
    {
        return null;
    }

    @Override
    public String getDestinationRegister()
    {
        return destinationLabel;
    }

    @Override
    public String toString()
    {
        return super.toString() + "J " + destinationLabel;
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
