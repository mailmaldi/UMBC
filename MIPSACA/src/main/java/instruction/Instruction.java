package instruction;

public abstract class Instruction implements InstructionI
{
    /* Hazards */
    public boolean RAW;
    public boolean WAR;
    public boolean WAW;
    public boolean STRUCT;

    public int[]   entryCycle;
    public int[]   exitCycle;

    public String  printableInstruction = "";

    public Instruction()
    {
        super();
        this.entryCycle = new int[4];
        this.exitCycle = new int[4];
        this.RAW = false;
        this.WAR = false;
        this.WAW = false;
        this.STRUCT = false;
    }

    // Purely for decorative purposes
    public void setPrintableInstruction(String str)
    {
        this.printableInstruction = str;
    }

}
