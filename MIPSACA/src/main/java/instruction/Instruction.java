package instruction;

import java.util.List;

public abstract class Instruction
{

    /* Hazards */
    public boolean RAW;
    public boolean WAR;
    public boolean WAW;
    public boolean STRUCT;

    public int[]   entryCycle           = new int[4];
    public int[]   exitCycle            = new int[4];

    public String  printableInstruction = "";

    public Instruction()
    {
        super();
        this.RAW = false;
        this.WAR = false;
        this.WAW = false;
        this.STRUCT = false;
    }

    public abstract List<String> getSourceRegister();

    public abstract String getDestinationRegister();

    public abstract void executeInstruction();

    public abstract void decodeInstruction();

    public abstract void writeBackResult();

    // Purely for decorative purposes
    public void setPrintableInstruction(String str)
    {
        this.printableInstruction = str;
    }

}
