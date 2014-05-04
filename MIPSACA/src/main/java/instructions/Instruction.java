package instructions;

import java.util.Arrays;

public abstract class Instruction implements InstructionI
{
    /* Hazards */
    public boolean            RAW;
    public boolean            WAR;
    public boolean            WAW;
    public boolean            STRUCT;
    public FunctionalUnitType functionalUnitType;
    public InstructionType    instructionType;

    public int[]              entryCycle;
    public int[]              exitCycle;

    public String             printableInstruction;

    public long               address;             // HACK

    public Instruction()
    {
        super();
        this.entryCycle = new int[4];
        this.exitCycle = new int[4];
        this.RAW = false;
        this.WAR = false;
        this.WAW = false;
        this.STRUCT = false;
        this.functionalUnitType = FunctionalUnitType.UNKNOWN;
        this.instructionType = InstructionType.UNKNOWN;
    }

    public Instruction(Instruction obj)
    {
        super();
        // System.out.println("Copy Constructor of Instruction SuperClass: "
        // + obj.printableInstruction);
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
        this.functionalUnitType = obj.functionalUnitType;
        this.instructionType = obj.instructionType;
    }

    // Purely for decorative purposes
    public void setPrintableInstruction(String str)
    {
        this.printableInstruction = str;
    }

    public String debugString()
    {
        String format = "%-25s %-10s %-10s";
        return String.format(format, printableInstruction,
                Arrays.toString(entryCycle), Arrays.toString(exitCycle));
    }

}