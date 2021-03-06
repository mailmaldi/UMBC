package com.umbc.courses.aca.projects.mips.instructions;

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

    public static boolean isNOOP(Instruction inst)
    {
        return (inst instanceof NOOP);
    }

    public static boolean isHLT(Instruction inst)
    {
        return (inst instanceof HLT);
    }

    public static boolean isJump(Instruction inst)
    {
        return (inst instanceof J);
    }

    public static boolean isBranch(Instruction inst)
    {
        return (inst instanceof ConditionalBranchInstruction);
    }

    public static boolean isDoubleLoadStore(Instruction inst)
    {
        return (isLoadStore(inst) && (inst instanceof LD || inst instanceof SD));
    }

}
