package com.umbc.courses.aca.projects.mips.functionalUnits;

import com.umbc.courses.aca.projects.mips.instructions.Instruction;
import com.umbc.courses.aca.projects.mips.instructions.InstructionUtils;
import com.umbc.courses.aca.projects.mips.instructions.WriteBackObject;
import com.umbc.courses.aca.projects.mips.main.CPU;
import com.umbc.courses.aca.projects.mips.registers.RegisterManager;
import com.umbc.courses.aca.projects.mips.results.ResultsManager;
import com.umbc.courses.aca.projects.mips.stages.StageType;

public class WriteBackUnit extends FunctionalUnit
{

    private static volatile WriteBackUnit instance;

    public static WriteBackUnit getInstance()
    {
        if (null == instance)
            synchronized (WriteBackUnit.class)
            {
                if (null == instance)
                    instance = new WriteBackUnit();
            }

        return instance;
    }

    private WriteBackUnit()
    {
        super();
        isPipelined = false;
        clockCyclesRequired = 1;
        pipelineSize = 1;
        stageId = StageType.WBSTAGE;
        createPipelineQueue(pipelineSize);
    }

    @Override
    public void executeUnit() throws Exception
    {
        Instruction inst = peekFirst();

        if (InstructionUtils.isNOOP(inst))
            return;

        System.out.println(CPU.CLOCK + " WBUnit " + inst.debugString());

        // Write back the data to the destination register if any and unlock
        // destination register as Free
        WriteBackObject writeBackObject = inst.getDestinationRegister();

        if (writeBackObject != null)
        {
            RegisterManager.instance.setRegisterValue(
                    writeBackObject.getDestinationLabel(),
                    writeBackObject.getDestination());
            RegisterManager.instance.setRegisterFree(writeBackObject
                    .getDestinationLabel());
        }

        // Update the exit cycle in the instruction and pass it on to the result
        updateExitClockCycle(inst);
        // manager for printing
        ResultsManager.instance.addInstruction(inst);

        // Remove the instruction from the queue and enqueue a NOOP
        rotatePipe();
    }

    @Override
    public int getClockCyclesRequiredForNonPipeLinedUnit()
    {
        // TODO Auto-generated method stub
        return clockCyclesRequired;
    }
}
