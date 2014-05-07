package instructions;

public class InstructionUtils
{

    public static boolean isStore(Instruction inst)
    {
        return (inst instanceof StoreInstruction);
    }

    public static boolean isLoad(Instruction inst)
    {
        return (inst instanceof LoadInstruction);
    }

    public static boolean isLoadStore(Instruction inst)
    {
        return isLoad(inst) || isStore(inst);
    }

}
