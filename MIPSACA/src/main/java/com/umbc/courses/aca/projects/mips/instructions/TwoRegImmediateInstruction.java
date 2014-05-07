package com.umbc.courses.aca.projects.mips.instructions;

import java.util.ArrayList;
import java.util.List;

public abstract class TwoRegImmediateInstruction extends Instruction
{
    protected SourceObject    src1;
    protected WriteBackObject dest;
    protected int             immediate;

    public TwoRegImmediateInstruction(String sourceLabel,
            String destinationLabel, int immediate)
    {
        super();
        src1 = new SourceObject(sourceLabel, 0);
        dest = new WriteBackObject(destinationLabel, 0);
        this.immediate = immediate;
    }

    public TwoRegImmediateInstruction(TwoRegImmediateInstruction obj)
    {
        super(obj);
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

    @Override
    public String toString()
    {
        if (InstructionUtils.isLoadStore(this))
            return "";
        return dest.getDestinationLabel() + ", " + src1.getSourceLabel() + ", "
                + immediate;
    }
}
