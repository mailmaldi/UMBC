package instruction;

import java.util.ArrayList;
import java.util.List;

public class SW extends Instruction
{

    String sourceLabel;
    String destinationLabel;
    long   source;
    long   destination;
    int    immediate;

    public SW(String sourceLabel, String desitnationLabel, int immediate)
    {
        super();
        this.sourceLabel = sourceLabel;
        this.destinationLabel = desitnationLabel;
        this.immediate = immediate;
    }

    public SW(SW obj)
    {
        super(obj);
        setPrintableInstruction(obj.printableInstruction);
        sourceLabel = obj.sourceLabel;
        destinationLabel = obj.destinationLabel;
        source = obj.source;
        destination = obj.destination;
        immediate = obj.immediate;
    }

    @Override
    public List<String> getSourceRegister()
    {
        List<String> sourceRegisterList = new ArrayList<String>();
        sourceRegisterList.add(sourceLabel);
        return sourceRegisterList;
    }

    @Override
    public String getDestinationRegister()
    {
        return destinationLabel;
    }

    public int getImmediate()
    {
        return immediate;
    }

    @Override
    public String toString()
    {
        return "SW " + sourceLabel + " " + immediate + "(" + destinationLabel
                + ")";
    }

    @Override
    public void executeInstruction()
    {
        // TODO - Complete this method
        destination = immediate + source;
    }

    @Override
    public void decodeInstruction()
    {

    }

    @Override
    public void writeBackResult()
    {

    }

}
