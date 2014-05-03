package instruction;

import java.util.ArrayList;
import java.util.List;

public class DSUB extends Instruction
{
    SourceObject    src1, src2;
    WriteBackObject dest;

    public DSUB(String sourceLabel1, String sourceLabel2,
            String destinationLabel)
    {
        super();
        src1 = new SourceObject(sourceLabel1, 0);
        src2 = new SourceObject(sourceLabel2, 0);
        dest = new WriteBackObject(destinationLabel, 0);
    }

    public DSUB(DSUB obj)
    {
        super(obj);
        setPrintableInstruction(obj.printableInstruction);

        src1 = new SourceObject(obj.src1);
        src2 = new SourceObject(obj.src2);
        dest = new WriteBackObject(obj.dest);

    }

    @Override
    public List<SourceObject> getSourceRegister()
    {
        List<SourceObject> sourceRegisterList = new ArrayList<SourceObject>();

        sourceRegisterList.add(this.src1);
        sourceRegisterList.add(this.src2);

        return sourceRegisterList;
    }

    @Override
    public WriteBackObject getDestinationRegister()
    {
        return dest;
    }

    @Override
    public String toString()
    {
        return "DSUB " + dest.getDestinationLabel() + ", "
                + src1.getSourceLabel() + ", " + src2.getSourceLabel();
    }

    @Override
    public void executeInstruction()
    {
        dest.setDestination(src1.getSource() - src2.getSource());
    }

}
