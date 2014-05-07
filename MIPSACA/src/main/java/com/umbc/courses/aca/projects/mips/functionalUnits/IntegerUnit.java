package com.umbc.courses.aca.projects.mips.functionalUnits;

import com.umbc.courses.aca.projects.mips.instructions.Instruction;
import com.umbc.courses.aca.projects.mips.instructions.InstructionUtils;
import com.umbc.courses.aca.projects.mips.main.CPU;
import com.umbc.courses.aca.projects.mips.stages.StageType;

public class IntegerUnit extends FunctionalUnit
{

    private static volatile IntegerUnit instance;

    public static IntegerUnit getInstance()
    {
        if (null == instance)
            synchronized (IntegerUnit.class)
            {
                if (null == instance)
                    instance = new IntegerUnit();
            }

        return instance;
    }

    private IntegerUnit()
    {
        super();
        isPipelined = false;
        clockCyclesRequired = 1;
        pipelineSize = 1;
        stageId = StageType.EXSTAGE;
        createPipelineQueue(pipelineSize);

    }

    @Override
    public void executeUnit() throws Exception
    {
        validateQueueSize();

        Instruction inst = peekFirst();

        if (InstructionUtils.isNOOP(inst))
            return;

        inst.executeInstruction();

        System.out.println(CPU.CLOCK + " Intger " + inst.debugString());

        if (MemoryUnit.getInstance().checkIfFree(inst))
        {
            MemoryUnit.getInstance().acceptInstruction(inst);
            updateExitClockCycle(inst);
            rotatePipe();
        }
        else
        {
            markStructHazard();
        }

    }

    @Override
    public int getClockCyclesRequiredForNonPipeLinedUnit()
    {
        return clockCyclesRequired;
    }
}
