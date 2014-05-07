package instructions;

import java.util.ArrayList;
import java.util.List;

public abstract class ConditionalBranchInstruction extends Instruction
{
    private SourceObject src1, src2;

    private String       destinationLabel;

    public ConditionalBranchInstruction(String sourceLabel1,
            String sourceLabel2, String destinationLabel)
    {
        super();
        src1 = new SourceObject(sourceLabel1, 0);
        src2 = new SourceObject(sourceLabel2, 0);
        this.destinationLabel = destinationLabel;
        this.setInstructionType(InstructionType.BRANCH);
    }

    public ConditionalBranchInstruction(ConditionalBranchInstruction obj)
    {
        super(obj);
        src1 = new SourceObject(obj.src1);
        src2 = new SourceObject(obj.src2);
        destinationLabel = obj.destinationLabel;
    }

    @Override
    public List<SourceObject> getSourceRegister()
    {
        List<SourceObject> sourceRegisterList = new ArrayList<SourceObject>();
        sourceRegisterList.add(src1);
        sourceRegisterList.add(src2);
        return sourceRegisterList;
    }

    @Override
    public WriteBackObject getDestinationRegister()
    {
        return null;
    }

    public String getDestinationLabel()
    {
        return destinationLabel;
    }

    protected boolean compareRegisters()
    {
        return (src1.getSource() == src2.getSource());
    }

    /*
     * Decide when should branch be taken
     */
    public abstract boolean shouldBranch();

    @Override
    public String toString()
    {
        return src1.getSourceLabel() + ", " + src2.getSourceLabel() + ", "
                + destinationLabel;
    }

    @Override
    public void executeInstruction()
    {
        // Do nothing here
    }
}
