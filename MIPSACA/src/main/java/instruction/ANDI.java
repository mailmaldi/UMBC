package instruction;

import java.util.ArrayList;
import java.util.List;

public class ANDI extends Instruction
{
    SourceObject    src1;
    WriteBackObject dest;
    int             immediate;

    public ANDI(String sourceLabel, String destinationLabel, int immediate)
    {
        super();
        src1 = new SourceObject(sourceLabel, 0);
        dest = new WriteBackObject(destinationLabel, 0);
        this.immediate = immediate;
    }

    public ANDI(ANDI obj)
    {
        super(obj);
        setPrintableInstruction(obj.printableInstruction);
        this.src1 = new SourceObject(obj.src1);
        this.dest = new WriteBackObject(obj.dest);
        this.immediate = obj.immediate;
    }

    @Override
    public List<SourceObject> getSourceRegister()
    {
        List<SourceObject> sourceRegisterList = new ArrayList<SourceObject>();
        sourceRegisterList.add(src1);
        return sourceRegisterList;
    }

    @Override
    public WriteBackObject getDestinationRegister()
    {
        return dest;
    }

    public int getImmediate()
    {
        return this.immediate;
    }

    @Override
    public String toString()
    {
        return "ANDI " + dest.getDestinationLabel() + ", "
                + src1.getSourceLabel() + ", " + immediate;
    }

    @Override
    public void executeInstruction()
    {
        dest.setDestination(src1.getSource() & immediate);
    }

}
