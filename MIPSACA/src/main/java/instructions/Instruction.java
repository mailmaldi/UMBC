package instructions;

import java.util.Arrays;

public abstract class Instruction implements InstructionI
{
    private boolean              RAW;
    private boolean              WAR;
    private boolean              WAW;
    private boolean              STRUCT;
    private EXFunctionalUnitType functionalUnitType;
    private InstructionType      instructionType;

    private int[]                entryCycle;
    private int[]                exitCycle;

    private String               printableInstruction;

    private long                 destinationAddress;  // HACK

    public Instruction()
    {
        super();
        this.entryCycle = new int[4];
        this.exitCycle = new int[4];
        this.RAW = false;
        this.WAR = false;
        this.WAW = false;
        this.STRUCT = false;
        this.setFunctionalUnitType(EXFunctionalUnitType.UNKNOWN);
        this.setInstructionType(InstructionType.UNKNOWN);
    }

    public Instruction(Instruction obj)
    {
        super();
        this.RAW = obj.RAW;
        this.WAR = obj.WAR;
        this.WAW = obj.WAW;
        this.STRUCT = obj.STRUCT;
        this.entryCycle = new int[obj.entryCycle.length];
        this.exitCycle = new int[obj.exitCycle.length];
        System.arraycopy(obj.entryCycle, 0, this.entryCycle, 0,
                this.entryCycle.length);
        System.arraycopy(obj.exitCycle, 0, this.exitCycle, 0,
                this.exitCycle.length);
        this.setFunctionalUnitType(obj.getFunctionalUnitType());
        this.setInstructionType(obj.getInstructionType());
        this.setPrintableInstruction(obj.printableInstruction);
    }

    // Purely for decorative purposes
    public void setPrintableInstruction(String str)
    {
        this.printableInstruction = str;
    }

    public String debugString()
    {
        return String.format(Utils.Constants.instructionDebugFormatString,
                printableInstruction, Arrays.toString(entryCycle),
                Arrays.toString(exitCycle), RAW ? 'Y' : 'N', WAR ? 'Y' : 'N',
                WAW ? 'Y' : 'N', STRUCT ? 'Y' : 'N');
    }

    public String getOutputString()
    {
        return String.format(Utils.Constants.instructionOutputFormatString,
                printableInstruction, exitCycle[0] != 0 ? exitCycle[0] : "",
                exitCycle[1] != 0 ? exitCycle[1] : "",
                exitCycle[2] != 0 ? exitCycle[2] : "",
                exitCycle[3] != 0 ? exitCycle[3] : "", RAW ? 'Y' : 'N',
                WAR ? 'Y' : 'N', WAW ? 'Y' : 'N', STRUCT ? 'Y' : 'N');
    }

    public void setRAW(boolean b)
    {
        this.RAW = b;
    }

    public void setWAW(boolean b)
    {
        this.WAW = b;
    }

    public void setStruct(boolean b)
    {
        STRUCT = b;
    }

    public long getDestinationAddress() throws Exception
    {
        if (InstructionUtils.isLoadStore(this))
            return destinationAddress;
        else
            throw new Exception(
                    " called getDestinationAddress for non-LoadStore "
                            + toString());
    }

    protected void setDestinationAddress(long address) throws Exception
    {
        if (InstructionUtils.isLoadStore(this))
            this.destinationAddress = address;
        else
            throw new Exception(
                    " called setDestinationAddress for non-LoadStore "
                            + toString());
    }

    public EXFunctionalUnitType getFunctionalUnitType()
    {
        return functionalUnitType;
    }

    protected void setFunctionalUnitType(EXFunctionalUnitType functionalUnitType)
    {
        this.functionalUnitType = functionalUnitType;
    }

    public InstructionType getInstructionType()
    {
        return instructionType;
    }

    protected void setInstructionType(InstructionType instructionType)
    {
        this.instructionType = instructionType;
    }

    public int getEntryCycleForStage(int stageId)
    {
        return entryCycle[stageId];
    }

    public int getExitCycleForStage(int stageId)
    {
        return exitCycle[stageId];
    }

    public void setEntryCycleForStage(int stageId, int value)
    {
        entryCycle[stageId] = value;
    }

    public void setExitCycleForStage(int stageId, int value)
    {
        exitCycle[stageId] = value;
    }

}
